package com.example.whatdidyoudo.ui.main

import android.view.LayoutInflater
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
        val idView: TextView = binding.itemNumber
        val contentView: TextView = binding.content
        val status: CheckBox = binding.status

        override fun toString(): String {
            return super.toString() + " '" + contentView.text + "'"
        }
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
        holder.idView.text = item.text
        holder.contentView.text = dateFormatter.format(item.timestamp)
        holder.status.isChecked = item.isProductive
        holder.status.setOnCheckedChangeListener { _, isChecked ->
            onTaskClickListener.onChangeProductivity(item.apply { isProductive = isChecked })
        }
    }

    override fun getItemCount(): Int = values.size

    fun updateData(newDataList: List<Task>) {
        values = newDataList
        notifyDataSetChanged()
    }

}