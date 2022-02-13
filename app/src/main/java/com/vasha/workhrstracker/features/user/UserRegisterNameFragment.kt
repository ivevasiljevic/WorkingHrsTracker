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
import com.vasha.workhrstracker.databinding.UserRegisterNameFragmentBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

/**
 * Created by ivasil on 1/26/2022
 */

class UserRegisterNameFragment : Fragment(R.layout.user_register_name_fragment) {

    private val binding by viewBinding(UserRegisterNameFragmentBinding::bind)
    private val viewModel: UserViewModel by sharedViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.registerButton.setOnClickListener {
            if(binding.usernameEdit.text.isNullOrBlank()) {
                binding.usernameLayout.error = "Full name cannot be empty"
            }
            else {
                binding.usernameLayout.error = null
                viewModel.fullName = binding.usernameEdit.text.toString()
                //findNavController().navigate(UserRegisterNameFragmentDirections.actionUserRegisterFragmentToUserRegisterPINFragment())
            }
        }
    }
}