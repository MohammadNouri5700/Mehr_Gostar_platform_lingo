package com.android.platform.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.platform.di.factory.ViewModelFactory
import com.android.platform.di.scop.ViewModelKey
import com.android.platform.ui.course.CourseViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import dagger.multibindings.StringKey

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(CourseViewModel::class)
    abstract fun bindCourseViewModel(viewModel: CourseViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}