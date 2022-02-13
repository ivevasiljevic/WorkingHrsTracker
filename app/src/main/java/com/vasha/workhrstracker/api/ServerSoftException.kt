package com.vasha.workhrstracker.api

import java.io.IOException

class ServerSoftException(private val responseMessage: String?) : IOException() {

    override val message: String?
        get() = responseMessage
}