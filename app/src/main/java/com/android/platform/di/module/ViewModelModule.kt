package com.android.platform.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.platform.di.factory.ViewModelFactory
import com.android.platform.di.scop.ViewModelKey
import com.android.platform.ui.home.HomeViewModel
import com.android.platform.ui.level.LevelViewModel
import com.android.platform.ui.main.MainViewModel
import com.android.platform.ui.profile.ProfileViewModel
import com.android.platform.ui.report.ReportViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

//    @Binds
//    @IntoMap
//    @ViewModelKey(CourseViewModel::class)
//    abstract fun bindCourseViewModel(viewModel: CourseViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindMainViewModel(viewModel: MainViewModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    abstract fun bindHomeViewModel(viewModel: HomeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ReportViewModel::class)
    abstract fun bindReportViewModel(viewModel: ReportViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LevelViewModel::class)
    abstract fun bindLearnViewModel(viewModel: LevelViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProfileViewModel::class)
    abstract fun bindProfileViewModel(viewModel: ProfileViewModel): ViewModel



    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}