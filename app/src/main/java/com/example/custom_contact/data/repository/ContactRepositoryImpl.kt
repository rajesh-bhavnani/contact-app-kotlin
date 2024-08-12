package com.example.custom_contact.data.repository

import com.example.custom_contact.data.datasource.local.ContactDao
import com.example.custom_contact.data.datasource.local.ContactEntity
import com.example.custom_contact.domain.model.Contact
import com.example.custom_contact.domain.repository.ContactRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContactRepositoryImpl @Inject constructor(
    private val contactDao: ContactDao
) : ContactRepository {

    override fun getAllContacts(): Flow<List<Contact>> {
        return contactDao.getAllContacts().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override suspend fun addContact(contact: Contact) {
        contactDao.insertContact(contact.toEntity())
    }

    override fun getContactsFlow(): Flow<List<Contact>> {
        return contactDao.getAllContacts().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override suspend fun searchContacts(query: String): List<Contact> {
        return contactDao.searchContacts(query).map { it.toDomainModel() }
    }

    private fun ContactEntity.toDomainModel(): Contact {
        return Contact(
            id = id,
            name = name,
            phoneNumber = phoneNumber,
            email = email,
            relationship = relationship
        )
    }

    private fun Contact.toEntity(): ContactEntity {
        return ContactEntity(
            id = id,
            name = name,
            phoneNumber = phoneNumber,
            email = email,
            relationship = relationship
        )
    }

    override suspend fun getContactCount(): Int {
        return contactDao.getContactCount()
    }

    override suspend fun getContactById(id: Long): Contact? {
        return contactDao.getContactById(id)?.toDomainModel()
    }

    override suspend fun updateContact(contact: Contact) {
        contactDao.updateContact(contact.toEntity())
    }
}