package com.example.custom_contact.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.custom_contact.domain.model.Contact
import com.example.custom_contact.domain.usecase.AddContactUseCase
import com.example.custom_contact.domain.usecase.GetContactsUseCase
import com.example.custom_contact.domain.usecase.UpdateContactUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import java.util.regex.Pattern

@HiltViewModel
class ContactFormViewModel @Inject constructor(
    private val addContactUseCase: AddContactUseCase,
    private val updateContactUseCase: UpdateContactUseCase,
    private val getContactsUseCase: GetContactsUseCase
) : ViewModel() {

    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name.asStateFlow()

    private val _countryCode = MutableStateFlow("+1")
    val countryCode: StateFlow<String> = _countryCode.asStateFlow()

    private val _phoneNumber = MutableStateFlow("")
    val phoneNumber: StateFlow<String> = _phoneNumber.asStateFlow()

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _relationship = MutableStateFlow("")
    val relationship: StateFlow<String> = _relationship.asStateFlow()

    private val _isSaving = MutableStateFlow(false)
    val isSaving: StateFlow<Boolean> = _isSaving.asStateFlow()

    private val _nameError = MutableStateFlow<String?>(null)
    val nameError: StateFlow<String?> = _nameError.asStateFlow()

    private val _phoneError = MutableStateFlow<String?>(null)
    val phoneError: StateFlow<String?> = _phoneError.asStateFlow()

    private val _emailError = MutableStateFlow<String?>(null)
    val emailError: StateFlow<String?> = _emailError.asStateFlow()

    private val _relationshipError = MutableStateFlow<String?>(null)
    val relationshipError: StateFlow<String?> = _relationshipError.asStateFlow()

    private val _isFormValid = MutableStateFlow(false)
    val isFormValid: StateFlow<Boolean> = _isFormValid.asStateFlow()

    private val phoneRegex = Pattern.compile("^\\(\\d{3}\\) \\d{3} \\d{4}$")

    private val emailRegex = Pattern.compile(
        "[a-zA-Z0-9+._%\\-]{1,256}" +
                "@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+"
    )

    private var currentContactId: Long? = null

    fun loadContact(contactId: Long) {
        viewModelScope.launch {
            val contact = getContactsUseCase(contactId)
            contact?.let {
                currentContactId = it.id
                _name.value = it.name
                if (it.phoneNumber.startsWith("+")) {
                    val phoneparts = it.phoneNumber.split(" ", limit = 2)
                    _countryCode.value = phoneparts.firstOrNull() ?: "+1"
                    _phoneNumber.value = phoneparts.getOrNull(1) ?: ""
                } else {
                    _countryCode.value = "+1"
                    _phoneNumber.value = it.phoneNumber
                }
                _email.value = it.email
                _relationship.value = it.relationship
                updateFormValidity()
            }
        }
    }

    fun onNameChange(name: String) {
        _name.value = name
        validateName(name)
        updateFormValidity()
    }

    fun onCountryCodeChange(countryCode: String) {
        _countryCode.value = countryCode
        validatePhone(_phoneNumber.value)
        updateFormValidity()
    }

    fun onPhoneNumberChange(phoneNumber: String) {
        val formattedNumber = formatPhoneNumber(phoneNumber)
        _phoneNumber.value = formattedNumber
        validatePhone(formattedNumber)
        updateFormValidity()
    }

    fun onEmailChange(email: String) {
        _email.value = email
        validateEmail(email)
        updateFormValidity()
    }

    fun onRelationshipChange(relationship: String) {
        _relationship.value = relationship
        validateRelationship(relationship)
        updateFormValidity()
    }

    private fun validateName(name: String) {
        _nameError.value = when {
            name.isBlank() -> "Name cannot be blank"
            else -> null
        }
    }

    private fun validatePhone(phone: String) {
        _phoneError.value = when {
            phone.isBlank() -> "Phone number cannot be empty"
            !phoneRegex.matcher(phone).matches() -> "Invalid phone number format. Use: (XXX) XXX XXXX"
            else -> null
        }
    }

    private fun validateEmail(email: String) {
        _emailError.value = when {
            email.isBlank() -> "Email cannot be empty"
            !emailRegex.matcher(email).matches() -> "Invalid email format"
            else -> null
        }
    }

    private fun validateRelationship(relationship: String) {
        _relationshipError.value = when {
            relationship.isBlank() -> "Relationship cannot be empty"
            else -> null
        }
    }

    private fun updateFormValidity() {
        _isFormValid.update {
            _nameError.value == null &&
                    _phoneError.value == null &&
                    _emailError.value == null &&
                    _relationshipError.value == null &&
                    _name.value.isNotBlank() &&
                    _phoneNumber.value.isNotBlank() &&
                    _email.value.isNotBlank() &&
                    _relationship.value.isNotBlank()
        }
    }

    private fun formatPhoneNumber(input: String): String {
        val digitsOnly = input.filter { it.isDigit() }
        return when {
            digitsOnly.length <= 3 -> "($digitsOnly"
            digitsOnly.length <= 6 -> "(${digitsOnly.substring(0, 3)}) ${digitsOnly.substring(3)}"
            digitsOnly.length <= 10 -> "(${digitsOnly.substring(0, 3)}) ${digitsOnly.substring(3, 6)} ${digitsOnly.substring(6)}"
            else -> "(${digitsOnly.substring(0, 3)}) ${digitsOnly.substring(3, 6)} ${digitsOnly.substring(6, 10)}"
        }
    }

    fun saveContact(onSuccess: () -> Unit) {
        if (!_isFormValid.value) {
            return
        }

        viewModelScope.launch {
            _isSaving.value = true
            val contact = Contact(
                id = currentContactId ?: 0, // Use 0 for new contacts, existing ID for updates
                name = _name.value,
                phoneNumber = "${_countryCode.value} ${_phoneNumber.value}",
                email = _email.value,
                relationship = _relationship.value
            )

            if (currentContactId == null) {
                addContactUseCase(contact)
            } else {
                updateContactUseCase(contact)
            }

            _isSaving.value = false
            onSuccess()
        }
    }
}