package com.android.platform.ui.exercises.context_placement


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.android.platform.ExerciseModel
import com.android.platform.PlatformApplication
import com.android.platform.R
import com.android.platform.databinding.FragmentContextPlacementExerciseBinding
import com.android.platform.di.factory.DragManager
import com.android.platform.ui.exercises.ExerciseViewModel
import com.android.platform.ui.exercises.context_placement.adapter.BottomAdapter
import com.android.platform.ui.exercises.context_placement.adapter.TopAdapter
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import javax.inject.Inject


class ContextPlacementFragment @Inject constructor(val value: ExerciseModel) : Fragment() {

    private lateinit var sharedViewModel: ExerciseViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: ContextPlacementViewModel

    private lateinit var binding: FragmentContextPlacementExerciseBinding

//    private lateinit var itemsAdapter:ContextPlacementItemsAdapter
//    private lateinit var sentenceAdapter:ContextPlacementSentenceAdapter


    private lateinit var bottomAdapter: BottomAdapter
    private lateinit var topAdapter: TopAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_context_placement_exercise,
            container,
            false
        )
        (requireActivity().application as PlatformApplication).appComponent.inject(this)
        viewModel = ViewModelProvider(this, viewModelFactory)[ContextPlacementViewModel::class.java]
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
            justifyContent = JustifyContent.SPACE_AROUND
        }
        binding.recItems.layoutManager = flexboxLayoutManager
        binding.recSentences.layoutManager = FlexboxLayoutManager(requireContext())


        viewModel.event.observe(viewLifecycleOwner, Observer { data ->
            when (data) {
                "Init" -> {

                    bottomAdapter = BottomAdapter(viewModel.items)
                    topAdapter = TopAdapter(viewModel.sentences, requireContext())
                    binding.recItems.adapter = bottomAdapter
                    binding.recSentences.adapter = topAdapter

                    val dragManager = DragManager(
                        binding.parentLayout,
                        binding.recSentences,
                        true
                    ) { draggedView, x, y, position, draggedPosition ->
                        position.let {
                            it?.let { it1 ->
                                draggedPosition?.let { index ->
                                    viewModel.addSelected(
                                        it1,
                                        viewModel.items[index]
                                    )
                                }
                            }
                        }
                        true
                    }

                    dragManager.attachToRecyclerView(binding.recItems)

                }

                "Update" -> {
                    bottomAdapter = BottomAdapter(viewModel.items)
                    topAdapter = TopAdapter(viewModel.sentences, requireContext())
                    binding.recSentences.adapter = topAdapter
                    if (!viewModel.items.isEmpty()) {
                        binding.recItems.adapter = bottomAdapter
                        binding.recItems.visibility = View.VISIBLE
                        binding.btnConfirm.visibility = View.GONE
                    } else {
                        binding.recItems.visibility = View.GONE
                        binding.btnConfirm.visibility = View.VISIBLE
                    }
                }

                "Confirm" -> {
                    sharedViewModel.confirmExercise()
                }
            }
        })


    }


}