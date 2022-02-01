package com.vasha.workhrstracker.util

/**
 * Created by ivasil on 1/26/2022
 */

sealed class Resource<T>(
 val data: T? = null,
 val message: String? = null
) {
   class Success<T>(data: T) : Resource<T>(data, null)
   class Loading<T>(data: T? = null) : Resource<T>(data, null)
   class Error<T>(throwable: Throwable, data: T? = null) : Resource<T>(data, throwable.localizedMessage)
}
