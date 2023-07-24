package com.example.finance.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.finance.R
import com.example.finance.adapter.holder.ExpensesHolder
import com.example.finance.database.model.FinanceModel

class ExpensesAdapter(private var data: List<FinanceModel>) : RecyclerView.Adapter<ExpensesHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpensesHolder {
        return ExpensesHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false))
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ExpensesHolder, position: Int) {
        holder.setData(data[position])
    }

    fun updateData(newExpensesList: List<FinanceModel>) {
        data = newExpensesList
        notifyDataSetChanged()
    }

}