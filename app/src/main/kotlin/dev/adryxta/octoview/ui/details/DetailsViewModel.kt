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

    val profile = with(savedStateHandle) {
        User.Profile(
            id = savedStateHandle.require<Int>("id"),
            login = savedStateHandle.require<String>("login"),
            avatarUrl = savedStateHandle.require<String>("avatarUrl")
        )
    }

    private val _uiState = MutableStateFlow(UiState(profile = profile))
    val uiState: StateFlow<UiState>
        get() = _uiState.asStateFlow()

    init {
        userRepository.getUserDetails(profile.login).onEach { user ->
            Timber.d("User ${profile.login}: $user")
            when (user) {
                is User.Profile -> _uiState.update { it.copy(profile = user) }
                is User.Details -> _uiState.update { it.copy(details = user) }
            }
        }.launchIn(viewModelScope)
    }

    data class UiState(
        val isLoading: Boolean = false,
        val profile: User.Profile,
        val details: User.Details? = null,
        val error: String? = null
    )

    private fun <T> SavedStateHandle.require(key: String): T {
        return get<T>(key) ?: throw IllegalStateException("No value found for key: $key")
    }
}