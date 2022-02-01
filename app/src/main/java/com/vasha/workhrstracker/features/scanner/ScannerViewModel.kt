package com.vasha.workhrstracker.features.scanner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vasha.workhrstracker.data.Scan
import com.vasha.workhrstracker.data.ScannerRepository
import com.vasha.workhrstracker.util.Resource
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.util.*

/**
 * Created by ivasil on 1/26/2022
 */

class ScannerViewModel(private val scannerRepository: ScannerRepository) : ViewModel() {

    private val _scannerEvent = Channel<ScannerUiState>()
    val scannerEvent = _scannerEvent.receiveAsFlow()

    fun scanQrCode(scan: Scan) = viewModelScope.launch {
        scannerRepository.scanQrCode(scan).collect {
            when(it) {
                is Resource.Error -> {
                    _scannerEvent.send(ScannerUiState.ScanFailed(it.message.toString()))
                }
                is Resource.Loading -> {
                    _scannerEvent.send(ScannerUiState.ScanLoading)
                }
                is Resource.Success -> {
                    if(it.data != null)
                        _scannerEvent.send(ScannerUiState.ScanSuccessful(it.data.status))
                }
            }
        }
    }

    fun onCloseButtonClicked() = viewModelScope.launch {
        _scannerEvent.send(ScannerUiState.NavigateUp)
    }

    sealed class ScannerUiState {
        object Empty : ScannerUiState()
        data class ScanSuccessful(val data: String) : ScannerUiState()
        object ScanLoading : ScannerUiState()
        data class ScanFailed(val error: String) : ScannerUiState()
        object NavigateUp : ScannerUiState()

    }
}