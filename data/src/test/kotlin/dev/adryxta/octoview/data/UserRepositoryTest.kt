package dev.adryxta.octoview.data

import app.cash.turbine.test
import dev.adryxta.octoview.data.api.GitHubUserApi
import dev.adryxta.octoview.data.api.NetworkError
import dev.adryxta.octoview.data.model.User
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime

class UserRepositoryTest {

    // Mocks
    private val gitHubUserApi = mockk<GitHubUserApi>()

    private lateinit var userRepository: UserRepository

    @Before
    fun setup() {
        userRepository = DefaultUserRepository(gitHubUserApi)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun `getUserList returns list of user profiles`() = runTest {
        // Arrange
        coEvery { gitHubUserApi.getUsers(any<Int>(), any<Int>()) } returns listOf(GitHubUser1)

        // Act
        val result = userRepository.getUserList()

        // Assert
        assertTrue(result.isSuccess)
        val (userProfile) = requireNotNull(result.getOrNull())
        assertEquals(1, userProfile.id)
        assertEquals(null, userProfile.name)
        assertEquals("octocat", userProfile.login)
        assertEquals(null, userProfile.email)
        assertEquals("https://github.com/images/error/octocat_happy.gif", userProfile.avatarUrl)
        coVerify {
            gitHubUserApi.getUsers(null)
        }
    }

    @Test
    fun `getUserList with previousUserId passes parameter correctly`() = runTest {
        // Arrange
        val previousUserId = 42
        coEvery { gitHubUserApi.getUsers(previousUserId) } returns listOf(GitHubUser2)

        // Act
        val result = userRepository.getUserList(previousUserId)

        // Assert
        assertTrue(result.isSuccess)
        coVerify { gitHubUserApi.getUsers(previousUserId) }
    }

    @Test
    fun `getUserDetails returns user profile first, then details`() = runTest {
        val userLogin = "octocat"
        // Arrange
        coEvery { gitHubUserApi.getUsers(any<Int>(), any<Int>()) } returns listOf(GitHubUser1)
        coEvery { gitHubUserApi.getUserDetail(userLogin) } returns GitHubUser1Details

        // Act
        userRepository.getUserList()
        val userFlow = userRepository.getUserDetails(userLogin)

        // Assert
        userFlow.test<User> {
            // First Profile is emitted
            val profile = awaitItem()
            assertTrue(profile is User.Profile)
            with(profile as User.Profile) {
                // Verify non null properties
                assertEquals(1, id)
                assertEquals("octocat", login)
                assertEquals("https://github.com/images/error/octocat_happy.gif", avatarUrl)
            }
            // Then, Details
            val details = awaitItem()
            assert(details is User.Details)
            with(details as User.Details) {
                assertEquals("monalisa octocat", details.name)
                assertEquals("GitHub", company)
                assertEquals("https://github.com/blog", details.blog)
                assertEquals("San Francisco", details.location)
                assertEquals("octocat@github.com", details.email)
                assertEquals(false, details.hireable)
                assertEquals("There once was a octocat.", details.bio)
                assertEquals("monatheoctocat", details.twitterUsername)
                assertEquals(2, details.publicRepos)
                assertEquals(1, details.publicGists)
                assertEquals(20, details.followers)
                assertEquals(0, details.following)
                assertEquals(LocalDateTime.of(2008, 1, 12, 5, 35, 35), details.createdAt)
                assertEquals(LocalDateTime.of(2008, 1, 14, 4, 33, 35), details.updatedAt)
            }
            // Complete
            awaitComplete()
        }

    }

    @Test
    fun `getUserDetails returns user details if profile is not cached`() = runTest {
        val userLogin = "octocat"
        // Arrange
        coEvery { gitHubUserApi.getUserDetail(userLogin) } returns GitHubUser1Details

        // Act
        val userFlow = userRepository.getUserDetails(userLogin)

        // Assert
        userFlow.test<User> {
            // With no cache, Details is emitted directly
            val details = awaitItem()
            assert(details is User.Details)
            with(details as User.Details) {
                assertEquals(1, id)
                assertEquals("octocat", login)
                assertEquals("https://github.com/images/error/octocat_happy.gif", avatarUrl)
                assertEquals("monalisa octocat", details.name)
                assertEquals("GitHub", company)
                assertEquals("https://github.com/blog", details.blog)
                assertEquals("San Francisco", details.location)
                assertEquals("octocat@github.com", details.email)
                assertEquals(false, details.hireable)
                assertEquals("There once was a octocat.", details.bio)
                assertEquals("monatheoctocat", details.twitterUsername)
                assertEquals(2, details.publicRepos)
                assertEquals(1, details.publicGists)
                assertEquals(20, details.followers)
                assertEquals(0, details.following)
                assertEquals(LocalDateTime.of(2008, 1, 12, 5, 35, 35), details.createdAt)
                assertEquals(LocalDateTime.of(2008, 1, 14, 4, 33, 35), details.updatedAt)
            }
            // Complete
            awaitComplete()
        }
    }

    @Test
    fun `network errors are propagated to calling code`() = runTest {
        // Arrange
        coEvery {
            gitHubUserApi.getUsers(
                any<Int>(),
                any<Int>()
            )
        } throws NetworkError.HttpError(404, "Not Found")

        // Act
        val result = userRepository.getUserList()

        // Assert
        assertTrue(result.isFailure)
        assertEquals(404, (result.exceptionOrNull() as NetworkError.HttpError).code)
        assertEquals("Not Found", result.exceptionOrNull()?.message)
        coVerify { gitHubUserApi.getUsers(null) }
    }

    companion object {

        private val GitHubUser1 = GitHubUserApi.ParseJson.decodeFromString<GitHubUserApi.User>(
            """{
                "login": "octocat",
                "id": 1,
                "node_id": "MDQ6VXNlcjE=",
                "avatar_url": "https://github.com/images/error/octocat_happy.gif",
                "gravatar_id": "",
                "url": "https://api.github.com/users/octocat",
                "html_url": "https://github.com/octocat",
                "followers_url": "https://api.github.com/users/octocat/followers",
                "following_url": "https://api.github.com/users/octocat/following{/other_user}",
                "gists_url": "https://api.github.com/users/octocat/gists{/gist_id}",
                "starred_url": "https://api.github.com/users/octocat/starred{/owner}{/repo}",
                "subscriptions_url": "https://api.github.com/users/octocat/subscriptions",
                "organizations_url": "https://api.github.com/users/octocat/orgs",
                "repos_url": "https://api.github.com/users/octocat/repos",
                "events_url": "https://api.github.com/users/octocat/events{/privacy}",
                "received_events_url": "https://api.github.com/users/octocat/received_events",
                "type": "User",
                "site_admin": false
              }""".trimIndent()
        )

        private val GitHubUser1Details =
            GitHubUserApi.ParseJson.decodeFromString<GitHubUserApi.UserDetails>(
                """{
                  "login": "octocat",
                  "id": 1,
                  "node_id": "MDQ6VXNlcjE=",
                  "avatar_url": "https://github.com/images/error/octocat_happy.gif",
                  "gravatar_id": "",
                  "url": "https://api.github.com/users/octocat",
                  "html_url": "https://github.com/octocat",
                  "followers_url": "https://api.github.com/users/octocat/followers",
                  "following_url": "https://api.github.com/users/octocat/following{/other_user}",
                  "gists_url": "https://api.github.com/users/octocat/gists{/gist_id}",
                  "starred_url": "https://api.github.com/users/octocat/starred{/owner}{/repo}",
                  "subscriptions_url": "https://api.github.com/users/octocat/subscriptions",
                  "organizations_url": "https://api.github.com/users/octocat/orgs",
                  "repos_url": "https://api.github.com/users/octocat/repos",
                  "events_url": "https://api.github.com/users/octocat/events{/privacy}",
                  "received_events_url": "https://api.github.com/users/octocat/received_events",
                  "type": "User",
                  "site_admin": false,
                  "name": "monalisa octocat",
                  "company": "GitHub",
                  "blog": "https://github.com/blog",
                  "location": "San Francisco",
                  "email": "octocat@github.com",
                  "hireable": false,
                  "bio": "There once was a octocat.",
                  "twitter_username": "monatheoctocat",
                  "public_repos": 2,
                  "public_gists": 1,
                  "followers": 20,
                  "following": 0,
                  "created_at": "2008-01-12T05:35:35Z",
                  "updated_at": "2008-01-14T04:33:35Z"
                  }""".trimIndent()
            )

        private val GitHubUser2 = GitHubUserApi.ParseJson.decodeFromString<GitHubUserApi.User>(
            """{
                "login": "github",
                "name": "GitHub",
                "id": 43,
                "node_id": "MDJDVXNlcjE=",
                "avatar_url": "https://github.com/images/error/github_happy.gif",
                "url": "https://api.github.com/users/github",
                "html_url": "https://github.com/github",
                "followers_url": "https://api.github.com/users/github/followers",
                "following_url": "https://api.github.com/users/github/following{/other_user}",
                "gists_url": "https://api.github.com/users/github/gists{/gist_id}",
                "starred_url": "https://api.github.com/users/github/starred{/owner}{/repo}",
                "subscriptions_url": "https://api.github.com/users/github/subscriptions",
                "organizations_url": "https://api.github.com/users/github/orgs",
                "repos_url": "https://api.github.com/users/github/repos",
                "events_url": "https://api.github.com/users/github/events{/privacy}",
                "received_events_url": "https://api.github.com/users/github/received_events",
                "type": "User",
                "site_admin": false
                }
        """.trimIndent()
        )
    }
}