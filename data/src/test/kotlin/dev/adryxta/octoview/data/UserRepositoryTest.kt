package dev.adryxta.octoview.data

import app.cash.turbine.test
import dev.adryxta.octoview.data.api.GitHubUserApi
import dev.adryxta.octoview.data.api.NetworkError
import dev.adryxta.octoview.data.model.User
import kotlinx.coroutines.test.runTest
import org.junit.After
import com.google.common.truth.Truth.assertThat
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.SocketPolicy
import org.junit.Before
import org.junit.Test
import java.net.HttpURLConnection
import java.time.LocalDateTime

class UserRepositoryTest {

    private val mockWebServer = MockWebServer()

    private lateinit var gitHubUserApi: GitHubUserApi

    private lateinit var userRepository: UserRepository

    @Before
    fun setup() {
        gitHubUserApi = GitHubUserApi.create(
            baseUrl = mockWebServer.url("/").toString(),
            authToken = "",
        )
        userRepository = DefaultUserRepository(gitHubUserApi)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `getUserList returns list of user profiles`() = runTest {
        // setup
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(GitHubUserArrayA)
        mockWebServer.enqueue(response)

        // execute
        val userList = userRepository.getUserList()

        // verify
        val userProfile = userList.first()
        assertThat(userProfile.id).isEqualTo(1)
        assertThat(userProfile.login).isEqualTo("octocat")
        assertThat(userProfile.avatarUrl).isEqualTo("https://github.com/images/error/octocat_happy.gif")
    }

    @Test
    fun `getUserList with previousUserId passes parameter correctly`() = runTest {
        // setup
        val previousUserId = 42
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(GithubUserArrayB)
        mockWebServer.enqueue(response)

        // execute
        val result = userRepository.getUserList(previousUserId)

        // verify
        assertThat(result.first().id).isEqualTo(43)
    }

    @Test
    fun `getUserDetails returns user profile first, then details`() = runTest {
        val userLogin = "octocat"
        // setup
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(GitHubUserArrayA)
        )
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(GitHubUserDetailsA)
        )

        // execute
        userRepository.getUserList()
        val userFlow = userRepository.getUserDetails(userLogin)

        // verify
        userFlow.test {
            // First Profile is emitted
            val profile = awaitItem()
            assert(profile is User.Profile)
            with(profile as User.Profile) {
                // Verify non null properties
                assertThat(id).isEqualTo(1)
                assertThat(login).isEqualTo("octocat")
                assertThat(avatarUrl).isEqualTo("https://github.com/images/error/octocat_happy.gif")
            }
            // Then, Details
            val details = awaitItem()
            assert(details is User.Details)
            with(details as User.Details) {
                assertThat(name).isEqualTo("monalisa octocat")
                assertThat(company).isEqualTo("GitHub")
                assertThat(blog).isEqualTo("https://github.com/blog")
                assertThat(location).isEqualTo("San Francisco")
                assertThat(email).isEqualTo("octocat@github.com")
                assertThat(hireable).isEqualTo(false)
                assertThat(bio).isEqualTo("There once was a octocat.")
                assertThat(twitterUsername).isEqualTo("monatheoctocat")
                assertThat(publicRepos).isEqualTo(2)
                assertThat(publicGists).isEqualTo(1)
                assertThat(followers).isEqualTo(20)
                assertThat(following).isEqualTo(0)
                assertThat(createdAt).isEqualTo(LocalDateTime.of(2008, 1, 12, 5, 35, 35))
                assertThat(updatedAt).isEqualTo(LocalDateTime.of(2008, 1, 14, 4, 33, 35))
            }
            // Complete
            awaitComplete()
        }

    }

    @Test
    fun `getUserDetails returns user details if profile is not cached`() = runTest {
        val userLogin = "octocat"
        // setup
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(GitHubUserDetailsA)
        )

        // execute
        val userFlow = userRepository.getUserDetails(userLogin)

        // verify
        userFlow.test {
            // With no cache, Details is emitted directly
            val details = awaitItem()
            assert(details is User.Details)
            with(details as User.Details) {
                assertThat(id).isEqualTo(1)
                assertThat(login).isEqualTo("octocat")
                assertThat(avatarUrl).isEqualTo("https://github.com/images/error/octocat_happy.gif")
                assertThat(name).isEqualTo("monalisa octocat")
                assertThat(company).isEqualTo("GitHub")
                assertThat(blog).isEqualTo("https://github.com/blog")
                assertThat(location).isEqualTo("San Francisco")
                assertThat(email).isEqualTo("octocat@github.com")
                assertThat(hireable).isEqualTo(false)
                assertThat(bio).isEqualTo("There once was a octocat.")
                assertThat(twitterUsername).isEqualTo("monatheoctocat")
                assertThat(publicRepos).isEqualTo(2)
                assertThat(publicGists).isEqualTo(1)
                assertThat(followers).isEqualTo(20)
                assertThat(following).isEqualTo(0)
                assertThat(createdAt).isEqualTo(LocalDateTime.of(2008, 1, 12, 5, 35, 35))
                assertThat(updatedAt).isEqualTo(LocalDateTime.of(2008, 1, 14, 4, 33, 35))
            }
            // Complete
            awaitComplete()
        }
    }

    @Test
    fun `timeout errors are propagated to calling code`() = runTest {
        // setup
        mockWebServer.enqueue(MockResponse().setSocketPolicy(SocketPolicy.NO_RESPONSE))

        // execute
        try {
            userRepository.getUserList()
        } catch (throwable: Throwable) {
            // verify
            assert(throwable is NetworkError.Timeout)
        }
    }

    @Test
    fun `http error codes are propagated to calling code`() = runTest {
        // setup
        mockWebServer.enqueue(MockResponse().setResponseCode(HttpURLConnection.HTTP_NOT_FOUND))

        // execute
        try {
            userRepository.getUserList()
        } catch (throwable: Throwable) {
            // verify
            assert(throwable is NetworkError.Http)
            assertThat((throwable as NetworkError.Http).code).isEqualTo(HttpURLConnection.HTTP_NOT_FOUND)
        }
    }

    companion object {

        private val GitHubUserArrayA = """[
            {
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
              }
        ]""".trimIndent()

        private val GithubUserArrayB = """[
            {
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
        ]""".trimIndent()

        private val GitHubUserDetailsA = """
            {
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
                  }
        """.trimIndent()
    }
}