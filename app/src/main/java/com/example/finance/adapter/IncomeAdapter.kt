package com.example.finance.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.finance.R
import com.example.finance.adapter.holder.IncomeHolder
import com.example.finance.database.model.FinanceModel

class IncomeAdapter(private var data: List<FinanceModel>) : RecyclerView.Adapter<IncomeHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IncomeHolder {
        return IncomeHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false))
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: IncomeHolder, position: Int) {
        holder.setData(data[position])
    }
}