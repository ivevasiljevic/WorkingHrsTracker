package com.vasha.workhrstracker.api

import android.util.Log
import com.google.gson.Gson
import com.vasha.workhrstracker.data.ScanStatus
import okhttp3.Interceptor
import okhttp3.Response
import org.json.JSONException
import org.json.JSONObject


class ServerInterceptor : Interceptor {

    @Throws(ServerSoftException::class)
    override fun intercept(chain: Interceptor.Chain): Response {

        val response = chain.proceed(chain.request())

        if(response.code == 400 || response.code == 404) {
            val gson = Gson()
            val responseMessage = response.body?.byteString()?.utf8().toString()
            if(isJson(responseMessage)) {
                throw ServerSoftException(gson.fromJson(responseMessage, ScanStatus::class.java).message)
            }
            else {
                throw ServerSoftException(responseMessage)
            }
        }

        return response
    }

    private fun isJson(responseBody: String): Boolean {
        try {
            val o = JSONObject(responseBody)
        } catch (e: JSONException) {
            return false
        }
        return true
    }
}