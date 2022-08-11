package com.example.developersapp.data.retrofit

import com.example.developersapp.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitBuilder {

    fun <T> build(baseUrl: String, clazz: Class<T>): T {
        return Retrofit.Builder()
            .client(getOkHttpClientBuilder().build())
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(clazz)
    }

    private fun getOkHttpClientBuilder() =
        OkHttpClient.Builder().apply {
            connectTimeout(60, TimeUnit.SECONDS)
            readTimeout(60, TimeUnit.SECONDS)
            writeTimeout(60, TimeUnit.SECONDS)
            addClientLogInterceptor(this)
        }

    private fun addClientLogInterceptor(builder: OkHttpClient.Builder) {
        if (BuildConfig.DEBUG)
            builder.addLogInterceptor()
    }

    private fun OkHttpClient.Builder.addLogInterceptor(): OkHttpClient.Builder {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        addNetworkInterceptor(logging)
        return this
    }
}