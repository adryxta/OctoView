package dev.adryxta.octoview.ui.details

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.adryxta.octoview.data.UserRepository
import dev.adryxta.octoview.data.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

sealed interface DetailUiState {
    data class Success(
        val isLoading: Boolean,
        val profile: User.Profile,
        val details: User.Details?,
    ) : DetailUiState

    data class Error(
        val error: Throwable?
    ) : DetailUiState
}

internal data class DetailsViewModelState(
    val isLoading: Boolean = false,
    val profile: User.Profile,
    val details: User.Details? = null,
    val error: Throwable? = null,
) {
    fun toUiState(): DetailUiState = when {
        error != null -> DetailUiState.Error(error)
        else -> DetailUiState.Success(isLoading, profile, details)
    }
}

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

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    private val viewModelState = MutableStateFlow(DetailsViewModelState(profile = profile))
    val uiState = viewModelState
        .map { it.toUiState() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = viewModelState.value.toUiState()
        )

//    private val _uiState = MutableStateFlow(UiState(profile = profile))
//    val uiState: StateFlow<UiState>
//        get() = _uiState.asStateFlow()

    init {
//        userRepository.getUserDetails(profile.login).onEach { user ->
//            Timber.d("User ${profile.login}: $user")
//            when (user) {
//                is User.Profile -> _uiState.update { it.copy(profile = user) }
//                is User.Details -> _uiState.update { it.copy(details = user) }
//            }
//        }.launchIn(viewModelScope)
        fetchDetails()
    }

    private fun fetchDetails() {
        if (viewModelState.value.isLoading) return
        viewModelState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            runCatching {
                userRepository.getUserDetails(viewModelState.value.profile.login)
            }.onFailure { e ->
                viewModelState.update { it.copy(isLoading = false, error = e) }
            }.onSuccess { result ->
                result.collectLatest { user ->
                    Timber.d("User ${viewModelState.value.profile.login}: $user")
                    when (user) {
                        is User.Profile -> viewModelState.update { it.copy(profile = user) }
                        is User.Details -> viewModelState.update { it.copy(details = user) }
                    }
                }
            }
        }
    }

    private fun <T> SavedStateHandle.require(key: String): T {
        return get<T>(key) ?: throw IllegalStateException("No value found for key: $key")
    }

    class Factory @Inject constructor(
        private val savedStateHandle: SavedStateHandle,
        private val userRepository: UserRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(DetailsViewModel::class.java)) {
                return DetailsViewModel(savedStateHandle, userRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}