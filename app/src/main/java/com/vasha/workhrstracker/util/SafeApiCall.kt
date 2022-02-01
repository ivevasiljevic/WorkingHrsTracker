package com.vasha.workhrstracker.util

import kotlinx.coroutines.flow.channelFlow
import retrofit2.HttpException

/**
 * Created by ivasil on 1/26/2022
 */

inline fun <RequestType> safeApiCall(
    crossinline apiCall: suspend () -> RequestType
) = channelFlow<Resource<RequestType>> {

    send(Resource.Loading())

    try {
        send(Resource.Success(apiCall.invoke()))
    }
    catch (e: HttpException) {
        send(Resource.Error(e, null))
    }
    catch (e: Throwable) {
        send(Resource.Error(e, null))
    }
}