package com.android.platform.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.android.platform.PlatformApplication
import com.android.platform.R
import com.android.platform.databinding.FragmentHomeBinding
import com.android.platform.ui.main.MainViewModel
import dagger.android.support.DaggerFragment
import javax.inject.Inject


class HomeFragment : Fragment() {


    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: HomeViewModel

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        (requireActivity().application as PlatformApplication).appComponent.inject(this)
        viewModel = ViewModelProvider(this, viewModelFactory)[HomeViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }


}