package com.example.whatdidyoudo.ui.main

import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.PopupMenu
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.whatdidyoudo.R
import com.example.whatdidyoudo.databases.Task
import com.example.whatdidyoudo.databinding.FragmentItemBinding
import java.text.SimpleDateFormat
import java.util.*

class TaskRecyclerViewAdapter(
    private val onTaskClickListener: OnTaskClickListener,
    private var taskList: List<Task>
) : RecyclerView.Adapter<TaskRecyclerViewAdapter.ViewHolder>() {

    interface OnTaskClickListener {
        fun onChangeProductivity(task: Task)
    }

    private val dateFormatter = SimpleDateFormat("HH:mm", Locale.getDefault())

    inner class ViewHolder(binding: FragmentItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val container: ConstraintLayout = binding.taskItemContainer
        val content: TextView = binding.content
        val timestamp: TextView = binding.timestamp
        val statusImage: ImageView = binding.statusImage
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
        val item = taskList[position]
        holder.content.text = item.text
        holder.timestamp.text = dateFormatter.format(item.timestamp)
        holder.statusImage.setImageResource(chooseImageResource(item.isProductive))
        holder.status.setOnCheckedChangeListener(null)
        holder.status.isChecked = item.isProductive
        holder.status.setOnCheckedChangeListener { _, isChecked ->
            onTaskClickListener.onChangeProductivity(Task(item.timestamp, item.text, isChecked))
        }
        holder.divider.visibility = if (position == taskList.size - 1) {
            View.GONE
        } else {
            View.VISIBLE
        }

        holder.container.setOnLongClickListener {
            showPopup(it)
            true
        }
    }

    override fun getItemCount(): Int = taskList.size

    fun updateData(newTaskList: List<Task>) {
        val added = (taskList.size until newTaskList.size)
        val changed =
            taskList.mapIndexedNotNull { index, task -> if (newTaskList.size > index && task != newTaskList[index]) index else null }
        val removed = (newTaskList.size until taskList.size).reversed()

        added.forEach { notifyItemInserted(it) }
        changed.forEach { notifyItemChanged(it) }
        removed.forEach { notifyItemRemoved(it) }
        taskList = newTaskList
    }

    private fun chooseImageResource(isChecked: Boolean) =
        if (isChecked) R.drawable.hard_work_hat else R.drawable.coffee

    private fun showPopup(v: View) {
        val popup = PopupMenu(v.context, v)
        val inflater: MenuInflater = popup.menuInflater
        inflater.inflate(R.menu.task_item_menu, popup.menu)
        popup.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.remove_menu_option -> {
//todo remove item
                    true
                }
                else -> false
            }
        }
        popup.show()
    }

}