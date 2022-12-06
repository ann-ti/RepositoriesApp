package com.annti.repositoriesapp.data.network

import com.annti.repositoriesapp.domain.AuthUseCase
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import retrofit2.Invocation
import java.io.IOException

@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)
@Retention(AnnotationRetention.RUNTIME)
annotation class AuthRequired

private fun findAuthAnnotation(
    request: Request
): AuthRequired? {
    return request.tag(Invocation::class.java)
        ?.method()
        ?.annotations
        ?.filterIsInstance<AuthRequired>()
        ?.firstOrNull()
}

class NetInterceptor(
    private val authUseCase: AuthUseCase,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .apply {
                findAuthAnnotation(chain.request())?.also {
                    runBlocking {
                        try {
                            authUseCase.getAccessTokenForInterceptor()
                        } catch (e: Throwable) {
                            throw IOException(e.message, e)
                        }
                    }?.also { token ->
                        addHeader("Authorization", "Bearer $token")
                    }
                }
            }
            .build()
        return chain.proceed(request)
    }
}
