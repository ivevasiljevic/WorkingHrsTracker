package com.vasha.workhrstracker.api

import com.vasha.workhrstracker.data.*
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * Created by ivasil on 1/26/2022
 */

interface WorkingHrsTrackerApi {

    @POST("scan")
    suspend fun scanQrCode(@Body scanBody: Scan) : ScanStatus

    @POST("register")
    suspend fun registerUser(@Body user: User) : RegisterUser

    @GET("logs")
    suspend fun getLogs() : List<Employee>
}