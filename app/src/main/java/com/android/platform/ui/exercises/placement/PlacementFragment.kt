package com.android.platform.ui.exercises.placement


import android.content.ClipData
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.DragEvent
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.platform.ExerciseModel
import com.android.platform.PlatformApplication
import com.android.platform.R
import com.android.platform.databinding.FragmentHomeBinding
import com.android.platform.databinding.FragmentPlacementExerciseBinding
import com.android.platform.di.factory.CallQueueManager
import com.android.platform.di.factory.DragManager
import com.android.platform.di.module.CoroutineModule
import com.android.platform.repository.data.database.ImageDao
import com.android.platform.ui.exercises.ExerciseViewModel
import com.android.platform.ui.exercises.order.adapter.OrderListAdapter
import com.android.platform.ui.exercises.placement.adapter.AdapterTarget
import com.android.platform.ui.exercises.placement.adapter.PlacementListAdapter
import com.android.platform.ui.exercises.placement.data.addSelected
import com.android.platform.ui.home.story.PodcastAdapter
import com.android.platform.ui.home.story.StoryAdapter
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


class PlacementFragment @Inject constructor(val value: ExerciseModel) : Fragment() {

//[{"Title":"city","Word":"tehran + tabrix + qom"},{"Title":"country","Word":"germany + iran + japan"}]

    private lateinit var sharedViewModel: ExerciseViewModel
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: PlacementViewModel

    private lateinit var binding: FragmentPlacementExerciseBinding

    lateinit var targetAdapter: AdapterTarget
    lateinit var itemsAdapter: PlacementListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_placement_exercise, container, false
        )
        (requireActivity().application as PlatformApplication).appComponent.inject(this)
        viewModel = ViewModelProvider(this, viewModelFactory)[PlacementViewModel::class.java]
        sharedViewModel = ViewModelProvider(this, viewModelFactory)[ExerciseViewModel::class.java]
        binding.viewModel = viewModel
        viewModel.value = value
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.initList()

        val flexboxLayoutManager = FlexboxLayoutManager(requireContext()).apply {
            flexDirection = FlexDirection.ROW
            justifyContent = JustifyContent.FLEX_START
        }
        val flexboxLayoutManager2 = FlexboxLayoutManager(requireContext()).apply {
            flexDirection = FlexDirection.ROW
            justifyContent = JustifyContent.FLEX_START
        }
        binding.recTarget.layoutManager = flexboxLayoutManager
        binding.recItems.layoutManager = flexboxLayoutManager2


        viewModel.event.observe(viewLifecycleOwner, Observer { data ->
            when (data) {
                "Init" -> {
                    targetAdapter =
                        AdapterTarget(viewModel.contentList, viewModel, false, requireContext())
                    val dataItems: ArrayList<String> = ArrayList()
                    viewModel.contentList.Questions.forEach {
                        it.AnswerOptions.forEach {
                            dataItems.add(it.Answer.toString())
                        }
                    }
                    itemsAdapter = PlacementListAdapter(dataItems, viewModel, requireContext())
                    binding.recTarget.adapter = targetAdapter
                    binding.recItems.adapter = itemsAdapter
//                    viewModel.updateList()
                }

                "Update" -> {
//                    targetAdapter.notifyDataSetChanged()
//                    itemsAdapter.notifyDataSetChanged()
                }

                "Confirm" -> {
                    sharedViewModel.confirmExercise()
                }
            }
        })


        val dragManager = DragManager(
            binding.parentLayout, binding.recTarget
        ) { draggedView, x, y, position, draggedPosition ->
            position.let {
                it?.let { it1 ->
                    draggedPosition?.let { index ->
                        viewModel.contentList.addSelected(itemsAdapter.getItem(draggedPosition),position!!)
                        itemsAdapter.removeItem(draggedPosition)
                        targetAdapter.notifyItemChanged(position)
                        binding.btnConfirm.visibility = if (itemsAdapter.itemCount==0) View.VISIBLE else View.GONE
                    }
                }
            }
            true
        }

        dragManager.attachToRecyclerView(binding.recItems)

    }



}