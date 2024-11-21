package com.arunabha.expensetracker

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking


class StoreUserInfo(private val context: Context) {

    // To make sure there's only one instance
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("userInfo")
        val USER_NAME_KEY = stringPreferencesKey("userName")
        val IS_REGISTERED_KEY = booleanPreferencesKey("isRegistered")
    }

    // Get saved name
    val getName: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[USER_NAME_KEY] ?: "Buddy"
    }

    // Get registration status and go to respective screen
    fun readRegistrationStatusAndGoToParticularScreen(context: Context): String {
        return runBlocking {
            context.dataStore.data
                .map { preferences -> preferences[IS_REGISTERED_KEY] ?: false }
                .first() // Synchronously get the first emitted value
                .let { isRegistered -> if (isRegistered) "/home" else "/welcome" }
        }
    }


    // Save name
    suspend fun saveName(name: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_NAME_KEY] = name
        }
    }

    // Save registration status
    suspend fun saveRegStatus(isReg: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[IS_REGISTERED_KEY] = isReg
        }
    }
}