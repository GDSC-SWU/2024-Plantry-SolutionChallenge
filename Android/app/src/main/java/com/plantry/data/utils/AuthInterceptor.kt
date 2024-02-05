package com.plantry.data.utils

import android.util.Log
import com.google.android.gms.common.ConnectionResult.NETWORK_ERROR
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody
import java.net.HttpURLConnection.HTTP_OK

class AuthInterceptor constructor(
    private val tokenManager: TokenManager
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {

        val request = chain.request().newBuilder().header("Authorization",
            "Bearer " + tokenManager.getAccessToken().toString()
        ).build()


        val response = chain.proceed(request)

        if (response.code == HTTP_OK) {
            val newAccessToken: String = response.header("Authorization", null) ?: return response

            CoroutineScope(Dispatchers.IO).launch {
                val existedAccessToken = tokenManager.getAccessToken().first()
                if (existedAccessToken != newAccessToken) {
                    tokenManager.saveAccessToken(newAccessToken)
                    Log.d("aaa","newAccessToken = ${newAccessToken}\nExistedAccessToken = ${existedAccessToken}")
                }
            }

        } else {
            Log.e("aaa","${response.code} : ${response.request} \n ${response.message}")
        }
        return response
    }

    private fun errorResponse(request: Request): Response = Response.Builder()
        .request(request)
        .protocol(Protocol.HTTP_2)
        .code(NETWORK_ERROR)
        .message("")
        .body(ResponseBody.create(null, ""))
        .build()
}