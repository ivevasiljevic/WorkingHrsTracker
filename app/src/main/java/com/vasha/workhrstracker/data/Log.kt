package com.vasha.workhrstracker.data

import com.google.gson.annotations.SerializedName

data class Log(
    @SerializedName("_id") val id: String,
    val type: String,
    val timestamp: String
)
