package com.annti.repositoriesapp.domain


import androidx.lifecycle.LiveData
import com.annti.repositoriesapp.data.db.Database
import com.annti.repositoriesapp.data.model.AccessToken
import com.annti.repositoriesapp.data.model.AccessTokenDto
import com.annti.repositoriesapp.data.repository.AuthRepository
import com.annti.repositoriesapp.utils.ResultData
import com.annti.repositoriesapp.utils.resultLiveDataPersistent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

interface AuthUseCase {
    fun getAccessToken(
        clientId: String,
        clientSecret: String,
        oAuthCode: String,
        redirectUrl: String
    ): LiveData<ResultData<AccessToken>>

    fun deleteAccessToken(): Job
    fun getAccessTokenForInterceptor(): String?
}

class AuthUseCaseImpl(
    private val authRepository: AuthRepository
) : AuthUseCase {

    private val dao = Database.instance.mainDao()

    override fun getAccessToken(
        clientId: String,
        clientSecret: String,
        oAuthCode: String,
        redirectUrl: String
    ) = resultLiveDataPersistent(
        networkCall = {
            authRepository.getAccessToken(
                clientId,
                clientSecret,
                oAuthCode,
                redirectUrl
            )
        },
        saveLocal = { dao.insertAccessToken(AccessTokenDto.fromResponse(it)) },
        observeLocal = { dao.getAccessToken() }
    )

    override fun getAccessTokenForInterceptor(): String? {
        return dao.getAccessTokenSync()?.token
    }

    override fun deleteAccessToken() = CoroutineScope(Dispatchers.IO).launch {
        dao.deleteAccessToken()
    }
}