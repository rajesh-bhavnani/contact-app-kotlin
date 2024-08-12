package com.example.custom_contact.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.custom_contact.ui.AppNavigation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    val navController = rememberNavController()

    Scaffold { padding ->
        Box(modifier = Modifier.padding(padding)) {
            AppNavigation(navController = navController)
        }
    }
}