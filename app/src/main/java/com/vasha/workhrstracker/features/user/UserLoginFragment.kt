package com.vasha.workhrstracker.features.user

import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.core.view.isInvisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.vasha.workhrstracker.R
import com.vasha.workhrstracker.databinding.UserLoginFragmentBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.util.concurrent.Executor
import kotlin.properties.Delegates

/**
 * Created by ivasil on 1/25/2022
 */

class UserLoginFragment : Fragment(R.layout.user_login_fragment), View.OnClickListener, View.OnLongClickListener {

    private val binding by viewBinding(UserLoginFragmentBinding::bind)
    private val viewModel: UserViewModel by sharedViewModel()

    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    private var pin by Delegates.observable("") { _, _, newValue ->
        binding.backspace.isInvisible = newValue.isBlank()
        binding.finerprint.isInvisible = newValue.isNotBlank()

        if(newValue.length == 4) {
            viewModel.loginUser(newValue)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            welcomeUser.text = "Welcome back ${viewModel.fullName.split(" ")[0]},"
            no0.setOnClickListener(this@UserLoginFragment)
            no1.setOnClickListener(this@UserLoginFragment)
            no2.setOnClickListener(this@UserLoginFragment)
            no3.setOnClickListener(this@UserLoginFragment)
            no4.setOnClickListener(this@UserLoginFragment)
            no5.setOnClickListener(this@UserLoginFragment)
            no6.setOnClickListener(this@UserLoginFragment)
            no7.setOnClickListener(this@UserLoginFragment)
            no8.setOnClickListener(this@UserLoginFragment)
            no9.setOnClickListener(this@UserLoginFragment)
            backspace.setOnClickListener(this@UserLoginFragment)
            backspace.setOnLongClickListener(this@UserLoginFragment)
            finerprint.setOnClickListener(this@UserLoginFragment)
        }


        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                viewModel.userUiState.collect {
                    when(it) {
                        UserViewModel.UserState.Empty -> {
                            //do nothing
                        }
                        is UserViewModel.UserState.UserFailed -> {
                            val shake: Animation =
                                AnimationUtils.loadAnimation(requireContext(), R.anim.shake)
                            binding.indicatorDots.startAnimation(shake)
                        }
                        UserViewModel.UserState.UserSuccessful -> {
                            //findNavController().navigate(UserLoginFragmentDirections.actionUserLoginFragmentToAdminFragment())
                        }
                    }
                }
            }
        }

        executor = ContextCompat.getMainExecutor(requireContext())
        biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int,
                                                   errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    val shake: Animation =
                        AnimationUtils.loadAnimation(requireContext(), R.anim.shake)
                    binding.indicatorDots.startAnimation(shake)
                }

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    //findNavController().navigate(UserLoginFragmentDirections.actionUserLoginFragmentToAdminFragment())
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    val shake: Animation =
                        AnimationUtils.loadAnimation(requireContext(), R.anim.shake)
                    binding.indicatorDots.startAnimation(shake)
                }
            })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Time tracker")
            .setSubtitle("Identify yourself using your biometric credential")
            .setNegativeButtonText("Cancel")
            .build()
    }

    override fun onClick(button: View?) {
        when(button?.id) {
            R.id.backspace -> {
                pin = pin.substring(0, pin.length - 1)
                changeIndicatorDotsState()
                return
            }
            R.id.finerprint -> {
                biometricPrompt.authenticate(promptInfo)
            }
            else -> {
                if(pin.length < 4) {
                    pin += (button as Button).text.toString()
                    changeIndicatorDotsState()
                }
            }
        }
    }

    override fun onLongClick(p0: View?): Boolean {
        pin = ""
        changeIndicatorDotsState()
        return true
    }

    private fun changeIndicatorDotsState() {
        for(i in pin.indices) {
            binding.indicatorDots.getChildAt(i).setBackgroundResource(R.drawable.dot_filled)
        }
        for(j in (pin.length) until 4) {
            binding.indicatorDots.getChildAt(j).setBackgroundResource(R.drawable.dot_empty)
        }
    }
}