package com.example.custom_contact.di


import android.content.Context
import androidx.room.Room
import com.example.custom_contact.data.datasource.local.ContactDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideContactDatabase(@ApplicationContext context: Context): ContactDatabase {
        return Room.databaseBuilder(
            context,
            ContactDatabase::class.java,
            "contact_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideContactDao(database: ContactDatabase) = database.contactDao()
}