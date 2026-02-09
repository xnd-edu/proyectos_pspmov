package com.example.composeapp.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.composeapp.common.DataStoreConstants
import com.example.composeapp.common.UserRoles
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = DataStoreConstants.AUTH_PREFS)

@Singleton
class TokenManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val accessTokenKey = stringPreferencesKey(DataStoreConstants.ACCESS_TOKEN_KEY)
    private val refreshTokenKey = stringPreferencesKey(DataStoreConstants.REFRESH_TOKEN_KEY)
    private val userRoleKey = stringPreferencesKey(DataStoreConstants.USER_ROLE_KEY)
    private val userIdKey = stringPreferencesKey(DataStoreConstants.USER_ID_KEY)
    private val usernameKey = stringPreferencesKey(DataStoreConstants.USERNAME_KEY)
    private val userEmailKey = stringPreferencesKey(DataStoreConstants.USER_EMAIL_KEY)
    private val userNombreKey = stringPreferencesKey(DataStoreConstants.USER_NOMBRE_KEY)

    suspend fun saveAccessToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[accessTokenKey] = token
        }
    }

    fun getAccessToken(): String? = runBlocking {
        context.dataStore.data.map { preferences ->
            preferences[accessTokenKey]
        }.first()
    }

    suspend fun saveRefreshToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[refreshTokenKey] = token
        }
    }

    fun getRefreshToken(): String? = runBlocking {
        context.dataStore.data.map { preferences ->
            preferences[refreshTokenKey]
        }.first()
    }

    suspend fun saveUserRole(role: String) {
        context.dataStore.edit { preferences ->
            preferences[userRoleKey] = role
        }
    }

    fun getUserRole(): String? = runBlocking {
        context.dataStore.data.map { preferences ->
            preferences[userRoleKey]
        }.first()
    }

    suspend fun saveUserId(userId: Int) {
        context.dataStore.edit { preferences ->
            preferences[userIdKey] = userId.toString()
        }
    }

    fun getUserId(): Int? = runBlocking {
        context.dataStore.data.map { preferences ->
            preferences[userIdKey]?.toIntOrNull()
        }.first()
    }

    suspend fun saveUsername(username: String) {
        context.dataStore.edit { preferences ->
            preferences[usernameKey] = username
        }
    }

    fun getUsername(): String? = runBlocking {
        context.dataStore.data.map { preferences ->
            preferences[usernameKey]
        }.first()
    }

    suspend fun saveUserEmail(email: String?) {
        context.dataStore.edit { preferences ->
            if (email != null) {
                preferences[userEmailKey] = email
            } else {
                preferences.remove(userEmailKey)
            }
        }
    }

    fun getUserEmail(): String? = runBlocking {
        context.dataStore.data.map { preferences ->
            preferences[userEmailKey]
        }.first()
    }

    suspend fun saveUserNombre(nombre: String?) {
        context.dataStore.edit { preferences ->
            if (nombre != null) {
                preferences[userNombreKey] = nombre
            } else {
                preferences.remove(userNombreKey)
            }
        }
    }

    fun getUserNombre(): String? = runBlocking {
        context.dataStore.data.map { preferences ->
            preferences[userNombreKey]
        }.first()
    }

    suspend fun clearTokens() {
        context.dataStore.edit { preferences ->
            preferences.remove(accessTokenKey)
            preferences.remove(refreshTokenKey)
            preferences.remove(userRoleKey)
            preferences.remove(userIdKey)
            preferences.remove(usernameKey)
            preferences.remove(userEmailKey)
            preferences.remove(userNombreKey)
        }
    }

    fun isLoggedIn(): Boolean {
        return getAccessToken() != null
    }

    fun isAdmin(): Boolean {
        return getUserRole() == UserRoles.ADMIN
    }
}

