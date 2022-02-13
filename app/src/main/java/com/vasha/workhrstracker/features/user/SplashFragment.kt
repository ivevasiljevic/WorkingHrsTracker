package com.vasha.workhrstracker.features.user

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.vasha.workhrstracker.R
import com.vasha.workhrstracker.data.PreferencesManager
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

/**
 * Created by ivasil on 1/26/2022
 */

class SplashFragment : Fragment(R.layout.splash_fragment) {

    private val viewModel: UserViewModel by sharedViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                viewModel.userNavigationState.collect {
                    when(it) {
                        UserViewModel.UserState.UserNavigation.Empty -> {
                            //do nothing
                        }
                        UserViewModel.UserState.UserNavigation.NavigateToLoginScreen -> {
                            //findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToUserLoginFragment())
                        }
                        UserViewModel.UserState.UserNavigation.NavigateToRegisterScreen -> {
                            //findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToUserRegisterFragment())
                        }
                    }
                }
            }
        }
    }
}