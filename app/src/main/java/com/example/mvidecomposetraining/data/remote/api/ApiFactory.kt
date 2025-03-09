package com.example.mvidecomposetraining.data.remote.api

import com.example.mvidecomposetraining.BuildConfig
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.Locale

object ApiFactory {

    private const val BASE_URL = "https://api.weatherapi.com/v1/"
    private const val KEY = "key"
    private const val PARAM_LANG = "lang"

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val currentRequest = chain.request()
            val newRequest = currentRequest.url().newBuilder()
                .addQueryParameter(KEY, BuildConfig.API_KEY)
                .addQueryParameter(PARAM_LANG, Locale.getDefault().language)
                .build()
            val newUrl = currentRequest.newBuilder()
                .url(newRequest)
                .build()
            chain.proceed(newUrl)
        }.build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()

    val apiService: ApiService = retrofit.create()
}