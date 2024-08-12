package com.example.custom_contact.domain.usecase

import com.example.custom_contact.domain.model.Contact
import com.example.custom_contact.domain.repository.ContactRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SearchContactsUseCase @Inject constructor(
    private val repository: ContactRepository
) {
    operator fun invoke(query: String): Flow<List<Contact>> {
        return repository.getAllContacts().map { contacts ->
            if (query.isBlank()) {
                contacts
            } else {
                contacts.filter { contact ->
                    contact.doesMatchSearchQuery(query)
                }
            }
        }
    }
}