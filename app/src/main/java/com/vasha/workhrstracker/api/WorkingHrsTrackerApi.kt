package com.vasha.workhrstracker.api

import com.vasha.workhrstracker.data.*
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Created by ivasil on 1/26/2022
 */

interface WorkingHrsTrackerApi {

    @POST("scan")
    suspend fun scanQrCode(@Body scanBody: Scan) : ScanStatus

    @POST("register")
    suspend fun registerUser(@Body user: User) : RegisterUser

    @GET("logs/{id}")
    suspend fun getEmployeeLogs(@Path("id") employeeId: String) : List<Log>

    @GET("employees")
    suspend fun getEmployees() : List<Employee>
}