package com.vasha.workhrstracker.features.user

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.vasha.workhrstracker.R
import com.vasha.workhrstracker.data.User
import com.vasha.workhrstracker.databinding.UserRegisterPinFragmentBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import kotlin.properties.Delegates

class UserRegisterPINFragment : Fragment(R.layout.user_register_pin_fragment), View.OnClickListener, View.OnLongClickListener {

    private val binding by viewBinding(UserRegisterPinFragmentBinding::bind)
    private val viewModel: UserViewModel by sharedViewModel()

    private var pin by Delegates.observable("") { _, _, newValue ->
        binding.backspace.isInvisible = newValue.isBlank()

        if(newValue.length == 4) {
            viewModel.registerUser(User(viewModel.fullName, newValue))
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            userFirstName.text = "${viewModel.fullName.split(" ")[0]},"
            no0.setOnClickListener(this@UserRegisterPINFragment)
            no1.setOnClickListener(this@UserRegisterPINFragment)
            no2.setOnClickListener(this@UserRegisterPINFragment)
            no3.setOnClickListener(this@UserRegisterPINFragment)
            no4.setOnClickListener(this@UserRegisterPINFragment)
            no5.setOnClickListener(this@UserRegisterPINFragment)
            no6.setOnClickListener(this@UserRegisterPINFragment)
            no7.setOnClickListener(this@UserRegisterPINFragment)
            no8.setOnClickListener(this@UserRegisterPINFragment)
            no9.setOnClickListener(this@UserRegisterPINFragment)
            backspace.setOnClickListener(this@UserRegisterPINFragment)
            backspace.setOnLongClickListener(this@UserRegisterPINFragment)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                viewModel.userUiState.collect {
                    when(it) {
                        UserViewModel.UserState.Empty -> {
                            //do nothing
                        }
                        UserViewModel.UserState.Loading -> {
                            binding.progressBar.isVisible = true
                        }
                        is UserViewModel.UserState.UserFailed -> {
                            binding.progressBar.isVisible = false
                            findNavController().navigate(UserRegisterPINFragmentDirections.actionUserRegisterPINFragmentToUserDialog())
                        }
                        UserViewModel.UserState.UserSuccessful -> {
                            binding.progressBar.isVisible = false
                            findNavController().navigate(UserRegisterPINFragmentDirections.actionUserRegisterPINFragmentToAdminFragment())
                        }
                    }
                }
            }
        }
    }

    override fun onClick(button: View?) {
        when(button?.id) {
            R.id.backspace -> {
                pin = pin.substring(0, pin.length - 1)
                changeIndicatorDotsState()
                return
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