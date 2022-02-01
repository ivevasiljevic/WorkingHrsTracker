package com.vasha.workhrstracker.data

/**
 * Created by ivasil on 1/26/2022
 */

data class User(
 val username: String,
 val password: String,
 val adminRole: Boolean = false
)
