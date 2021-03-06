package com.vasha.workhrstracker.data

import android.content.Context
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

/**
 * Created by ivasil on 1/26/2022
 */

val Context.userDataStore: DataStore<Preferences> by preferencesDataStore("user_preferences")
data class UserPreferences(val username: String? = null, val pin: String? = null, val qrCode: String? = null, val avatar: Int? = null, val position: String? = null)

class PreferencesManager(private val context: Context) {

    val userPreferencesFlow = context.userDataStore.data
        .catch { exception ->
            if (exception is IOException) {
                Toast.makeText(context, "Error while accessing data (Login)", Toast.LENGTH_SHORT)
                    .show()
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val username = preferences[UserPreferencesKey.USERNAME]
            val pin = preferences[UserPreferencesKey.USER_PIN]
            val qrcode = preferences[UserPreferencesKey.USER_QRCODE]
            val avatar = preferences[UserPreferencesKey.USER_AVATAR]
            val position = preferences[UserPreferencesKey.USER_POSITION]
            UserPreferences(username, pin, qrcode, avatar, position)
        }

    suspend fun updateUser(username: String, pin: String, qrCode: String, avatar: Int, position: String) = context.userDataStore.edit { preferences ->
        preferences[UserPreferencesKey.USERNAME] = username
        preferences[UserPreferencesKey.USER_PIN] = pin
        preferences[UserPreferencesKey.USER_QRCODE] = qrCode
        preferences[UserPreferencesKey.USER_AVATAR] = avatar
        preferences[UserPreferencesKey.USER_POSITION] = position
    }

    private object UserPreferencesKey {
        val USERNAME = stringPreferencesKey("username")
        val USER_PIN = stringPreferencesKey("user_pin")
        val USER_QRCODE = stringPreferencesKey("user_qrcode")
        val USER_AVATAR = intPreferencesKey("user_avatar")
        val USER_POSITION = stringPreferencesKey("user_position")
    }
}