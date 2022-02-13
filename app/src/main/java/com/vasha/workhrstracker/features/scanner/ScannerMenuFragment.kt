package com.vasha.workhrstracker.features.scanner

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.vasha.workhrstracker.R
import com.vasha.workhrstracker.databinding.ScannerMenuFragmentBinding
import com.vasha.workhrstracker.features.scanner.ViewPagerAdapter.Companion.LOGS_FRAGMENT
import com.vasha.workhrstracker.features.scanner.ViewPagerAdapter.Companion.MENU_FRAGMENT

/**
 * Created by ivasil on 1/25/2022
 */

class ScannerMenuFragment : Fragment(R.layout.scanner_menu_fragment) {

    private val binding by viewBinding(ScannerMenuFragmentBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.viewpager.apply {
            adapter = ViewPagerAdapter(requireActivity())
        }

        TabLayoutMediator(binding.tabs, binding.viewpager) { tab, position ->
            when(position) {
                MENU_FRAGMENT -> {
                    tab.text = "Main menu"
                }
                LOGS_FRAGMENT -> {
                    tab.text = "User logs"
                }
            }
        }.attach()
    }
}