package com.example.finance.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.finance.ui.expenses.ExpensesFragment
import com.example.finance.ui.income.IncomeFragment

class HomePageAdapter (activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = ExpensesFragment()
            1 -> fragment = IncomeFragment()
        }
        return fragment as Fragment
    }
}