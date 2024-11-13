package com.android.platform.ui.course.course.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.platform.ContentResult
import com.android.platform.ContentType
import com.android.platform.LessonReply
import com.android.platform.R
import com.android.platform.databinding.DialogSelectCourseItemBinding
import com.android.platform.ui.course.course.CourseActivityViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SelectCourseItemDialog(private val lessonsReply: LessonReply,private val  contentResultId: Int ,private val  viewModel: CourseActivityViewModel, private val context: Context
) :
    BottomSheetDialogFragment() {


    private lateinit var adapterLeft: SelectCourseItemAdapter
    private lateinit var adapterRight: SelectCourseItemAdapter
    private lateinit var binding: DialogSelectCourseItemBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogSelectCourseItemBinding.inflate(inflater, container, false)
        binding.imgClose.setOnClickListener{dismiss()}

        binding.recLeft.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.recRight.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        val data : ContentResult? = lessonsReply.contentsList?.filter { item-> item.contentType.number==contentResultId }?.get(0)

        data?.let {
            adapterLeft = SelectCourseItemAdapter(data.exercisesList.subList(0,(data.exercisesList.size/2)),viewModel,context)
            adapterRight = SelectCourseItemAdapter(data.exercisesList.subList((data.exercisesList.size/2),data.exercisesList.size),viewModel,context)
            binding.recLeft.adapter = adapterLeft
            binding.recRight.adapter = adapterRight
            binding.lblTitle.text = data.contentType.name
        }


        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }
}