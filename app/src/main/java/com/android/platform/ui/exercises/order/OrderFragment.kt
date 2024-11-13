package com.android.platform.ui.exercises.order



import android.os.Bundle
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
import com.android.platform.databinding.FragmentOrderExerciseBinding
import com.android.platform.ui.exercises.ExerciseViewModel
import com.android.platform.ui.exercises.order.adapter.OrderListAdapter
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import javax.inject.Inject


class OrderFragment @Inject constructor(val value:ExerciseModel): Fragment() {

    private lateinit var sharedViewModel: ExerciseViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: OrderViewModel

    private lateinit var binding: FragmentOrderExerciseBinding

    lateinit var itemsAdapter : OrderListAdapter
    lateinit var selectedAdapter : OrderListAdapter



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_order_exercise, container, false)
        (requireActivity().application as PlatformApplication).appComponent.inject(this)
        viewModel = ViewModelProvider(this, viewModelFactory)[OrderViewModel::class.java]
        sharedViewModel = ViewModelProvider(this, viewModelFactory)[ExerciseViewModel::class.java]
        viewModel.value=value
        binding.viewModel = viewModel
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
        binding.recItems.layoutManager = flexboxLayoutManager
        binding.recOrderedItems.layoutManager = flexboxLayoutManager2



        viewModel.event.observe(viewLifecycleOwner, Observer { data ->
            when (data) {
                "Init" -> {
                    itemsAdapter = OrderListAdapter(viewModel.contentList,viewModel,false,requireContext())
                    selectedAdapter = OrderListAdapter(viewModel.selectedList,viewModel,true,requireContext())
                    binding.recItems.adapter=itemsAdapter
                    binding.recOrderedItems.adapter=selectedAdapter
                    viewModel.updateList()
                }
                "Update"->{
                    selectedAdapter.notifyDataSetChanged()
                    itemsAdapter.notifyDataSetChanged()
                    binding.btnConfirm.visibility = if (itemsAdapter.itemCount==0) View.VISIBLE else View.GONE
                    binding.containerItems.visibility = if (itemsAdapter.itemCount!=0) View.VISIBLE else View.GONE
                    binding.containerSelected.visibility = if (selectedAdapter.itemCount!=0) View.VISIBLE else View.GONE
                }
                "Confirm"->{
                    sharedViewModel.confirmExercise()
                }
            }
        })



    }






}