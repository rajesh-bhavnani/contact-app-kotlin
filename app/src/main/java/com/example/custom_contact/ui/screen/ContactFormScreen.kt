package com.example.custom_contact.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.custom_contact.ui.viewmodel.ContactFormViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactFormScreen(
    contactId: Long? = null,
    onDismiss: () -> Unit,
    onSave: () -> Unit,
    viewModel: ContactFormViewModel = hiltViewModel()
) {
    val name by viewModel.name.collectAsState()
    val countryCode by viewModel.countryCode.collectAsState()
    val phoneNumber by viewModel.phoneNumber.collectAsState()
    val email by viewModel.email.collectAsState()
    val relationship by viewModel.relationship.collectAsState()
    val isSaving by viewModel.isSaving.collectAsState()
    val isFormValid by viewModel.isFormValid.collectAsState()

    val nameError by viewModel.nameError.collectAsState()
    val phoneError by viewModel.phoneError.collectAsState()
    val emailError by viewModel.emailError.collectAsState()
    val relationshipError by viewModel.relationshipError.collectAsState()

    LaunchedEffect(contactId) {
        contactId?.let { viewModel.loadContact(it) }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (contactId == null) "Add Contact" else "Edit Contact") },
                navigationIcon = {
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp, vertical = 24.dp)
        ) {
            val textFieldModifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)

            OutlinedTextField(
                value = name,
                onValueChange = { viewModel.onNameChange(it) },
                label = { Text("Name") },
                modifier = textFieldModifier,
                singleLine = true,
                isError = nameError != null,
                supportingText = {
                    nameError?.let { error ->
                        Text(
                            text = error,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                    errorBorderColor = MaterialTheme.colorScheme.error,
                ),
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = countryCode,
                    onValueChange = {
                        if (it.startsWith("+") && it.length <= 4) {
                            viewModel.onCountryCodeChange(it)
                        }
                    },
                    label = { Text("Code") },
                    modifier = Modifier.weight(0.3f),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                )

                OutlinedTextField(
                    value = phoneNumber,
                    onValueChange = { viewModel.onPhoneNumberChange(it) },
                    label = { Text("Phone") },
                    modifier = Modifier.weight(0.7f),
                    singleLine = true,
                    isError = phoneError != null,
                    supportingText = {
                        phoneError?.let { error ->
                            Text(
                                text = error,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                        errorBorderColor = MaterialTheme.colorScheme.error,
                    ),
                )
            }

            OutlinedTextField(
                value = email,
                onValueChange = { viewModel.onEmailChange(it) },
                label = { Text("Email") },
                modifier = textFieldModifier,
                singleLine = true,
                isError = emailError != null,
                supportingText = {
                    emailError?.let { error ->
                        Text(
                            text = error,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                    errorBorderColor = MaterialTheme.colorScheme.error,
                ),
            )

            OutlinedTextField(
                value = relationship,
                onValueChange = { viewModel.onRelationshipChange(it) },
                label = { Text("Relationship") },
                modifier = textFieldModifier,
                singleLine = true,
                isError = relationshipError != null,
                supportingText = {
                    relationshipError?.let { error ->
                        Text(
                            text = error,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                    errorBorderColor = MaterialTheme.colorScheme.error,
                ),
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { viewModel.saveContact(onSave) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                enabled = isFormValid && !isSaving,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    disabledContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
                )
            ) {
                Text(
                    text = if (isSaving) "Saving..." else if (contactId == null) "Save" else "Update",
                    color = if (isFormValid && !isSaving)
                        MaterialTheme.colorScheme.onPrimary
                    else
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                )
            }
        }
    }
}