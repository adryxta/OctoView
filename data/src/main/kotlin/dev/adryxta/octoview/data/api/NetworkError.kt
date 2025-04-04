package dev.adryxta.octoview.data.api

import okhttp3.Interceptor as OkHttpInterceptor
import okhttp3.Response
import retrofit2.HttpException

sealed class NetworkError: Throwable() {

    class Connection(override val message: String) : NetworkError()

    class Timeout(override val message: String) : NetworkError()

    class RateLimit(override val message: String) : NetworkError()

    class HttpError(val code: Int, override val message: String) : NetworkError()

    class UnknownError(override val message: String) : NetworkError()

    internal object Interceptor: OkHttpInterceptor {
        override fun intercept(chain: OkHttpInterceptor.Chain): Response {
            try {
                return chain.proceed(chain.request())
            } catch (httpException: HttpException) {
                throw when(httpException.code()) {
                    403, 429 -> RateLimit("Rate limit exceeded")
                    in 400..599 -> HttpError(httpException.code(), httpException.message())
                    else -> UnknownError("Unknown Response: ${httpException.message}")
                }
            } catch (ioException: Exception) {
                throw when (ioException) {
                    is java.net.SocketTimeoutException -> Timeout("Timeout Error: ${ioException.message}")
                    else -> Connection("Connection Error: ${ioException.message}")
                }
            } catch (throwable: Throwable) {
                throw UnknownError("Unknown Error: ${throwable.message}")
            }
        }
    }
}