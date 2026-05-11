package com.jpalomino502.vivebien.feature.profile.ui

import androidx.lifecycle.ViewModel
import com.jpalomino502.vivebien.core.domain.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

data class ProfileUiState(
    val user: User = User(
        id = "VB-78542",
        name = "María García",
        email = "maria.garcia@ejemplo.com",
        phone = "+34 612 345 678",
        birthDate = "15/05/1957"
    ),
    val notificationsEnabled: Boolean = true,
    val darkModeEnabled: Boolean = false,
    val isLoading: Boolean = false
)

@HiltViewModel
class ProfileViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    fun onNotificationsToggled(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(notificationsEnabled = enabled)
    }

    fun onDarkModeToggled(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(darkModeEnabled = enabled)
    }
}
