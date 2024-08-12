package com.example.custom_contact.di

import com.example.custom_contact.domain.repository.ContactRepository
import com.example.custom_contact.domain.usecase.AddContactUseCase
import com.example.custom_contact.domain.usecase.GetContactsUseCase
import com.example.custom_contact.domain.usecase.SearchContactsUseCase
import com.example.custom_contact.domain.usecase.UpdateContactUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideAddContactUseCase(repository: ContactRepository): AddContactUseCase {
        return AddContactUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetContactsUseCase(repository: ContactRepository): GetContactsUseCase {
        return GetContactsUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideSearchContactsUseCase(repository: ContactRepository): SearchContactsUseCase {
        return SearchContactsUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideUpdateContactUseCase(repository: ContactRepository): UpdateContactUseCase {
        return UpdateContactUseCase(repository)
    }
}