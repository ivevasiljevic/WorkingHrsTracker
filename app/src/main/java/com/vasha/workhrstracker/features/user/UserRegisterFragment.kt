package com.vasha.workhrstracker.features.user

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.vasha.workhrstracker.R
import com.vasha.workhrstracker.data.User
import com.vasha.workhrstracker.databinding.UserRegisterFragmentBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Created by ivasil on 1/26/2022
 */

class UserRegisterFragment : Fragment(R.layout.user_register_fragment) {

    private val binding by viewBinding(UserRegisterFragmentBinding::bind)
    private val viewModel: UserViewModel by sharedViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
                        }
                        UserViewModel.UserState.UserSuccessful -> {
                            binding.progressBar.isVisible = false
                            findNavController().navigate(UserRegisterFragmentDirections.actionUserRegisterFragmentToAdminFragment())
                        }
                    }
                }
            }
        }

        binding.registerButton.setOnClickListener {
            if(binding.passwordLayout.editText?.text?.count()!! < 4 || binding.passwordLayout.editText?.text?.count()!! > 4) {
                binding.passwordLayout.error = "Must be 4 numbers"
            }
            else if(binding.usernameLayout.editText?.text?.count()!! < 1) {
                binding.usernameLayout.error = "Cannot be empty"
            }
            else {
                binding.passwordLayout.error = null
                binding.usernameLayout.error = null
                viewModel.registerUser(User(binding.usernameEdit.text.toString(), binding.passwordEdit.text.toString()))
            }
        }
    }
}