package com.example.finance.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.viewpager2.widget.ViewPager2
import com.example.finance.R
import com.example.finance.adapter.HomePageAdapter
import com.example.finance.database.MyApp.Companion.db
import com.example.finance.databinding.ActivityHomeBinding
import com.example.finance.ui.dialog.MonthYearPickerDialog
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding

    // tab and view pager
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager2: ViewPager2

    // username
    private lateinit var username: String

    // calendar
    private val calendar: Calendar = Calendar.getInstance()
    private var selectedYear: Int = calendar.get(Calendar.YEAR)
    private var selectedMonth: Int = calendar.get(Calendar.MONTH)

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.income,
            R.string.expenses
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

        //setting button
        binding.setting.setOnClickListener {
            val moveIntent = Intent(this@HomeActivity, SettingActivity::class.java)
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

        // Mendapatkan data username dari SharedPreferences
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        username = sharedPreferences.getString("username", "") ?: ""

        // Mengatur nilai username ke edt_username
        binding.edtUsername.text = username

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

        // Set the initial value of tv_date to the current date
        val initialDate = getFormattedDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH))
        binding.tvDate.text = initialDate

        // Show the month-year picker when the button is clicked
        binding.btnMonthPicker.setOnClickListener {
            showMonthYearPicker()
        }
    }

    // monthYearPicker
    private fun showMonthYearPicker() {
        val monthYearPickerDialog = MonthYearPickerDialog(
            this,
            object : MonthYearPickerDialog.OnDateSetListener {
                override fun onDateSet(year: Int, month: Int) {
                    // Update the selected year and month
                    selectedYear = year
                    selectedMonth = month

                    // Update the date displayed in the TextView
                    binding.tvDate.text = getFormattedDate(selectedYear, selectedMonth)

                    // Calculate and display the total nominal, total expenses, and total income for the selected month
                    val formattedSelectedMonth = String.format("%04d-%02d", selectedYear, selectedMonth + 1)
                    updateTotalAmounts(formattedSelectedMonth)
                }
            },
            calendar // Pass the current calendar instance to initialize the picker
        )
        monthYearPickerDialog.show()
    }

    private fun updateTotalAmounts(yearMonth: String) {
        val financeDao = db.financeDao()

        val totalNominal = financeDao?.getTotalNominalByMonth(yearMonth)
        val totalExpenses = financeDao?.getTotalExpensesByMonth(yearMonth)
        val totalIncome = financeDao?.getTotalIncomeByMonth(yearMonth)

        val formatter = NumberFormat.getCurrencyInstance(Locale("in", "ID")) // currency format
        val formattedTotalNominal = formatter.format(totalNominal)
        val formattedTotalExpenses = formatter.format(totalExpenses)
        val formattedTotalIncome = formatter.format(totalIncome)

        binding.edtTotal.text = formattedTotalNominal
        binding.edtPengeluaran.text = formattedTotalExpenses
        binding.edtPemasukan.text = formattedTotalIncome
    }

    private fun getFormattedDate(year: Int, month: Int): String {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, 1)

        val simpleDateFormat = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
        return simpleDateFormat.format(calendar.time)
    }
}
