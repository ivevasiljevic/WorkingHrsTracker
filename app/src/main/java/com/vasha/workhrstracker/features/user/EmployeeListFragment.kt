package com.vasha.workhrstracker.features.user

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import by.kirich1409.viewbindingdelegate.viewBinding
import com.vasha.workhrstracker.R
import com.vasha.workhrstracker.databinding.EmployeeListFragmentBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

/**
 * Created by ivasil on 1/27/2022
 */

class EmployeeListFragment : Fragment(R.layout.employee_list_fragment), SwipeRefreshLayout.OnRefreshListener {

    private val binding by viewBinding(EmployeeListFragmentBinding::bind)
    private val viewModel: UserViewModel by sharedViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.swipe.setOnRefreshListener(this)
        val listAdapter = Adapter()

        binding.recList.apply {
            adapter = listAdapter
        }

        viewModel.getLogs()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                viewModel.userUiState.collect {
                    when(it) {
                        UserViewModel.UserState.Empty -> {
                            //do nothing
                        }
                        UserViewModel.UserState.Loading -> {
                            binding.swipe.isRefreshing = false
                            binding.progressBar.isVisible = true
                        }
                        is UserViewModel.UserState.ShowUserData -> {
                            //do nothing
                        }
                        is UserViewModel.UserState.UserFailed -> {
                            binding.progressBar.isVisible = false
                        }
                        is UserViewModel.UserState.UserSuccessful -> {
                            //do nothing
                        }
                        is UserViewModel.UserState.LogsSuccessful -> {
                            binding.progressBar.isVisible = false
                            listAdapter.submitList(it.list)
                        }
                    }
                }
            }
        }
    }

    override fun onRefresh() {
        viewModel.getLogs()
    }
}