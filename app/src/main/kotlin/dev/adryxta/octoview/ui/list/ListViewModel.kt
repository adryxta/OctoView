package dev.adryxta.octoview.ui.list

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.adryxta.octoview.data.UserRepository
import dev.adryxta.octoview.data.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface ListUiState {
    data class Success(
        val isLoading: Boolean,
        val users: List<User.Profile>,
    ) : ListUiState
    data class Error(val message: String) : ListUiState
}

internal data class ListViewModelState(
    val isLoading: Boolean = false,
    val users: List<User.Profile> = emptyList(),
    val error: String? = null,
    val lastId: Int? = null,
) {
    fun toUiState(): ListUiState = when {
        error != null -> ListUiState.Error(error)
        else -> ListUiState.Success(isLoading, users)
    }
}

class ListViewModel @Inject internal constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    private val viewModelState = MutableStateFlow(ListViewModelState())
    val uiState = viewModelState
        .map { it.toUiState() }
        .stateIn(
            scope = viewModelScope,
            started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5_000),
            initialValue = viewModelState.value.toUiState()
        )

    init {
        loadUsers()
    }

    fun loadUsers() {
        if (viewModelState.value.isLoading) return
        viewModelState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            runCatching {
                userRepository.getUserList(viewModelState.value.lastId)
            }.onFailure {
                viewModelState.update { it.copy(isLoading = false, error = it.error) }
            }.onSuccess { result ->
                viewModelState.update {
                    it.copy(
                        isLoading = false,
                        users = it.users.toMutableList().apply { addAll(result.getOrThrow()) },
                        lastId = result.getOrThrow().size.plus(it.lastId?: 0)
                    ) }
            }
        }
    }
}