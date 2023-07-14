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
import java.text.NumberFormat
import java.util.Locale

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

        // view pager
        viewPager2 = binding.viewPager2

        val homePageAdapter = HomePageAdapter(this)
        viewPager2.adapter = homePageAdapter

        // tab layout
        tabLayout = binding.tabLayout

        TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        supportActionBar?.elevation = 0f

        // to show the amount of a nominal data
        val financeDao = db.financeDao()
        val totalNominal = financeDao?.getTotalNominal()
        val totalExpenses = financeDao?.getTotalExpenses()
        val totalIncome = financeDao?.getTotalIncome()

        val formatter = NumberFormat.getCurrencyInstance(Locale("in", "ID")) // currency format
        val formattedTotalNominal = formatter.format(totalNominal)
        val formattedTotalExpenses = formatter.format(totalExpenses)
        val formattedTotalIncome = formatter.format(totalIncome)

        binding.edtTotal.text = formattedTotalNominal
        binding.edtPengeluaran.text = formattedTotalExpenses
        binding.edtPemasukan.text = formattedTotalIncome
    }
}