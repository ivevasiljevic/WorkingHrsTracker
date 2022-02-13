package com.vasha.workhrstracker.data

import com.google.gson.annotations.SerializedName

/**
 * Created by ivasil on 1/27/2022
 */

data class Employee(
 @SerializedName("_id") val id: String,
 val username: String
)