package com.example.custom_contact.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.custom_contact.ui.screen.ContactDetailScreen
import com.example.custom_contact.ui.screen.ContactFormScreen
import com.example.custom_contact.ui.screen.ContactListScreen

sealed class Screen(val route: String) {
    object ContactList : Screen("contactList")
    object ContactForm : Screen("contactForm?contactId={contactId}") {
        fun createRoute(contactId: Long?) = "contactForm?contactId=$contactId"
    }
    object ContactDetail : Screen("contactDetail/{contactId}") {
        fun createRoute(contactId: Long) = "contactDetail/$contactId"
    }
}

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.ContactList.route) {
        composable(Screen.ContactList.route) {
            ContactListScreen(
                onAddContact = { navController.navigate(Screen.ContactForm.createRoute(null)) },
                onContactClick = { contactId ->
                    navController.navigate(Screen.ContactDetail.createRoute(contactId))
                },
                onEditContact = { contactId ->
                    navController.navigate(Screen.ContactForm.createRoute(contactId))
                }
            )
        }
        composable(
            route = Screen.ContactForm.route,
            arguments = listOf(navArgument("contactId") { type = NavType.LongType; defaultValue = -1L })
        ) { backStackEntry ->
            val contactId = backStackEntry.arguments?.getLong("contactId")
            ContactFormScreen(
                contactId = if (contactId != -1L) contactId else null,
                onDismiss = { navController.popBackStack() },
                onSave = { navController.popBackStack() }
            )
        }
        composable(
            route = Screen.ContactDetail.route,
            arguments = listOf(navArgument("contactId") { type = NavType.LongType })
        ) { backStackEntry ->
            val contactId = backStackEntry.arguments?.getLong("contactId") ?: return@composable
            ContactDetailScreen(
                contactId = contactId,
                onNavigateBack = { navController.popBackStack() },
                onEditContact = { navController.navigate(Screen.ContactForm.createRoute(contactId)) }
            )
        }
    }
}