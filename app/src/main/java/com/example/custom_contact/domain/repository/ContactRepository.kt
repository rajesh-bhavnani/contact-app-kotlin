package com.example.custom_contact.domain.repository

import com.example.custom_contact.domain.model.Contact
import kotlinx.coroutines.flow.Flow

interface ContactRepository {
    fun getAllContacts(): Flow<List<Contact>>
    suspend fun addContact(contact: Contact)
    fun getContactsFlow(): Flow<List<Contact>>
    suspend fun searchContacts(query: String): List<Contact>
    suspend fun getContactCount(): Int
    suspend fun getContactById(id: Long): Contact?
    suspend fun updateContact(contact: Contact)
}