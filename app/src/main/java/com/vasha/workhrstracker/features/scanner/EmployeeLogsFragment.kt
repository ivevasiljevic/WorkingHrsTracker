package com.vasha.workhrstracker.features.scanner

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.vasha.workhrstracker.R
import com.vasha.workhrstracker.databinding.EmployeeLogsFragmentBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class EmployeeLogsFragment : Fragment(R.layout.employee_logs_fragment) {

    private val binding by viewBinding(EmployeeLogsFragmentBinding::bind)
    private val viewModel: ScannerViewModel by sharedViewModel()
    private val args: EmployeeLogsFragmentArgs by navArgs()
    private val logsAdapter = LogsAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.toolbar.apply {
            setupWithNavController(navController, appBarConfiguration)
            title = "${args.employeeName}'s Logs"
            navigationIcon = ResourcesCompat.getDrawable(resources, R.drawable.ic_back, null)
            inflateMenu(R.menu.sort_logs_menu)
            setOnMenuItemClickListener {
                when(it.itemId) {
                    R.id.action_sort_by_type -> {
                        viewModel.sortEmployeeLogsList(SortType.TYPE_SORT)
                        true
                    }
                    R.id.action_sort_by_timestamp -> {
                        viewModel.sortEmployeeLogsList(SortType.TIMESTAMP_SORT)
                        true
                    }
                    else -> super.onOptionsItemSelected(it)
                }
            }
        }

        viewModel.getEmployeeLogs(args.employeeId)

        binding.recList.apply {
            adapter = logsAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        binding.tryAgainButton.setOnClickListener {
            viewModel.getEmployeeLogs(args.employeeId)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.scannerUiState.collectLatest {
                    when(it) {
                        is ScannerViewModel.ScannerUiState.LogsError -> {
                            binding.shimmer.stopShimmer()
                            binding.shimmer.isVisible = false
                            binding.recList.isVisible = false
                            binding.messageContainer.isVisible = true

                            if(it.isSoftException) {
                                binding.tryAgainButton.isVisible = false
                                binding.message.text = it.errorMessage
                                binding.animationView.setAnimation(R.raw.empty_animation)
                                binding.animationView.playAnimation()
                                binding.animationView.loop(true)
                            }
                            else {
                                binding.tryAgainButton.isVisible = true
                                binding.message.text = "Something went wrong. Please try again."
                                binding.animationView.setAnimation(R.raw.error_animation)
                            }
                        }
                        ScannerViewModel.ScannerUiState.LogsLoading -> {
                            binding.shimmer.isVisible = true
                            binding.recList.isVisible = false
                            binding.messageContainer.isVisible = false
                            binding.shimmer.startShimmer()
                        }
                        is ScannerViewModel.ScannerUiState.LogsSuccess -> {
                            binding.shimmer.stopShimmer()
                            binding.shimmer.isVisible = false
                            binding.messageContainer.isVisible = false
                            binding.recList.isVisible = true
                            logsAdapter.submitList(it.logs) {
                                viewModel.saveFullEmployeeLogsList(it.logs)
                            }
                        }
                    }
                }
            }
        }

        viewModel.fullEmployeeLogsListState.observe(viewLifecycleOwner) {
            if(it != null) {
                logsAdapter.submitList(it)
            }
        }

        setHasOptionsMenu(true)
    }
}