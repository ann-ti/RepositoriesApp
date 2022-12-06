package com.annti.repositoriesapp.data.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import kotlin.reflect.KClass

class RetrofitFactory(private val okHttpClient: OkHttpClient) {

    private val converterFactory by lazy {
        MoshiConverterFactory.create()
    }

    private val coroutineCallAdapterFactory by lazy {
        CoroutineCallAdapterFactory()
    }

    fun <T : Any> makeService(baseUrl: String, clazz: KClass<T>): T =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addCallAdapterFactory(coroutineCallAdapterFactory)
            .addConverterFactory(converterFactory)
            .client(okHttpClient)
            .build().create(clazz.java)

    inline fun <reified T : Any> makeService(baseUrl: String): T = makeService(baseUrl, T::class)
}