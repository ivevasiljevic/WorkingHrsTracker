package com.vasha.workhrstracker.features.user

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.zxing.qrcode.encoder.QRCode
import com.vasha.workhrstracker.data.*
import com.vasha.workhrstracker.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.nio.file.attribute.UserDefinedFileAttributeView

/**
 * Created by ivasil on 1/26/2022
 */

class UserViewModel(
    private val userRepository: UserRepository,
    private val preferencesManager: PreferencesManager) : ViewModel() {

    val userUiState = MutableStateFlow<UserState>(UserState.Empty)
    val userNavigationState = MutableStateFlow<UserState.UserNavigation>(UserState.UserNavigation.Empty)
    private val userPreferences = preferencesManager.userPreferencesFlow
    private lateinit var user: UserPreferences

    init {
        viewModelScope.launch {
            userPreferences.collect {
                user = it
                if(it.pin != null || it.qrCode != null) {
                    userNavigationState.value = UserState.UserNavigation.NavigateToLoginScreen
                }
                else {
                    userNavigationState.value = UserState.UserNavigation.NavigateToRegisterScreen
                }
            }
        }
    }

    fun registerUser(user: User) = viewModelScope.launch {
        userRepository.registerUser(user).collect {
            when(it) {
                is Resource.Error -> {
                    userUiState.value = UserState.UserFailed(it.message.toString())
                }
                is Resource.Loading -> {
                    userUiState.value = UserState.Loading
                }
                is Resource.Success -> {
                    if (it.data != null) {
                        preferencesManager.updateUser(it.data.data.username, user.password, it.data.data.qrCode)
                        userUiState.value = UserState.UserSuccessful
                    }
                }
            }
        }
    }

    fun getUser() = viewModelScope.launch {
        val user = userPreferences.first()
        userUiState.value = UserState.ShowUserData(user.username.toString(), user.qrCode.toString())
    }

    fun getLogs() = viewModelScope.launch {
        userRepository.getLogs().collect {
            when(it) {
                is Resource.Error -> {
                    userUiState.value = UserState.UserFailed(it.message.toString())
                }
                is Resource.Loading -> {
                    userUiState.value = UserState.Loading
                }
                is Resource.Success -> {
                    if (it.data != null) {
                        userUiState.value = UserState.LogsSuccessful(it.data)
                    }
                }
            }
        }
    }

    fun comparePins(pin: String): Boolean {
        return user.pin == pin
    }

    sealed class UserState {
        object UserSuccessful : UserState()
        data class LogsSuccessful(val list: List<Employee>) : UserState()
        object Empty : UserState()
        data class UserFailed(val error: String) : UserState()
        object Loading : UserState()
        data class ShowUserData(val username: String, val qrCode: String) : UserState()

        sealed class UserNavigation {
            object Empty : UserNavigation()
            object NavigateToRegisterScreen : UserNavigation()
            object NavigateToLoginScreen : UserNavigation()
        }
    }
}