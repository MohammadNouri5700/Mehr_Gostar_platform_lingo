package com.android.platform.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.android.platform.ui.main.MainViewModel
import dagger.android.support.DaggerFragment
import javax.inject.Inject


class HomeFragment : DaggerFragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        val binding = FragmentSampleBinding.inflate(inflater, container, false)
//
//        viewModel.data.observe(viewLifecycleOwner, Observer { data ->
//            binding.textViewData.text = data
//        })
//
//        binding.buttonLoadData.setOnClickListener {
//            viewModel.loadData()
//        }

//        return binding.root
        return null
    }


}