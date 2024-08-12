package com.example.custom_contact.domain.usecase

import com.example.custom_contact.domain.model.Contact
import com.example.custom_contact.domain.repository.ContactRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetContactsUseCase @Inject constructor(
    private val repository: ContactRepository
) {
    operator fun invoke(): Flow<List<Contact>> {
        return repository.getContactsFlow()
    }

    suspend operator fun invoke(id: Long): Contact? {
        return repository.getContactById(id)
    }
}