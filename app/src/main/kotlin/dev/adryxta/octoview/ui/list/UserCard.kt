package dev.adryxta.octoview.ui.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import dev.adryxta.octoview.R

@Composable
fun UserCard(
    userAvatarUrl: String,
    username: String,
    onClick: () -> Unit,
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .padding(10.dp)
            .wrapContentSize(align = Alignment.Center)
            .clickable{
                onClick.invoke()
            },
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 10.dp,
            pressedElevation = 5.dp,
            hoveredElevation = 15.dp,
        )

    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .padding(10.dp)
                .size(150.dp)
        ) {
            AsyncImage(
                model = userAvatarUrl,
                contentDescription = null,
                placeholder = painterResource(R.drawable.ic_avatar_placeholder),
                modifier = Modifier
                    .padding(bottom = 5.dp)
                    .size(100.dp)
                    .clip(CircleShape),
                alignment = Alignment.Center
            )
            Text(
                text = "@$username",
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun UserCardPreview() {
    UserCard(
        userAvatarUrl = "https://avatars.githubusercontent.com/u/1?v=4",
        username = "octocat",
        onClick = {}
    )
}