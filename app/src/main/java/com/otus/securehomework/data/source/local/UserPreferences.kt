package com.otus.securehomework.data.source.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.otus.securehomework.security.AesEncryptionManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private const val DATA_STORE_NAME: String = "secure_pref"

class UserPreferences @Inject constructor(
    private val context: Context,
    private val encryptionAes: AesEncryptionManager
) {

    private val Context.dataStore by preferencesDataStore(name = DATA_STORE_NAME)

    val accessToken: Flow<CharSequence?>
        get() = context.dataStore.data.map { preferences ->
            preferences[ACCESS_TOKEN]?.let {
                encryptionAes.decryptAes(it)
            }
        }

    val refreshToken: Flow<CharSequence?>
        get() = context.dataStore.data.map { preferences ->
            preferences[REFRESH_TOKEN]
        }

    suspend fun saveAccessTokens(accessToken: String?, refreshToken: String?) {
        context.dataStore.edit { preferences ->
            accessToken?.let { encryptedToken ->
                preferences[ACCESS_TOKEN] = encryptionAes.encryptAes(encryptedToken)
            }
            refreshToken?.let {
                preferences[REFRESH_TOKEN] = it
            }
        }
    }

    suspend fun clear() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    companion object {
        private val ACCESS_TOKEN = stringPreferencesKey("key_access_token")
        private val REFRESH_TOKEN = stringPreferencesKey("key_refresh_token")
    }
}
