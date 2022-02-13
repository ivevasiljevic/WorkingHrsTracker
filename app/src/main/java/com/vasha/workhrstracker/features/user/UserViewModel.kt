package com.vasha.workhrstracker.features.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vasha.workhrstracker.R
import com.vasha.workhrstracker.data.*
import com.vasha.workhrstracker.util.Resource
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * Created by ivasil on 1/26/2022
 */

class UserViewModel(
    private val userRepository: UserRepository,
    private val preferencesManager: PreferencesManager) : ViewModel() {

    val userUiState = MutableSharedFlow<UserState>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )
    val userNavigationState = MutableStateFlow<UserState.UserNavigation>(UserState.UserNavigation.Empty)
    private val userPreferences = preferencesManager.userPreferencesFlow
    private lateinit var user: UserPreferences
    var fullName: String = ""

    init {
        viewModelScope.launch {
            userPreferences.collect {
                user = it
                if(it.pin != null || it.qrCode != null) {
                    userNavigationState.value = UserState.UserNavigation.NavigateToLoginScreen
                    fullName = user.username.toString()
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
                    userUiState.tryEmit(UserState.UserFailed(it.message.toString()))
                }
                is Resource.Loading -> {
                    userUiState.tryEmit(UserState.Loading)
                }
                is Resource.Success -> {
                    if (it.data != null) {
                        preferencesManager.updateUser(it.data.data.username, user.password, it.data.data.qrCode, employeeAvatars.random(), employeePositions.random())
                        userUiState.tryEmit(UserState.UserSuccessful)
                    }
                }
            }
        }
    }

    fun getUser() = viewModelScope.launch {
        val user = userPreferences.first()
        userUiState.tryEmit(UserState.ShowUserData(
            user.username.toString(),
            user.qrCode.toString(),
            user.avatar ?: R.drawable.avatar1,
            user.position.toString()
        ))
    }

    fun loginUser(pin: String) {
        if(user.pin == pin) {
            userUiState.tryEmit(UserState.UserSuccessful)
        }
        else {
            userUiState.tryEmit(UserState.UserFailed(""))
        }
    }

    sealed class UserState {
        object UserSuccessful : UserState()
        object Empty : UserState()
        data class UserFailed(val error: String) : UserState()
        object Loading : UserState()
        data class ShowUserData(val username: String, val qrCode: String, val avatar: Int, val position: String) : UserState()

        sealed class UserNavigation {
            object Empty : UserNavigation()
            object NavigateToRegisterScreen : UserNavigation()
            object NavigateToLoginScreen : UserNavigation()
        }
    }
}