package com.vasha.workhrstracker.features.scanner

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.budiyev.android.codescanner.*
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.zxing.BarcodeFormat
import com.vasha.workhrstracker.BuildConfig
import com.vasha.workhrstracker.R
import com.vasha.workhrstracker.data.Scan
import com.vasha.workhrstracker.databinding.ScannerCameraFragmentBinding
import com.vasha.workhrstracker.features.scanner.ActivityType.*
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Created by ivasil on 1/25/2022
 */

class ScannerCameraFragment : Fragment(R.layout.scanner_camera_fragment) {

    private val binding by viewBinding(ScannerCameraFragmentBinding::bind)
    private val viewModel: ScannerViewModel by sharedViewModel()
    //private val args: ScannerCameraFragmentArgs by navArgs()

    private lateinit var codeScanner: CodeScanner
    private var activityResultLauncher: ActivityResultLauncher<String> = registerForActivityResult(
        ActivityResultContracts.RequestPermission()) { isGranted ->
        when {
            isGranted -> {
                //Success
            }
            !isGranted && !ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.CAMERA) -> {
                Toast.makeText(requireContext(), "If you want to use this feature, you need to enable it in settings", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri: Uri = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null)
                intent.data = uri
                startActivity(intent)
            }
            else -> {
                Toast.makeText(requireContext(), "Denied", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requestCameraPermission()
        initializeScannerCamera()

        /*val activity = when(args.activityType) {
            ARRIVAL -> "arrival to work"
            DEPARTURE -> "departure from work"
            BREAK_START -> "start of lunch break"
            BREAK_END -> "end of lunch break"
        }*/

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                viewModel.scannerEvent.collect {
                    when(it) {
                        ScannerViewModel.ScannerUiState.Empty -> {
                            //do nothing
                        }
                        is ScannerViewModel.ScannerUiState.ScanFailed -> {
                            binding.progressBar.isVisible = false
                            //findNavController().navigate(ScannerCameraFragmentDirections.actionScannerCameraFragmentToScannerDialog("Unsuccessfully logged $activity", TitleType.DENIED))
                        }
                        ScannerViewModel.ScannerUiState.ScanLoading -> {
                            binding.progressBar.isVisible = true
                        }
                        is ScannerViewModel.ScannerUiState.ScanSuccessful -> {
                            binding.progressBar.isVisible = false
                            //findNavController().navigate(ScannerCameraFragmentDirections.actionScannerCameraFragmentToScannerDialog("Successfully logged $activity", TitleType.APPROVED))
                        }
                        ScannerViewModel.ScannerUiState.NavigateUp -> {
                            findNavController().navigateUp()
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }

    private fun requestCameraPermission() {
        val appPerms = Manifest.permission.CAMERA
        activityResultLauncher.launch(appPerms)
    }

    private fun initializeScannerCamera() {

        codeScanner = CodeScanner(requireActivity(), binding.scannerView)

        codeScanner.apply {
            camera = CodeScanner.CAMERA_BACK
            formats = listOf(BarcodeFormat.QR_CODE)
            autoFocusMode = AutoFocusMode.CONTINUOUS
            scanMode = ScanMode.SINGLE
            isAutoFocusEnabled = true
            isFlashEnabled = false
        }

        codeScanner.decodeCallback = DecodeCallback {
            lifecycleScope.launch {
                //viewModel.scanQrCode(Scan(it.text, args.activityType.toString()))
            }
        }
        codeScanner.errorCallback = ErrorCallback {
            lifecycleScope.launch {
                Log.e("CameraError", "Camera initialization error")
            }
        }
    }
}