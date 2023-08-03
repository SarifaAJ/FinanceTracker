package com.example.finance.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.finance.R
import com.example.finance.adapter.holder.IncomeHolder
import com.example.finance.database.model.FinanceModel

class IncomeAdapter: ListAdapter<FinanceModel, IncomeHolder>(FinanceModelDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IncomeHolder {
        return IncomeHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false))
    }

    override fun onBindViewHolder(holder: IncomeHolder, position: Int) {
        holder.setData(getItem(position))
    }

    class FinanceModelDiffCallback : DiffUtil.ItemCallback<FinanceModel>() {
        override fun areItemsTheSame(oldItem: FinanceModel, newItem: FinanceModel): Boolean {
            // Check if the items have the same ID
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: FinanceModel, newItem: FinanceModel): Boolean {
            // Check if the items' contents are the same
            return oldItem == newItem
        }
    }
}