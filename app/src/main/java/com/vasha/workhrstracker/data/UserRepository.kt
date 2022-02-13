package com.vasha.workhrstracker.data

import com.vasha.workhrstracker.api.WorkingHrsTrackerApi
import com.vasha.workhrstracker.util.safeApiCall

/**
 * Created by ivasil on 1/26/2022
 */

class UserRepository(private val workingHrsTrackerApi: WorkingHrsTrackerApi) {

   fun registerUser(user: User) = safeApiCall {
       workingHrsTrackerApi.registerUser(user)
   }
}