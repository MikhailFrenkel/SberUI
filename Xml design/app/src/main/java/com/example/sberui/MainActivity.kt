package com.example.sberui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import com.example.sberui.databinding.ActivityMainBinding
import com.example.sberui.utils.TabsAdapter
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlin.math.abs

class MainActivity : AppCompatActivity(), AppBarLayout.OnOffsetChangedListener {
    private val PERCENTAGE_TO_ANIMATE_AVATAR = 20
    private var maxScrollSize: Int = 0
    private var isAvatarShown = false

    private lateinit var binding: ActivityMainBinding
    private lateinit var tabsAdapter: TabsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            appBar.addOnOffsetChangedListener(this@MainActivity)
            maxScrollSize = appBar.totalScrollRange
        }

        initializeTabs()
    }

    private fun initializeTabs() {
        tabsAdapter = TabsAdapter(this)
        binding.viewPager.adapter = tabsAdapter
        binding.viewPager.isUserInputEnabled = false
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when(position) {
                0 -> "Профиль"
                1 -> "Настройки"
                else -> "Undefined"
            }
        }.attach()

        // https://stackoverflow.com/questions/36557801/coordinatorlayout-appbarlayout-viewpager-not-resize-child-layout
        binding.viewPager.post {
            binding.viewPager.requestLayout()
        }
    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
        if (maxScrollSize == 0)
            maxScrollSize = appBarLayout?.totalScrollRange ?: 0

        maxScrollSize = if (maxScrollSize == 0) 1 else maxScrollSize

        val percentage = (abs(verticalOffset)) * 100 / maxScrollSize;

        if (percentage >= PERCENTAGE_TO_ANIMATE_AVATAR && isAvatarShown) {
            isAvatarShown = false;

            binding.profileImageCard.animate()
                .scaleY(0f).scaleX(0f)
                .setDuration(200)
                .start();
        }

        if (percentage <= PERCENTAGE_TO_ANIMATE_AVATAR && !isAvatarShown) {
            isAvatarShown = true;

            binding.profileImageCard.animate()
                .scaleY(1f).scaleX(1f)
                .start();
        }
    }
}