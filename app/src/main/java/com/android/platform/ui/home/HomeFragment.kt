package com.android.platform.ui.home


import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.platform.PlatformApplication
import com.android.platform.R
import com.android.platform.databinding.FragmentHomeBinding
import com.android.platform.di.factory.CallQueueManager
import com.android.platform.di.module.CoroutineModule
import com.android.platform.repository.data.database.ImageDao
import com.android.platform.ui.home.story.PodcastAdapter
import com.android.platform.ui.home.story.StoryAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


class HomeFragment : Fragment() {

    @Inject
    lateinit var imageDao: ImageDao



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

    override fun onResume() {
        super.onResume()

        binding.recStory.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
//        binding.recStory.adapter = StoryAdapter(listOf("Elsa","Elsa","Elsa","Elsa","Elsa","Elsa","Elsa","Elsa","Elsa","Elsa","Elsa"))


//        binding.recPodcasts.layoutManager =
//            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
//        binding.recPodcasts.adapter = PodcastAdapter(listOf("علمی و تخیلی", "ترسناک","درام","درام","درام","درام","درام","درام","درام","درام","درام","درام","درام","درام","درام","درام","درام","درام","درام","درام","درام","درام","درام","درام","درام"))

        val bitmap =
            BitmapFactory.decodeResource(resources, com.android.platform.R.drawable.img_fake)
        binding.roundedBlurredCardView.setImageBitmap(bitmap)
        binding.roundedBlurredCardView.setBlurredBackground(bitmap);
        val bitmapb = binding.roundedBlurredCardView.createBlurredBitmap(bitmap)
        binding.roundedBlurredCardView.setBackgroundColor(Color.TRANSPARENT);


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        viewModel.event.observe(viewLifecycleOwner, Observer { data ->
            when (data) {
                "StoryUpdate" -> {
                    viewModel.call.enqueueMainTask {
                        binding.recStory.adapter = viewModel.stories?.let { StoryAdapter(it) }
                    }
                    viewModel.getPodcastCategory()
                }

                "PodcastCategoryUpdate" -> {
                            binding.recPodcasts.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                            binding.recPodcasts2.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                            val midIndex = viewModel.podcastCategory?.podcastCategoriesCount?.div(2) ?:0
                    viewModel.call.enqueueMainTask {
                            binding.recPodcasts.adapter = viewModel.podcastCategory?.let { PodcastAdapter(it.podcastCategoriesList.subList(0,midIndex),imageDao,viewModel.call,requireContext()) }
                            binding.recPodcasts2.adapter = viewModel.podcastCategory?.let { PodcastAdapter(it.podcastCategoriesList.subList(midIndex,it.podcastCategoriesList.size),imageDao,viewModel.call,requireContext()) }
                        }
                }
            }
        })
    }




}