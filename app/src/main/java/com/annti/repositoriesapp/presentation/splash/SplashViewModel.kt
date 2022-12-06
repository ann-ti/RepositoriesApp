package com.annti.repositoriesapp.presentation.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.annti.repositoriesapp.data.db.Database
import com.annti.repositoriesapp.data.model.AccessToken

class SplashViewModel: ViewModel() {

    private val dao = Database.instance.mainDao()

    fun accessTokenLive(): LiveData<AccessToken> = dao.getAccessToken()
}