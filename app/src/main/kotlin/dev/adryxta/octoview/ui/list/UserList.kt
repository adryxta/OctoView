package dev.adryxta.octoview.ui.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.adryxta.octoview.data.model.User
import dev.adryxta.octoview.ui.common.LoadingItem

@Composable
fun UserList(
    uiState: ListViewModel.UiState,
    modifier: Modifier,
    users: List<User>,
    onClick: (user: User) -> Unit,
) {
    LazyVerticalGrid(
        modifier = modifier
            .padding(20.dp),
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalArrangement = Arrangement.SpaceEvenly,
    ) {
        items(users) { user ->
            UserCard(
                userAvatarUrl = user.avatarUrl,
                username = user.login,
                onClick = { onClick(user)}
            )
        }
        item {
            if(uiState.isLoading) {
                LoadingItem()
            }
        }
    }
}

/*
@Preview(showBackground = true)
@Composable
private fun UserListScreenPreview() {
    UserListScreen (
        users = listOf(
            User.Profile(
                login = "octocat",
                avatarUrl = "https://avatars.githubusercontent.com/u/1?v=4",
                id = 100,
                name = "The Octocat",
                email = "dfjngnf@gmail.com"
            ),
            User.Profile(
                login = "octocat",
                avatarUrl = "https://avatars.githubusercontent.com/u/1?v=4",
                id = 100,
                name = "The Octocat",
                email = "dfjngnf@gmail.com"
            ),
            User.Profile(
                login = "octocat",
                avatarUrl = "https://avatars.githubusercontent.com/u/1?v=4",
                id = 100,
                name = "The Octocat",
                email = "dfjngnf@gmail.com"
            ),
            User.Profile(
                login = "octocat",
                avatarUrl = "https://avatars.githubusercontent.com/u/1?v=4",
                id = 100,
                name = "The Octocat",
                email = "dfjngnf@gmail.com"
            ),
            User.Profile(
                login = "octocat",
                avatarUrl = "https://avatars.githubusercontent.com/u/1?v=4",
                id = 100,
                name = "The Octocat",
                email = "dfjngnf@gmail.com"
            ),
            User.Profile(
                login = "octocat",
                avatarUrl = "https://avatars.githubusercontent.com/u/1?v=4",
                id = 100,
                name = "The Octocat",
                email = "dfjngnf@gmail.com"
            )
        ),
        onProfileClick = {}
    )
}*/
