package com.example.whatdidyoudo.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.whatdidyoudo.databases.Task
import com.example.whatdidyoudo.databinding.FragmentItemBinding
import java.text.SimpleDateFormat
import java.util.*

class TaskRecyclerViewAdapter(
    private val onTaskClickListener: OnTaskClickListener,
    private var values: List<Task>
) : RecyclerView.Adapter<TaskRecyclerViewAdapter.ViewHolder>() {

    interface OnTaskClickListener {
        fun onChangeProductivity(task: Task)
    }

    private val dateFormatter = SimpleDateFormat("HH:mm", Locale.getDefault())

    inner class ViewHolder(binding: FragmentItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val content: TextView = binding.content
        val timestamp: TextView = binding.timestamp
        val status: CheckBox = binding.status
        val divider: View = binding.divider
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            FragmentItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.content.text = item.text
        holder.timestamp.text = dateFormatter.format(item.timestamp)
        holder.status.isChecked = item.isProductive
        holder.status.setOnCheckedChangeListener { _, isChecked ->
            onTaskClickListener.onChangeProductivity(item.apply { isProductive = isChecked })
        }
        holder.divider.visibility = if (position == values.size - 1) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    override fun getItemCount(): Int = values.size

    fun updateData(newDataList: List<Task>) {
        values = newDataList
        notifyDataSetChanged()
    }

}