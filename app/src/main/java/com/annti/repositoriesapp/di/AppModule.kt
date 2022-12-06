package com.annti.repositoriesapp.di

import com.annti.repositoriesapp.data.network.NetInterceptor
import com.annti.repositoriesapp.data.network.RetrofitFactory
import com.annti.repositoriesapp.data.network.api.AuthApi
import com.annti.repositoriesapp.data.network.api.RepoApi
import com.annti.repositoriesapp.data.repository.*
import com.annti.repositoriesapp.domain.AuthUseCase
import com.annti.repositoriesapp.domain.AuthUseCaseImpl
import com.annti.repositoriesapp.presentation.auth.AuthViewModel
import com.annti.repositoriesapp.presentation.main.MainViewModel
import com.annti.repositoriesapp.presentation.splash.SplashViewModel
import com.annti.repositoriesapp.utils.MoshiInstantAdapter
import com.annti.repositoriesapp.domain.FileManager
import com.annti.repositoriesapp.domain.FileManagerImpl
import com.annti.repositoriesapp.presentation.download.DownloadViewModel
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import java.util.concurrent.TimeUnit

const val BASE_URL = "https://api.github.com/"

val appModule = module {

    single {
        val okHttp = OkHttpClient().newBuilder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
        RetrofitFactory(okHttp).makeService<AuthApi>(BASE_URL)
    }

    single {
        OkHttpClient().newBuilder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .addInterceptor(NetInterceptor(get()))
            .build()
    }



    single {
        NetInterceptor(authUseCase = get())
    }


    single {
        RetrofitFactory(okHttpClient = get())
    }

    single {
        get<RetrofitFactory>().makeService<RepoApi>(BASE_URL)
    }

    single {
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .add(MoshiInstantAdapter())
            .build()
    }

    single<AuthRepository> {
        AuthRepositoryImpl(
            authApi = get()
        )
    }


    single<AuthUseCase> {
        AuthUseCaseImpl(
            authRepository = get()
        )
    }

    single<FileManager> {
        FileManagerImpl(
            context = get()
        )
    }

    viewModel {
        AuthViewModel(
            authUseCase = get()
        )
    }

    single<RepoRemoteDataSource> {
        RepoRemoteDataSourceImpl(
            repoApi = get()
        )
    }

    viewModel {
        MainViewModel(
            repoDataSource = get(),
            fileManager = get()
        )
    }

    viewModel {
        SplashViewModel()
    }

    viewModel {
        DownloadViewModel(
            fileManager = get()
        )
    }

}