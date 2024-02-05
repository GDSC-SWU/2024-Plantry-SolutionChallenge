package com.plantry.data

import android.util.Log
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.plantry.BuildConfig.BASE_URL
import com.plantry.data.API.API_TAG
import com.plantry.data.api.LogoutApiService
import com.plantry.data.api.RefreshTokenApiService
import com.plantry.data.api.SignInApiService
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Retrofit


object ApiPool {
    val getSignIn = RetrofitPool.retrofit.create(SignInApiService::class.java)
    val deleteLogOut = RetrofitPool.retrofit.create(LogoutApiService::class.java)
    val getRefreshToken = RetrofitPool.retrofit.create(RefreshTokenApiService::class.java)
}


object RetrofitPool {
    var accessToken: String? = null
    var refreshToken: String? = null
    var userId: Int? = null

    fun setAccessToken(token: String?) {
        accessToken = token
    }
    fun setRefreshToken(token: String?) {
        refreshToken = token
    }
    fun setUserId(id: Int?) {
        userId = id
    }


    val retrofit: Retrofit by lazy {
        val loggingInterceptor = HttpLoggingInterceptor { message ->
            when {
                message.isJsonObject() ->
                    Log.d(API_TAG, JSONObject(message).toString(4))

                message.isJsonArray() ->
                    Log.d(API_TAG, JSONArray(message).toString(4))

                else -> {
                    Log.d(API_TAG, "CONNECTION INFO -> $message")
                }
            }
        }

        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        Retrofit.Builder()
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .build()
    }

}

object API {
    const val API_TAG = "Retrofit2"
}