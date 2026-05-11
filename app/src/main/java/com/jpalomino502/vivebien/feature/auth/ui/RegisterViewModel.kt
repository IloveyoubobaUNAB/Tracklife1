package com.jpalomino502.vivebien.feature.auth.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jpalomino502.vivebien.feature.auth.domain.usecase.RegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RegisterUiState(
    val displayName: String = "",
    val username: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String = "",
    val registerSuccess: Boolean = false
)

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    fun onDisplayNameChanged(value: String) { _uiState.value = _uiState.value.copy(displayName = value, errorMessage = "") }
    fun onUsernameChanged(value: String) { _uiState.value = _uiState.value.copy(username = value, errorMessage = "") }
    fun onPasswordChanged(value: String) { _uiState.value = _uiState.value.copy(password = value, errorMessage = "") }
    fun onConfirmPasswordChanged(value: String) { _uiState.value = _uiState.value.copy(confirmPassword = value, errorMessage = "") }

    fun onRegisterClicked() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = "")
            val s = _uiState.value
            val result = registerUseCase(s.displayName, s.username, s.password, s.confirmPassword)
            result.fold(
                onSuccess = { _uiState.value = _uiState.value.copy(isLoading = false, registerSuccess = true) },
                onFailure = { _uiState.value = _uiState.value.copy(isLoading = false, errorMessage = it.message ?: "Error desconocido") }
            )
        }
    }
}
