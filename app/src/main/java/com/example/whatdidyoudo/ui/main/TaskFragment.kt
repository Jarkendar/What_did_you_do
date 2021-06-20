package com.example.whatdidyoudo.ui.main

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.whatdidyoudo.R
import com.example.whatdidyoudo.databases.Task
import com.example.whatdidyoudo.databinding.FragmentItemListBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*


class TaskFragment : Fragment(), TaskRecyclerViewAdapter.OnTaskClickListener {

    private val viewModel: TaskFragmentViewModel by viewModel()

    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerAdapter: TaskRecyclerViewAdapter
    private lateinit var recyclerManager: RecyclerView.LayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentItemListBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_item_list, container, false)

        binding.viewModel = this.viewModel
        binding.lifecycleOwner = this

        val view = binding.root

        recyclerView = view.findViewById(R.id.list)
        recyclerAdapter =
            TaskRecyclerViewAdapter(this, viewModel.liveData.value?.toMutableList() ?: emptyList())
        recyclerManager = LinearLayoutManager(context)
        with(recyclerView) {
            layoutManager = recyclerManager
            adapter = recyclerAdapter
        }

        viewModel.liveData.observe(viewLifecycleOwner, {
            recyclerAdapter.updateData(it.toList())
        })

        val textInputLayout = view.findViewById<TextInputLayout>(R.id.text_input_layout)
        val textInputEditText = view.findViewById<TextInputEditText>(R.id.text_input_edit_text)
        textInputLayout.setEndIconOnClickListener {
            textInputEditText.text?.let {
                if (it.isNotBlank()) {
                    viewModel.addTask(Task(Date(), it.toString()))
                    it.clear()
                }
            }
        }

        view.findViewById<Button>(R.id.button).setOnClickListener {
            DatePickerDialog(requireContext(),
                { _, year, month, dayOfMonth ->
                    viewModel.changeDate(Calendar.getInstance().apply {
                        set(Calendar.YEAR, year)
                        set(Calendar.MONTH, month)
                        set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    }.time)
                },
                viewModel.getSelectedYear(),
                viewModel.getSelectedMonth(),
                viewModel.getSelectedDayOfMonth()
            )
                .show()
        }
        return view
    }

    override fun onChangeProductivity(task: Task) {
        viewModel.updateTask(task)
    }

    companion object {
        fun newInstance() = TaskFragment()
    }
}