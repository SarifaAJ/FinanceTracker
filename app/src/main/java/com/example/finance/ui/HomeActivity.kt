package com.example.finance.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.viewpager2.widget.ViewPager2
import com.example.finance.R
import com.example.finance.adapter.HomePageAdapter
import com.example.finance.database.MyApp.Companion.db
import com.example.finance.databinding.ActivityHomeBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager2: ViewPager2

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.expenses,
            R.string.income
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //add note button
        binding.FABAdd.setOnClickListener {
            val moveIntent = Intent(this@HomeActivity, AddNotesActivity::class.java)
            startActivity(moveIntent)
        }

        tabLayout = binding.tabLayout
        viewPager2 = binding.viewPager2

        val homePageAdapter = HomePageAdapter(this)
        viewPager2.adapter = homePageAdapter

        TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        supportActionBar?.elevation = 0f

        val financeDao = db.financeDao()
        val totalNominal = financeDao?.getTotalNominal()
        val totalExpenses = financeDao?.getTotalExpenses()
        val totalIncome = financeDao?.getTotalIncome()

        binding.edtTotal.text = totalNominal.toString()
        binding.edtPengeluaran.text = totalExpenses.toString()
        binding.edtPemasukan.text = totalIncome.toString()
    }
}