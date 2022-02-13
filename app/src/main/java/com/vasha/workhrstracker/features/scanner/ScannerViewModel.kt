package com.vasha.workhrstracker.features.scanner

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vasha.workhrstracker.api.ServerSoftException
import com.vasha.workhrstracker.data.Employee
import com.vasha.workhrstracker.data.Log
import com.vasha.workhrstracker.data.Scan
import com.vasha.workhrstracker.data.ScannerRepository
import com.vasha.workhrstracker.util.Resource
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*

/**
 * Created by ivasil on 1/26/2022
 */

class ScannerViewModel(private val scannerRepository: ScannerRepository) : ViewModel() {

    val scannerUiState = MutableSharedFlow<ScannerUiState>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )

    private var fullEmployeeList: List<Employee> = listOf()
    val fullEmployeeListState = MutableLiveData<List<Employee>?>(null)

    private var fullEmployeeLogsList: List<Log> = listOf()
    val fullEmployeeLogsListState = MutableLiveData<List<Log>?>(null)

    fun scanQrCode(scan: Scan) = viewModelScope.launch {
        scannerRepository.scanQrCode(scan).collect {
            when(it) {
                is Resource.Error -> {
                    scannerUiState.tryEmit(ScannerUiState.ScanFailed(it.message.toString(), it.isSoftException))
                }
                is Resource.Loading -> {
                    scannerUiState.tryEmit(ScannerUiState.ScanLoading)
                }
                is Resource.Success -> {
                    if(it.data != null)
                        scannerUiState.tryEmit(ScannerUiState.ScanSuccessful(it.data.status))
                }
            }
        }
    }

    fun getEmployees() = viewModelScope.launch {
        scannerRepository.getEmployees().collect {
            when(it) {
                is Resource.Error -> {
                    scannerUiState.tryEmit(ScannerUiState.EmployeesState.Error)
                }
                is Resource.Loading -> {
                    scannerUiState.tryEmit(ScannerUiState.EmployeesState.Loading)
                }
                is Resource.Success -> {
                    if(it.data != null)
                        scannerUiState.tryEmit(ScannerUiState.EmployeesState.Success(it.data))
                }
            }
        }
    }

    fun getEmployeeLogs(employeeId: String) = viewModelScope.launch {
        scannerRepository.getEmployeeLogs(employeeId).collect {
            when(it) {
                is Resource.Error -> {
                    scannerUiState.tryEmit(ScannerUiState.LogsError(it.message.toString(), it.isSoftException))
                }
                is Resource.Loading -> {
                    scannerUiState.tryEmit(ScannerUiState.LogsLoading)
                }
                is Resource.Success -> {
                    if(it.data != null)
                        scannerUiState.tryEmit(ScannerUiState.LogsSuccess(it.data))
                }
            }
        }
    }

    fun reset() {
        scannerUiState.resetReplayCache()
    }

    fun saveFullEmployeeList(employeeList: List<Employee>) {
        fullEmployeeList = employeeList
    }

    fun saveFullEmployeeLogsList(employeeLogsList: List<Log>) {
        fullEmployeeLogsList = employeeLogsList
    }

    fun filterEmployeeList(query: String) {
        fullEmployeeListState.value = fullEmployeeList.filter { it.username.lowercase().contains(query.lowercase()) }
    }

    fun sortEmployeeLogsList(sortType: SortType) {
        when(sortType) {
            SortType.TYPE_SORT -> fullEmployeeLogsListState.value = fullEmployeeLogsList.sortedBy { it.type }
            SortType.TIMESTAMP_SORT -> fullEmployeeLogsListState.value = fullEmployeeLogsList.sortedBy { it.timestamp }
        }
    }

    fun onCloseButtonClicked() = viewModelScope.launch {
        scannerUiState.tryEmit(ScannerUiState.NavigateUp)
    }

    sealed class ScannerUiState {
        object Empty : ScannerUiState()
        data class ScanSuccessful(val data: String) : ScannerUiState()
        object ScanLoading : ScannerUiState()
        data class ScanFailed(val error: String, val isSoftException: Boolean) : ScannerUiState()
        object NavigateUp : ScannerUiState()
        object LogsLoading : ScannerUiState()
        data class LogsSuccess(val logs: List<Log>) : ScannerUiState()
        data class LogsError(val errorMessage: String, val isSoftException: Boolean) : ScannerUiState()
        sealed class EmployeesState {
            object Loading : ScannerUiState()
            data class Success(val employees: List<Employee>) : ScannerUiState()
            object Error : ScannerUiState()
        }
    }
}