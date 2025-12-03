package com.example.appsindempart_grupo14.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.preferencesDataStoreFile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SessionManager(private val context: Context) {

    private val dataStore: DataStore<Preferences> =
        PreferenceDataStoreFactory.create(
            produceFile = { context.preferencesDataStoreFile("session") }
        )

    companion object {
        val KEY_EMAIL = stringPreferencesKey("email_usuario")
    }

    val emailUsuario: Flow<String?> = dataStore.data.map { pref ->
        pref[KEY_EMAIL]
    }

    suspend fun guardarSesion(email: String) {
        dataStore.edit { pref ->
            pref[KEY_EMAIL] = email
        }
    }

    suspend fun cerrarSesion() {
        dataStore.edit { pref ->
            pref.remove(KEY_EMAIL)
        }
    }
}
