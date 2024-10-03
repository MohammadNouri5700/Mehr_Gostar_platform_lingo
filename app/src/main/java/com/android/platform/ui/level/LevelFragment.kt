package com.android.platform.ui.level

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.platform.PlatformApplication
import com.android.platform.R
import com.android.platform.databinding.FragmentLevelBinding
import com.android.platform.ui.level.levels.Level
import com.android.platform.ui.level.levels.LevelAdapter
import com.android.platform.ui.level.task.Task
import com.android.platform.ui.level.task.TaskAdapter
import com.android.platform.utils.ui.CircleProgressBar
import javax.inject.Inject


class LevelFragment : Fragment() {


    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: LevelViewModel

    private lateinit var binding: FragmentLevelBinding

    private lateinit var taskAdapter: TaskAdapter
    private lateinit var levelAdapter: LevelAdapter



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_level, container, false)
        (requireActivity().application as PlatformApplication).appComponent.inject(this)
        viewModel = ViewModelProvider(this, viewModelFactory)[LevelViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        binding.recTask.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        val imageList = listOf<Task>(Task(1,"dvd","2"),Task(1,"dvd","2"),Task(1,"dvd","2")) // Replace with your images
        taskAdapter = TaskAdapter(imageList,requireContext())
        binding.recTask.adapter=taskAdapter


        binding.recLevel.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        val imageListc = listOf<Level>(Level(1,"dvd",37),Level(1,"dvd",24),Level(1,"dvd",6)) // Replace with your images
        levelAdapter = LevelAdapter(imageListc,requireContext())
        binding.recLevel.adapter=levelAdapter
    }


}