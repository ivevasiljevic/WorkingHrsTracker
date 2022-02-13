package com.vasha.workhrstracker.features.scanner

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.vasha.workhrstracker.R
import com.vasha.workhrstracker.databinding.MenuFragmentBinding

class MenuFragment : Fragment(R.layout.menu_fragment), View.OnClickListener {

    private val binding by viewBinding(MenuFragmentBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.arrivalButton.setOnClickListener(this)
        binding.departureButton.setOnClickListener(this)
        binding.lunchStartButton.setOnClickListener(this)
        binding.lunchEndButton.setOnClickListener(this)
    }

    override fun onClick(buttonView: View?) {

        val activityType = when(buttonView?.id) {
            R.id.arrival_button -> {
                ActivityType.ARRIVAL
            }
            R.id.departure_button -> {
                ActivityType.DEPARTURE
            }
            R.id.lunch_start_button -> {
                ActivityType.BREAK_START
            }
            R.id.lunch_end_button -> {
                ActivityType.BREAK_END
            }
            else -> {
                ActivityType.ARRIVAL
            }
        }

        //Send activity type
        findNavController().navigate(ScannerMenuFragmentDirections.actionScannerMenuFragmentToScannerCameraFragment(activityType))
    }
}