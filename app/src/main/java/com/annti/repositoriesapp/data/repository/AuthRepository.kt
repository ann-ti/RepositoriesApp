package com.annti.repositoriesapp.data.repository

import com.annti.repositoriesapp.data.model.AuthResponse
import com.annti.repositoriesapp.data.network.api.AuthApi
import com.annti.repositoriesapp.utils.BaseDataSource
import com.annti.repositoriesapp.utils.ResultData

interface AuthRepository {
    suspend fun getAccessToken(
        clientId: String,
        clientSecret: String,
        oAuthCode: String,
        redirectUrl: String
    ): ResultData<AuthResponse>
}

class AuthRepositoryImpl(
    private val authApi: AuthApi
) : AuthRepository, BaseDataSource() {

    override suspend fun getAccessToken(
        clientId: String,
        clientSecret: String,
        oAuthCode: String,
        redirectUrl: String
    ) = getResult {
        authApi.getAccessToken(
            ACCESS_TOKEN_URL,
            clientId,
            clientSecret,
            oAuthCode,
            redirectUrl
        )
    }

    companion object {
        private const val ACCESS_TOKEN_URL = "https://github.com/login/oauth/access_token"
    }

}