package dev.adryxta.octoview.ui.list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.adryxta.octoview.data.UserRepository
import dev.adryxta.octoview.data.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState>
        get() = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val result = userRepository.getUserList()
            if (result.isSuccess) {
                _uiState.value = UiState(users = result.getOrThrow())
            } else {
                _uiState.value = UiState(error = result.exceptionOrNull()?.message)
            }
        }
    }

    data class UiState(
        val isLoading: Boolean = false,
        val users: List<User.Profile> = emptyList(),
        val error: String? = null
    )
}