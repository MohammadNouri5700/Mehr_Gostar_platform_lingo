package com.android.platform.ui.course.course.ui

import android.app.Dialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.platform.ContentResult
import com.android.platform.ContentType
import com.android.platform.LessonReply
import com.android.platform.R
import com.android.platform.databinding.DialogSelectCourseItemBinding
import com.android.platform.ui.course.course.CourseActivityViewModel
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SelectCourseItemDialog(private val lessonsReply: LessonReply,private val  contentResultId: Int ,private val  viewModel: CourseActivityViewModel, private val context: Context
) :
    BottomSheetDialogFragment() {


    private lateinit var adapterRight: SelectCourseItemAdapter
    private lateinit var binding: DialogSelectCourseItemBinding



    override fun onStart() {
        super.onStart()
        dialog?.window?.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogSelectCourseItemBinding.inflate(inflater, container, false)
        binding.imgClose.setOnClickListener{dismiss()}



        val flexboxLayoutManager = FlexboxLayoutManager(requireContext()).apply {
            flexDirection = FlexDirection.ROW
            justifyContent = JustifyContent.SPACE_AROUND
            alignItems = AlignItems.STRETCH
        }


        binding.recItems.setLayoutManager(flexboxLayoutManager)

        val data : ContentResult? = lessonsReply.contentsList?.filter { item-> item.contentType.number==contentResultId }?.get(0)

        data?.let {
            adapterRight = SelectCourseItemAdapter(data.exercisesList,viewModel,context)
            binding.recItems.adapter = adapterRight
            binding.lblTitle.text = data.contentType.name
        }


        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }
}