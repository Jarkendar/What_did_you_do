package com.example.whatdidyoudo.ui.main

import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.whatdidyoudo.R

class RowAdapter(private var dataList: ArrayList<Task>): RecyclerView.Adapter<RowAdapter.ViewHolder>() {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val text: TextView
        val time: TextView
        val status: ImageView

        init {
            with(view) {
                text = findViewById(R.id.row_text)
                time = findViewById(R.id.row_time)
                status = findViewById(R.id.row_status_image)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_list_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            val item = dataList[position]
            text.text = item.text
            time.text = DateFormat.format("HH:mm", item.timestamp)
            status.setImageResource(if (item.isProductive) {
                R.drawable.add
            } else {
                R.drawable.ic_launcher_foreground
            })
        }
    }

    override fun getItemCount() = dataList.size

    public fun updateData(newDataList: ArrayList<Task>) {
        dataList = newDataList
        this.notifyDataSetChanged()
    }
}