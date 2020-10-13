package com.reihan.hira.utils.api

import android.content.Context
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class APIClient {

    companion object {
        const val BASE_URL = "http://34.229.16.81:8080/"
        private var retrofit: Retrofit? = null

        private fun provideHttpLoggingInterceptor() = run {
            HttpLoggingInterceptor().apply {
                apply { level = HttpLoggingInterceptor.Level.BODY }
            }
        }

        fun getApiClientToken(mContext: Context): Retrofit? {
            if (retrofit == null) {
                val okHttpClient =
                    OkHttpClient
                        .Builder()
                        .addInterceptor(provideHttpLoggingInterceptor())
                        .addInterceptor(HeaderInterceptor(mContext))
                        .connectTimeout(1, TimeUnit.MINUTES)
                        .readTimeout(1, TimeUnit.MINUTES)
                        .writeTimeout(1, TimeUnit.MINUTES)
                        .build()

                retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }

            return retrofit
        }

        fun getApiClient(mContext: Context): Retrofit? {
            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit
        }
    }
}