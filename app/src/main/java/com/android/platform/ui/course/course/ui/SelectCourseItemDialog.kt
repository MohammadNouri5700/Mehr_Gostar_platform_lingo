package com.android.platform.ui.course.course.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.platform.ContentType
import com.android.platform.LessonReply
import com.android.platform.R
import com.android.platform.databinding.DialogSelectCourseItemBinding
import com.android.platform.ui.course.course.CourseActivityViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SelectCourseItemDialog(private val lessonsReply: LessonReply,private val  lessonId: Int ,private val  viewModel: CourseActivityViewModel, private val context: Context
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


        when(lessonId){
            lessonsReply.vocabulary.id->{
                val data = lessonsReply.exercisesList?.filter { item-> item.contentType==ContentType.Vocab }?.toTypedArray()
                data?.let {
                    adapterLeft = SelectCourseItemAdapter(data.asList().subList(0,(it.size/2)),viewModel,context)
                    adapterRight = SelectCourseItemAdapter(data.asList().subList((it.size/2),it.size),viewModel,context)
                    binding.recLeft.adapter = adapterLeft
                    binding.recRight.adapter = adapterRight
                }
                binding.lblTitle.text = resources.getText(R.string.vocabulary)
            }
            lessonsReply.speaking.id->{
                binding.lblTitle.text = resources.getText(R.string.speaking)
            }
            lessonsReply.grammer.id->{
                binding.lblTitle.text = resources.getText(R.string.grammar)
            }
//            lessonsReply.grammer.id->{
//
//            }
        }



        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }
}