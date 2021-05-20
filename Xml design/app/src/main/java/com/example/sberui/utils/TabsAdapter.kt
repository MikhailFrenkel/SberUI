package com.example.sberui.utils

import android.app.Activity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.sberui.ProfileFragment
import com.example.sberui.SettingsFragment

class TabsAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment = when (position) {
        0 -> ProfileFragment()
        else -> SettingsFragment()
    }
}