package com.vasha.workhrstracker.data

/**
 * Created by ivasil on 1/26/2022
 */

data class RegisterUser(
 val data: RegisterData
)

data class RegisterData(
 val username: String,
 val qrCode: String
)

