package com.example.finance.ui.income

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finance.adapter.IncomeAdapter
import com.example.finance.database.MyApp.Companion.db
import com.example.finance.databinding.FragmentIncomeBinding

class IncomeFragment : Fragment() {
    private lateinit var binding: FragmentIncomeBinding
    private lateinit var adapter: IncomeAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentIncomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val incomeDao = db.financeDao()
        val incomeList = incomeDao?.getAllIncome()

        adapter = IncomeAdapter(incomeList!!)
        val recyclerView: RecyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
    }

    override fun onResume() {
        super.onResume()

        // Refresh data when the fragment is resumed
        val incomeDao = db.financeDao()
        val incomeList = incomeDao?.getAllIncome()

        adapter.updateData(incomeList!!)
    }
}