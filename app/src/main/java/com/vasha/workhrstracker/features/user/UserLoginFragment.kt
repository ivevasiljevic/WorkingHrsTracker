package com.vasha.workhrstracker.features.user

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.andrognito.pinlockview.PinLockListener
import com.vasha.workhrstracker.R
import com.vasha.workhrstracker.databinding.UserLoginFragmentBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.util.concurrent.Executor

/**
 * Created by ivasil on 1/25/2022
 */

class UserLoginFragment : Fragment(R.layout.user_login_fragment), PinLockListener {

    private val binding by viewBinding(UserLoginFragmentBinding::bind)
    private val viewModel: UserViewModel by sharedViewModel()

    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.pinLockView.attachIndicatorDots(binding.indicatorDots)
        binding.pinLockView.setPinLockListener(this)

        executor = ContextCompat.getMainExecutor(requireContext())
        biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int,
                                                   errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                }

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    findNavController().navigate(UserLoginFragmentDirections.actionUserLoginFragmentToAdminFragment())
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                }
            })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Time tracker")
            .setSubtitle("Identify yourself using your biometric credential")
            .setNegativeButtonText("Cancel")
            .build()

        binding.fingerprintButton.setOnClickListener {
            biometricPrompt.authenticate(promptInfo)
        }
    }

    override fun onComplete(pin: String?) {
        if(viewModel.comparePins(pin.toString())) {
            findNavController().navigate(UserLoginFragmentDirections.actionUserLoginFragmentToAdminFragment())
        }
        else {
            binding.errorText.isVisible = true
            binding.errorText.text = "Wrong PIN!"
        }
    }

    override fun onEmpty() {
        binding.fingerprintButton.isVisible = true
        binding.errorText.isVisible = false
    }

    override fun onPinChange(pinLength: Int, intermediatePin: String?) {
        binding.fingerprintButton.isVisible = false
        binding.errorText.isVisible = false
    }

}