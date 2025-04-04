package dev.adryxta.octoview.data.api

import okhttp3.Response
import java.io.IOException
import okhttp3.Interceptor as OkHttpInterceptor

sealed class NetworkError : IOException() {

    class Connection(override val message: String) : NetworkError()

    class Timeout(override val message: String) : NetworkError()

    class RateLimit(override val message: String) : NetworkError()

    class Http(val code: Int, override val message: String) : NetworkError()

    class Unknown(override val message: String) : NetworkError()

    internal object Interceptor : OkHttpInterceptor {
        override fun intercept(chain: OkHttpInterceptor.Chain): Response {
            val response = try {
                chain.proceed(chain.request())
            } catch (ioException: Exception) {
                throw when (ioException) {
                    is java.net.SocketTimeoutException -> Timeout("Timeout Error: ${ioException.message}")
                    else -> Connection("Connection Error: ${ioException.message}")
                }
            } catch (throwable: Throwable) {
                throw Unknown("Unknown Error: ${throwable.message}")
            }

            if (response.isSuccessful) {
                return response
            }

            throw when (response.code) {
                403, 429 -> RateLimit("Rate limit exceeded")
                in 400..599 -> Http(response.code, response.message)
                else -> Unknown("Unknown Response: ${response.message}")
            }
        }
    }
}