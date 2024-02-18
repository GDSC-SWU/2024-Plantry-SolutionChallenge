package com.plantry.data

import android.util.Log
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.plantry.BuildConfig.BASE_URL
import com.plantry.data.API.API_TAG
import com.plantry.data.api.signin.LogoutApiService
import com.plantry.data.api.pantry.PantryAddApiService
import com.plantry.data.api.pantry.PantryDeleteApiService
import com.plantry.data.api.pantry.PantryEditApiService
import com.plantry.data.api.pantry.PantryListApiService
import com.plantry.data.api.pantry.PantryStarApiService
import com.plantry.data.api.product.ProductAddApiService
import com.plantry.data.api.product.ProductAddMultiApiService
import com.plantry.data.api.product.ProductDeleteApiService
import com.plantry.data.api.product.ProductEditApiService
import com.plantry.data.api.product.ProductEditCountApiService
import com.plantry.data.api.product.ProductListApiService
import com.plantry.data.api.product.ProductSearchApiService
import com.plantry.data.api.signin.RefreshTokenApiService
import com.plantry.data.api.signin.SignInApiService
import com.plantry.data.dto.BaseResponseNullable
import com.plantry.data.dto.response.signin.RefreshTokenDto
import kotlinx.coroutines.runBlocking
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

    val getPantryList = RetrofitPool.retrofit.create(PantryListApiService::class.java)
    val postAddPantry = RetrofitPool.retrofit.create(PantryAddApiService::class.java)
    val deletePantry = RetrofitPool.retrofit.create(PantryDeleteApiService::class.java)
    val patchEditPantry = RetrofitPool.retrofit.create(PantryEditApiService::class.java)
    val patchSetStar = RetrofitPool.retrofit.create(PantryStarApiService::class.java)

    val postAddSingleProduct = RetrofitPool.retrofit.create(ProductAddApiService::class.java)
    val postAddMultiProduct = RetrofitPool.retrofit.create(ProductAddMultiApiService::class.java)
    val deleteProduct = RetrofitPool.retrofit.create(ProductDeleteApiService::class.java)
    val patchEditProduct = RetrofitPool.retrofit.create(ProductEditApiService::class.java)
    val patchEditCountProduct = RetrofitPool.retrofit.create(ProductEditCountApiService::class.java)
    val getListSearchProduct = RetrofitPool.retrofit.create(ProductListApiService::class.java)
    val getSearchProduct = RetrofitPool.retrofit.create(ProductSearchApiService::class.java)
}


object RetrofitPool {
    private var accessToken: String? = null
    private var refreshToken: String? = null
    private var userId: Int? = null

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

        val okHttpClient =
            OkHttpClient.Builder().addInterceptor(loggingInterceptor).addInterceptor { chain ->
                // AccessToken이 있는 경우, 헤더에 추가합니다.
                val request = accessToken?.let { token ->
                    chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer $token")
                        .build()
                } ?: chain.request()

                var response = chain.proceed(request)

                if (response.code == 401) {
                    runBlocking {
                        val refreshToken = RetrofitPool.refreshToken

                        if (refreshToken != null) {
                            val refreshResponse : BaseResponseNullable<RefreshTokenDto> = ApiPool.getRefreshToken.getToken()

                            if (refreshResponse.data!=null) {
                                setAccessToken(refreshResponse.data.accessToken)
                                setAccessToken(refreshResponse.data.accessToken)

                                val refreshRequest = refreshToken?.let { refreshToken ->
                                    chain.request().newBuilder()
                                        .addHeader("Authorization", "Bearer ${accessToken}")
                                        .build()
                                } ?: chain.request()
                                response.close()
                                response = chain.proceed(refreshRequest)
                            }
                            else {
                                Log.e("refreshResponse.data : 서버 통신", "refreshResponse.data == null")
                            }
                        }
                        else {
                            Log.e("refreshToken", "refreshToken == null")
                        }
                    }
                    response
                } else {
                    response
                }
            }.build()

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