package com.annti.repositoriesapp.data.network.api

import com.annti.repositoriesapp.data.model.AuthResponse
import retrofit2.Response
import retrofit2.http.*

interface AuthApi {

    @Headers("Accept: application/json")
    @POST
    @FormUrlEncoded
    suspend fun getAccessToken(
        @Url baseUrl: String,
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String,
        @Field("code") oAuthCode: String,
        @Field("redirect_uri") redirectUrl: String
    ): Response<AuthResponse>
}