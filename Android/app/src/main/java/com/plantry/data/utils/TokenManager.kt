package com.plantry.data.utils

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TokenManager constructor(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        private val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
        private val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token")
    }

    fun getAccessToken(): Flow<String?> {
        return dataStore.data.map { prefs ->
            prefs[ACCESS_TOKEN_KEY]
        }
    }

    suspend fun saveAccessToken(token: String?){
        dataStore.edit { prefs ->
            if(!token.isNullOrBlank()){
                prefs[ACCESS_TOKEN_KEY] = token
            }
            else {
                prefs[ACCESS_TOKEN_KEY] = ""

            }
        }
    }

    suspend fun deleteAccessToken(){
        dataStore.edit { prefs ->
            prefs.remove(ACCESS_TOKEN_KEY)
        }
    }

    fun getRefreshToken(): Flow<String?> {
        return dataStore.data.map { prefs ->
            prefs[REFRESH_TOKEN_KEY]
        }
    }

    suspend fun saveRefreshToken(token: String?){
        dataStore.edit { prefs ->
            if(!token.isNullOrBlank()){
                prefs[REFRESH_TOKEN_KEY] = token
            }
            else {
                prefs[REFRESH_TOKEN_KEY] = ""
            }
        }
    }

    private suspend fun deleteRefreshToken(){
        dataStore.edit { prefs ->
            prefs.remove(REFRESH_TOKEN_KEY)
        }
    }

    suspend fun deleteTokens(){
        deleteAccessToken()
        deleteRefreshToken()
    }
}