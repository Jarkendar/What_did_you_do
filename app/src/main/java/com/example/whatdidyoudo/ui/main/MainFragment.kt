package com.example.whatdidyoudo.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.whatdidyoudo.R
import com.example.whatdidyoudo.databinding.MainFragmentBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private var recyclerView: RecyclerView? = null
    private val viewModel: MainViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.main_fragment, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = this.view?.findViewById(R.id.recycler_view)
        recyclerView?.adapter = RowAdapter(viewModel.taskList)
        val binding: MainFragmentBinding = DataBindingUtil.setContentView(this.requireActivity(), R.layout.main_fragment)
        binding.viewModel = this.viewModel
        binding.lifecycleOwner = this
    }
}