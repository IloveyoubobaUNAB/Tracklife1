package com.jpalomino502.vivebien.feature.profile.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jpalomino502.vivebien.core.domain.model.User
import com.jpalomino502.vivebien.feature.profile.domain.usecase.GetSettingsUseCase
import com.jpalomino502.vivebien.feature.profile.domain.usecase.GetUserProfileUseCase
import com.jpalomino502.vivebien.feature.profile.domain.usecase.LogoutUseCase
import com.jpalomino502.vivebien.feature.profile.domain.usecase.SaveSettingsUseCase
import com.jpalomino502.vivebien.feature.profile.domain.usecase.UpdateProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileUiState(
    val user: User = User(id = "", name = ""),
    val notificationsEnabled: Boolean = true,
    val darkModeEnabled: Boolean = false,
    val isLoading: Boolean = true,
    val isEditMode: Boolean = false,
    val editName: String = "",
    val editEmail: String = "",
    val editPhone: String = "",
    val editBirthDate: String = "",
    val loggedOut: Boolean = false,
    val errorMessage: String = ""
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getUserProfile: GetUserProfileUseCase,
    private val getSettings: GetSettingsUseCase,
    private val updateProfile: UpdateProfileUseCase,
    private val saveSettings: SaveSettingsUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                getUserProfile(),
                getSettings.notificationsEnabled(),
                getSettings.darkModeEnabled()
            ) { user, notifications, darkMode -> Triple(user, notifications, darkMode) }
            .collect { (user, notifications, darkMode) ->
                _uiState.value = _uiState.value.copy(
                    user = user,
                    notificationsEnabled = notifications,
                    darkModeEnabled = darkMode,
                    isLoading = false
                )
            }
        }
    }

    fun onNotificationsToggled(enabled: Boolean) {
        viewModelScope.launch { saveSettings.saveNotifications(enabled) }
    }

    fun onDarkModeToggled(enabled: Boolean) {
        viewModelScope.launch { saveSettings.saveDarkMode(enabled) }
    }

    fun onStartEdit() {
        val user = _uiState.value.user
        _uiState.value = _uiState.value.copy(
            isEditMode = true,
            editName = user.name, editEmail = user.email,
            editPhone = user.phone, editBirthDate = user.birthDate
        )
    }

    fun onCancelEdit() { _uiState.value = _uiState.value.copy(isEditMode = false, errorMessage = "") }
    fun onEditNameChanged(v: String) { _uiState.value = _uiState.value.copy(editName = v) }
    fun onEditEmailChanged(v: String) { _uiState.value = _uiState.value.copy(editEmail = v) }
    fun onEditPhoneChanged(v: String) { _uiState.value = _uiState.value.copy(editPhone = v) }
    fun onEditBirthDateChanged(v: String) { _uiState.value = _uiState.value.copy(editBirthDate = v) }

    fun onSaveProfile() {
        viewModelScope.launch {
            val s = _uiState.value
            val result = updateProfile(s.user.copy(name = s.editName, email = s.editEmail, phone = s.editPhone, birthDate = s.editBirthDate))
            result.fold(
                onSuccess = { _uiState.value = _uiState.value.copy(isEditMode = false, errorMessage = "") },
                onFailure = { _uiState.value = _uiState.value.copy(errorMessage = it.message ?: "") }
            )
        }
    }

    fun onLogout() {
        viewModelScope.launch {
            logoutUseCase()
            _uiState.value = _uiState.value.copy(loggedOut = true)
        }
    }
}
