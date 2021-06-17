package com.example.whatdidyoudo.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.whatdidyoudo.R
import com.example.whatdidyoudo.databinding.FragmentItemListBinding
import org.koin.androidx.viewmodel.ext.android.viewModel


class TaskFragment : Fragment() {

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
        recyclerAdapter = TaskRecyclerViewAdapter(viewModel.taskLiveData.value!!.toMutableList())
        recyclerManager = LinearLayoutManager(context)
        with(recyclerView) {
            layoutManager = recyclerManager
            adapter = recyclerAdapter
        }

        viewModel.taskLiveData.observe(viewLifecycleOwner, {
            recyclerAdapter.updateData(it.toList())
        })
        return view
    }

    companion object {
        fun newInstance() = TaskFragment()
    }
}