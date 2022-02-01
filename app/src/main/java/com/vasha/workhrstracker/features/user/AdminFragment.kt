package com.vasha.workhrstracker.features.user

import android.os.Bundle
import android.util.Log
import android.view.View
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.zxing.WriterException
import com.vasha.workhrstracker.R
import com.vasha.workhrstracker.databinding.AdminFragmentBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

/**
 * Created by ivasil on 1/26/2022
 */

class AdminFragment : Fragment(R.layout.admin_fragment) {

    private val binding by viewBinding(AdminFragmentBinding::bind)
    private val viewModel: UserViewModel by sharedViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getUser()

        binding.employeesList.setOnClickListener {
            findNavController().navigate(AdminFragmentDirections.actionAdminFragmentToEmployeeListFragment())
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                viewModel.userUiState.collect {
                    when(it) {
                        is UserViewModel.UserState.ShowUserData -> {
                            binding.employeeNameTextView.text = it.username
                            generateQrCode(it.qrCode)
                        }
                    }
                }
            }
        }
    }

    private fun generateQrCode(text: String) {
        val qrgEncoder = QRGEncoder(text, null, QRGContents.Type.TEXT, 620)
        try {
            val bitmap = qrgEncoder.encodeAsBitmap()
            binding.qrcodeImage.setImageBitmap(bitmap)
        } catch (e: WriterException) {
            Log.e("Tag", e.toString())
        }
    }
}