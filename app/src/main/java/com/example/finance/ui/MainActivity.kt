package com.example.finance.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.example.finance.R
import com.example.finance.adapter.OnBoardingViewPagerAdapter
import com.example.finance.database.dataclass.OnBoardingData
import com.example.finance.databinding.ActivityMainBinding
import com.example.finance.ui.login.EnterActivity
import com.example.finance.ui.login.LoginActivity
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var onBoardingViewPager: ViewPager
    private lateinit var onBoardingViewPagerAdapter: OnBoardingViewPagerAdapter
    private lateinit var tabLayout: TabLayout
    private var position = 0
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // share preferences
        sharedPreferences = applicationContext.getSharedPreferences("pref", Context.MODE_PRIVATE)

        if (restorePrefData()) {
            // Check the switch state from SharedPreferences
            val switchState = sharedPreferences.getBoolean("switchState", false)

            if (switchState) {
                // Switch is ON, go to LoginActivity
                val intent = Intent(applicationContext, LoginActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                // Switch is OFF, go to EnterActivity
                val intent = Intent(applicationContext, EnterActivity::class.java)
                startActivity(intent)
                finish()
            }
        } else {
            setPrefData()

            // on boarding appearance
            val onBoardingData: MutableList<OnBoardingData> = ArrayList()
            onBoardingData.add(
                OnBoardingData(
                    "Welcome to Financial Tracker",
                    "Start your journey of financial organization and management with our intuitive and powerful app.",
                    R.drawable.onboard_1
                )
            )
            onBoardingData.add(
                OnBoardingData(
                    "Track Your Income and Expenses",
                    "Effortlessly record and categorize your income and expenses. Our app allows you to easily track and analyze your financial transactions.",
                    R.drawable.onboard_2
                )
            )
            onBoardingData.add(
                OnBoardingData(
                    "Take Control of Your Finances",
                    "Get started on your path to financial success! Sign up or log in to begin managing your finances effectively with our Financial Tracker app.",
                    R.drawable.onboard_3
                )
            )
            onBoardingViewPager = binding.screenPager
            tabLayout = binding.tabLayout

            setOnBoardingViewPagerAdapter(onBoardingData)

            binding.next.setOnClickListener {
                if (position < onBoardingData.size) {
                    position++
                    onBoardingViewPager.currentItem = position
                }
                if (position == onBoardingData.size) {
                    val intent = Intent(applicationContext, LoginActivity::class.java)
                    startActivity(intent)
                }
            }

            tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    position = tab?.position ?: 0
                    if (tab?.position == onBoardingData.size - 1) {
                        binding.next.text = getString(R.string.get_started)
                    } else {
                        binding.next.text = getString(R.string.next)
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                    // Do nothing
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                    // Do nothing
                }
            })
        }
    }

    private fun setOnBoardingViewPagerAdapter(onBoardingData: List<OnBoardingData>) {
        onBoardingViewPagerAdapter = OnBoardingViewPagerAdapter(this, onBoardingData)
        onBoardingViewPager.adapter = onBoardingViewPagerAdapter
        tabLayout.setupWithViewPager(onBoardingViewPager)
    }

    private fun setPrefData() {
        sharedPreferences.edit()
            .putBoolean("isFirstTimeRun", true)
            .apply()
    }

    private fun restorePrefData(): Boolean {
        return sharedPreferences.getBoolean("isFirstTimeRun", false)
    }
}
