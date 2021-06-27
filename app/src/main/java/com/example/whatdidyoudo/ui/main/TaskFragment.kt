package com.example.whatdidyoudo.ui.main

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
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

    companion object {
        fun newInstance() = TaskFragment()
    }

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

        setHasOptionsMenu(true)
        val rootView = binding.root

        initializeRecyclerView(rootView)

        viewModel.liveData.observe(viewLifecycleOwner, {
            recyclerAdapter.updateData(it.toList())
        })

        setEndIconListener(rootView)

        return rootView
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.task_menu, menu)
        viewModel.currentDateString.observe(viewLifecycleOwner) {
            menu.findItem(R.id.date_string).title = it
        }
        menu.findItem(R.id.date_button).setOnMenuItemClickListener {
            showDatePickerDialog()
            true
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun initializeRecyclerView(view: View) {
        recyclerView = view.findViewById(R.id.list)
        recyclerAdapter =
            TaskRecyclerViewAdapter(this, viewModel.liveData.value?.toMutableList() ?: emptyList())
        recyclerManager = LinearLayoutManager(context)
        with(recyclerView) {
            layoutManager = recyclerManager
            adapter = recyclerAdapter
        }
    }

    private fun showDatePickerDialog() {
        DatePickerDialog(
            requireContext(),
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
        ).apply {
            datePicker.minDate = viewModel.minimalDate.time
            datePicker.maxDate = System.currentTimeMillis()
        }.show()
    }

    private fun setEndIconListener(rootView: View) {
        val textInputLayout = rootView.findViewById<TextInputLayout>(R.id.text_input_layout)
        val textInputEditText = rootView.findViewById<TextInputEditText>(R.id.text_input_edit_text)
        textInputLayout.setEndIconOnClickListener {
            textInputEditText.text?.let {
                if (it.isNotBlank()) {
                    viewModel.addTask(Task(Date(), it.toString()))
                    it.clear()
                }
            }
        }
    }

    override fun onChangeProductivity(task: Task) {
        viewModel.updateTask(task)
    }
}