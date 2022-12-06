package com.annti.repositoriesapp.presentation.splash

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.annti.repositoriesapp.MainActivity
import com.annti.repositoriesapp.presentation.auth.AuthActivity
import kotlinx.coroutines.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashActivity : AppCompatActivity() {

    private val viewModel by viewModel<SplashViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        actionBar?.hide()
        CoroutineScope(Dispatchers.IO).launch {
            delay(1000)
            withContext(Dispatchers.Main) {
                viewModel.accessTokenLive().observe(this@SplashActivity) {
                    nextActivity(
                        if (it != null) MainActivity::class.java else AuthActivity::class.java
                    )
                }
            }
        }
    }

    private fun nextActivity(clazz: Class<*>) {
        startActivity(Intent(this, clazz))
        finish()
    }
}