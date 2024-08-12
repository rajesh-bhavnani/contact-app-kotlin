package com.example.custom_contact.domain.model

data class Contact(
    val id: Long = 0,
    val name: String,
    val phoneNumber: String,
    val email: String = "",
    val relationship: String = ""
) {
    fun doesMatchSearchQuery(query: String): Boolean {
        val matchingCombinations = listOf(
            "$name$phoneNumber",
            "$name $phoneNumber",
            "${name.replace(" ", "")}$phoneNumber",
            "${name.replace(" ", "")} $phoneNumber"
        )

        return matchingCombinations.any {
            it.contains(query, ignoreCase = true)
        }
    }
}