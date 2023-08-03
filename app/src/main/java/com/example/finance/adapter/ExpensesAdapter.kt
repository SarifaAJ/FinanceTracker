package com.example.finance.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.finance.R
import com.example.finance.adapter.holder.ExpensesHolder
import com.example.finance.database.model.FinanceModel

class ExpensesAdapter: ListAdapter<FinanceModel, ExpensesHolder>(FinanceModelDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpensesHolder {
        return ExpensesHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false))
    }

    override fun onBindViewHolder(holder: ExpensesHolder, position: Int) {
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
