package cz.cvut.fel.zan.macham29_zan.api

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import cz.cvut.fel.zan.macham29_zan.BuildConfig
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "app_settings")

class SettingsDataStore(private val context: Context) {
    companion object {
        val API_TOKEN = stringPreferencesKey("api_token")
    }

    suspend fun saveToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[API_TOKEN] = token
        }
    }

    val apiTokenFlow: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[API_TOKEN] ?: BuildConfig.API_TOKEN
        }
}