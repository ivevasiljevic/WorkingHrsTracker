package com.vasha.workhrstracker.data

import com.vasha.workhrstracker.api.WorkingHrsTrackerApi
import com.vasha.workhrstracker.util.safeApiCall

/**
 * Created by ivasil on 1/26/2022
 */

class ScannerRepository(private val workingHrsTrackerApi: WorkingHrsTrackerApi) {

    fun scanQrCode(scan: Scan) = safeApiCall {
        workingHrsTrackerApi.scanQrCode(scan)
    }
}