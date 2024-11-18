package com.android.platform.ui.exercises.context_placement


import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.platform.ExerciseModel
import com.android.platform.PlatformApplication
import com.android.platform.R
import com.android.platform.databinding.FragmentContextPlacementExerciseBinding
import com.android.platform.databinding.FragmentDetectExerciseBinding
import com.android.platform.databinding.FragmentGeneralExerciseBinding
import com.android.platform.ui.exercises.context_placement.adapter.BottomAdapter
import com.android.platform.ui.exercises.context_placement.adapter.SimpleItemTouchHelperCallback
import com.android.platform.ui.exercises.order.adapter.OrderListAdapter
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import javax.inject.Inject


class ContextPlacementFragment  @Inject constructor(val value: ExerciseModel) : Fragment() {


    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: ContextPlacementViewModel

    private lateinit var binding: FragmentContextPlacementExerciseBinding

//    private lateinit var itemsAdapter:ContextPlacementItemsAdapter
//    private lateinit var sentenceAdapter:ContextPlacementSentenceAdapter


private lateinit var bottomAdapter: BottomAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_context_placement_exercise, container, false)
        (requireActivity().application as PlatformApplication).appComponent.inject(this)
        viewModel = ViewModelProvider(this, viewModelFactory)[ContextPlacementViewModel::class.java]
        binding.viewModel = viewModel
        viewModel.value=value
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

                    bottomAdapter = BottomAdapter(viewModel.items) { draggedWord ->
                        updateTextWithDraggedWord(draggedWord)
                    }
                    binding.recItems.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                    binding.recItems.adapter = bottomAdapter


                    val itemTouchHelperCallback = object : ItemTouchHelper.Callback() {

                        override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
                            val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
                            return makeMovementFlags(dragFlags, 0)
                        }

                        override fun onMove(
                            recyclerView: RecyclerView,
                            viewHolder: RecyclerView.ViewHolder,
                            target: RecyclerView.ViewHolder
                        ): Boolean {
                            val draggedWord = viewModel.items[viewHolder.adapterPosition]
                            viewModel.items.removeAt(viewHolder.adapterPosition)
                            viewModel.items.add(target.adapterPosition, draggedWord)
                            bottomAdapter.notifyItemMoved(viewHolder.adapterPosition, target.adapterPosition)
                            return true
                        }

                        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                        }

                        override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
                            super.onSelectedChanged(viewHolder, actionState)
                            if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
                            }
                        }
                    }

                    val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
                    itemTouchHelper.attachToRecyclerView(binding.recItems)
                }
                "Update"->{

                }
                "Confirm"->{
                }
            }
        })





    }

    fun updateTextWithDraggedWord(draggedWord: String) {

    }



}