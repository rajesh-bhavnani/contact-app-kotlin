package com.example.custom_contact.domain.usecase

import com.example.custom_contact.domain.model.Contact
import com.example.custom_contact.domain.repository.ContactRepository
import javax.inject.Inject

class UpdateContactUseCase @Inject constructor(
    private val repository: ContactRepository
) {
    suspend operator fun invoke(contact: Contact) {
        repository.updateContact(contact)
    }
}