package com.vasha.workhrstracker.features.scanner

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import java.lang.Exception

class ViewPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    companion object {
        const val MENU_FRAGMENT = 0
        const val LOGS_FRAGMENT = 1
    }

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            MENU_FRAGMENT -> MenuFragment()
            LOGS_FRAGMENT -> LogsFragment()
            else -> throw FragmentNotFoundException()
        }
    }
}

class FragmentNotFoundException : Exception() {

    override val message: String?
        get() = super.message
}