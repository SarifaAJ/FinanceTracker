package com.example.finance.ui.expenses

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finance.adapter.ExpensesAdapter
import com.example.finance.database.MyApp.Companion.db
import com.example.finance.databinding.FragmentExpensesBinding

class ExpensesFragment : Fragment() {
    private lateinit var binding: FragmentExpensesBinding

    // database
    private lateinit var adapter : ExpensesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentExpensesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val expensesDao = db.financeDao()
        val expensesList = expensesDao?.getAllExpenses()

        adapter = ExpensesAdapter()

        val recyclerView: RecyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        // Submit the list of expenses to the adapter using submitList
        adapter.submitList(expensesList)
    }

    override fun onResume() {
        super.onResume()

        // Refresh data when the fragment is resumed
        val expensesDao = db.financeDao()
        val expensesList = expensesDao?.getAllExpenses()

        adapter.submitList(expensesList)
    }
}