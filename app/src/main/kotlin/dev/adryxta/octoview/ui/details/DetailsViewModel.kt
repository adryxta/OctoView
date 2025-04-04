package dev.adryxta.octoview.ui.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.adryxta.octoview.data.UserRepository
import dev.adryxta.octoview.data.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState>
        get() = _uiState.asStateFlow()

    init {
        val login = savedStateHandle.get<String>("login")
        if (login == null) {
            Timber.e("Login is null")
            // TODO Show error
        } else {
            userRepository.getUserDetails(login).onEach { user ->
                Timber.d("User $login: $user")
                _uiState.update { it.copy(user = user) }
            }.launchIn(viewModelScope)
//            _uiState.combine(userRepository.getUserDetails(login)) { uiState, user ->
//                uiState.copy(user = user)
//            }
//            viewModelScope.launch {
//                try {
//                    userRepository.getUserDetails(login).collect {
//                        _uiState.update {
//                            it.copy(user = it.user)
//                        }
//                    }
//                } catch (throwable: Throwable) {
//                    _uiState.update {
//                        it.copy(error = throwable.message)
//                    }
//                }
//            }
        }
    }

    data class UiState(
        val isLoading: Boolean = false,
        val user: User? = null,
        val error: String? = null
    )
}