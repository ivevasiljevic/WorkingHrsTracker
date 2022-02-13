package com.vasha.workhrstracker.features.scanner

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.vasha.workhrstracker.R
import com.vasha.workhrstracker.databinding.LogsFragmentBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class LogsFragment : Fragment(R.layout.logs_fragment), SearchView.OnQueryTextListener, EmployeesAdapter.OnItemClickListener {

    private val binding by viewBinding(LogsFragmentBinding::bind)
    private val viewModel: ScannerViewModel by sharedViewModel()

    private val employeesAdapter = EmployeesAdapter(this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getEmployees()
        binding.recList.apply {
            adapter = employeesAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        binding.searchView.setOnQueryTextListener(this@LogsFragment)

        binding.tryAgainButton.setOnClickListener {
            viewModel.getEmployees()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.scannerUiState.collect {
                    when(it) {
                        is ScannerViewModel.ScannerUiState.EmployeesState.Success -> {
                            binding.shimmer.stopShimmer()
                            binding.shimmer.isVisible = false
                            binding.messageContainer.isVisible = false
                            binding.recList.isVisible = true
                            employeesAdapter.submitList(it.employees) {
                                viewModel.saveFullEmployeeList(it.employees)
                            }
                        }
                        ScannerViewModel.ScannerUiState.EmployeesState.Error -> {
                            binding.shimmer.stopShimmer()
                            binding.shimmer.isVisible = false
                            binding.recList.isVisible = false
                            binding.messageContainer.isVisible = true
                            binding.tryAgainButton.isVisible = true
                            binding.message.text = "Something went wrong. Please try again."
                            binding.animationView.setAnimation(R.raw.error_animation)
                        }
                        ScannerViewModel.ScannerUiState.EmployeesState.Loading -> {
                            binding.shimmer.isVisible = true
                            binding.recList.isVisible = false
                            binding.messageContainer.isVisible = false
                            binding.shimmer.startShimmer()
                        }
                    }
                }
            }
        }

        viewModel.fullEmployeeListState.observe(viewLifecycleOwner) {
            if(it != null) {
                if(it.isEmpty()) {
                    binding.shimmer.isVisible = false
                    binding.recList.isVisible = false
                    binding.messageContainer.isVisible = true
                    binding.animationView.setAnimation(R.raw.empty_animation)
                    binding.animationView.playAnimation()
                    binding.animationView.loop(true)
                    binding.message.text = "That employee doesn't exist. Please try another."
                    binding.tryAgainButton.isVisible = false
                }
                else {
                    binding.shimmer.isVisible = false
                    binding.recList.isVisible = true
                    binding.messageContainer.isVisible = false
                    employeesAdapter.submitList(it)
                }
            }
        }
    }

    override fun onItemClicked(id: String, fullName: String) {
        findNavController().navigate(ScannerMenuFragmentDirections.actionScannerMenuFragmentToEmployeeLogsFragment(id, fullName))
    }

    override fun onQueryTextSubmit(p0: String?): Boolean {
        //do nothing
        return false
    }

    override fun onQueryTextChange(query: String?): Boolean {
        viewModel.filterEmployeeList(query.toString())
        return true
    }
}