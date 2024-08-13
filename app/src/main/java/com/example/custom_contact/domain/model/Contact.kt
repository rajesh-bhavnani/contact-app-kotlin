package com.example.custom_contact.domain.model

import kotlin.math.max
import kotlin.math.min

data class Contact(
    val id: Long = 0,
    val name: String,
    val phoneNumber: String,
    val email: String = "",
    val relationship: String = ""
) {
    companion object {
        private const val NAME_MATCH_THRESHOLD = 0.55
        private const val PHONE_MATCH_THRESHOLD = 0.65
    }

    fun matchesQuery(query: String): Boolean {
        val normalizedQuery = query.lowercase()
        val nameScore = calculateSimilarityScore(normalizedQuery, name.lowercase())
        val phoneScore = calculateSimilarityScore(normalizedQuery, phoneNumber.lowercase())
        return nameScore > NAME_MATCH_THRESHOLD || phoneScore > PHONE_MATCH_THRESHOLD
    }

    fun searchScore(query: String): Double {
        val normalizedQuery = query.lowercase()
        val nameScore = calculateSimilarityScore(normalizedQuery, name.lowercase())
        val phoneScore = calculateSimilarityScore(normalizedQuery, phoneNumber.lowercase())
        return max(nameScore, phoneScore)
    }

    private fun calculateSimilarityScore(query: String, text: String): Double {
        val levenshteinScore = calculateLevenshteinScore(query, text)
        val longestSubstringScore = calculateLongestSubstringScore(query, text)
        val jaroWinklerScore = calculateJaroWinklerScore(query, text)

        return (levenshteinScore + longestSubstringScore + jaroWinklerScore) / 3.0
    }

    private fun calculateLevenshteinScore(s1: String, s2: String): Double {
        val distance = levenshteinDistance(s1, s2)
        return 1.0 - distance.toDouble() / max(s1.length, s2.length)
    }

    private fun calculateLongestSubstringScore(s1: String, s2: String): Double {
        val longestCommonLength = longestCommonSubstring(s1, s2)
        return longestCommonLength.toDouble() / s1.length
    }

    private fun calculateJaroWinklerScore(s1: String, s2: String): Double {
        return jaroWinkler(s1, s2)
    }

    private fun levenshteinDistance(s1: String, s2: String): Int {
        val dp = Array(s1.length + 1) { IntArray(s2.length + 1) }

        for (i in dp.indices) dp[i][0] = i
        for (j in dp[0].indices) dp[0][j] = j

        for (i in 1..s1.length) {
            for (j in 1..s2.length) {
                dp[i][j] = if (s1[i - 1] == s2[j - 1]) {
                    dp[i - 1][j - 1]
                } else {
                    1 + min(dp[i - 1][j], min(dp[i][j - 1], dp[i - 1][j - 1]))
                }
            }
        }

        return dp[s1.length][s2.length]
    }

    private fun longestCommonSubstring(s1: String, s2: String): Int {
        val dp = Array(s1.length + 1) { IntArray(s2.length + 1) }
        var maxLength = 0

        for (i in 1..s1.length) {
            for (j in 1..s2.length) {
                if (s1[i - 1] == s2[j - 1]) {
                    dp[i][j] = dp[i - 1][j - 1] + 1
                    maxLength = max(maxLength, dp[i][j])
                }
            }
        }

        return maxLength
    }

    private fun jaroWinkler(s1: String, s2: String): Double {
        val jaroScore = calculateJaroSimilarity(s1, s2)
        val commonPrefixLength = s1.zip(s2).takeWhile { (c1, c2) -> c1 == c2 }.count().coerceAtMost(4)
        return jaroScore + (commonPrefixLength * 0.1 * (1 - jaroScore))
    }

    private fun calculateJaroSimilarity(s1: String, s2: String): Double {
        if (s1 == s2) return 1.0
        val matchDistance = (max(s1.length, s2.length) / 2) - 1

        val s1Matches = BooleanArray(s1.length)
        val s2Matches = BooleanArray(s2.length)

        var matches = 0
        for (i in s1.indices) {
            val start = max(0, i - matchDistance)
            val end = min(i + matchDistance + 1, s2.length)

            for (j in start until end) {
                if (!s2Matches[j] && s1[i] == s2[j]) {
                    s1Matches[i] = true
                    s2Matches[j] = true
                    matches++
                    break
                }
            }
        }

        if (matches == 0) return 0.0

        var transpositions = 0
        var k = 0
        for (i in s1.indices) {
            if (s1Matches[i]) {
                while (!s2Matches[k]) k++
                if (s1[i] != s2[k]) transpositions++
                k++
            }
        }

        val m = matches.toDouble()
        return ((m / s1.length) + (m / s2.length) + ((m - transpositions / 2.0) / m)) / 3.0
    }
}