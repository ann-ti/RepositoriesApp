package com.annti.repositoriesapp.presentation.auth

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.annti.repositoriesapp.BuildConfig
import com.annti.repositoriesapp.domain.AuthUseCase
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authUseCase: AuthUseCase
) : ViewModel() {
    private val clientId = BuildConfig.CLIENT_ID
    private val clientSecret = BuildConfig.CLIENT_SECRET

    fun getOAuthUrl(redirectUrl: String) =
        "https://github.com/login/oauth/authorize?client_id=$clientId&redirect_uri=$redirectUrl"

    fun verifyOAuthResponse(uri: Uri?, startsWith: String): Boolean =
        uri != null && uri.toString().startsWith(startsWith)

    fun getCode(uri: Uri) = uri.getQueryParameter("code")

    fun getAccessToken(
        oAuthCode: String,
        redirectUrl: String
    ) = authUseCase.getAccessToken(clientId, clientSecret, oAuthCode, redirectUrl)


    fun removeAccessToken() = viewModelScope.launch {
        authUseCase.deleteAccessToken()
    }

}