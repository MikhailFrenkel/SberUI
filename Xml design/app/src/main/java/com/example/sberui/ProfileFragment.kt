package com.example.sberui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.example.sberui.databinding.FragmentProfileBinding
import com.example.sberui.utils.*
import com.google.android.material.chip.Chip

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        with(binding){
            val subscriptionsAdapter = SubscriptionsAdapter()
            subscriptionsAdapter.submitList(subscriptionsList)
            subscriptions.adapter = subscriptionsAdapter

            val limitsAdapter = LimitsAdapter()
            limitsAdapter.submitList(limitsList)
            limits.adapter = limitsAdapter

            chipGroup.removeAllViews()
            chipsList.forEach {
                val chip = Chip(context).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT)
                    text = it
                    setEnsureMinTouchTargetSize(false)
                }
                chipGroup.addView(chip)
            }
        }

        return binding.root
    }
}