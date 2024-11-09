package com.android.platform.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.platform.di.factory.ViewModelFactory
import com.android.platform.di.scop.ViewModelKey
import com.android.platform.ui.course.course.CourseActivityViewModel
import com.android.platform.ui.course.list.CourseListViewModel
import com.android.platform.ui.exercises.ExerciseViewModel
import com.android.platform.ui.exercises.ai_content.AIContentViewModel
import com.android.platform.ui.exercises.ai_context.AIContextViewModel
import com.android.platform.ui.exercises.ai_latter.AILatterViewModel
import com.android.platform.ui.exercises.ai_voice.AIVoiceViewModel
import com.android.platform.ui.exercises.context_placement.ContextPlacementViewModel
import com.android.platform.ui.exercises.detect.DetectViewModel
import com.android.platform.ui.exercises.general.GeneralViewModel
import com.android.platform.ui.exercises.listening.ListeningViewModel
import com.android.platform.ui.exercises.order.OrderViewModel
import com.android.platform.ui.exercises.placement.PlacementViewModel
import com.android.platform.ui.home.HomeViewModel
import com.android.platform.ui.level.LevelViewModel
import com.android.platform.ui.main.MainViewModel
import com.android.platform.ui.profile.ProfileViewModel
import com.android.platform.ui.registeration.LoginViewModel
import com.android.platform.ui.registeration.old.SignViewModel
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
    @IntoMap
    @ViewModelKey(SignViewModel::class)
    abstract fun bindSignViewModel(viewModel: SignViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    abstract fun bindLoginViewModel(viewModel: LoginViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CourseListViewModel::class)
    abstract fun bindCourseListViewModel(viewModel: CourseListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CourseActivityViewModel::class)
    abstract fun bindCourseActivityViewModel(viewModel: CourseActivityViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ExerciseViewModel::class)
    abstract fun bindExerciseViewModel(viewModel: ExerciseViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PlacementViewModel::class)
    abstract fun bindPlacementViewModel(viewModel: PlacementViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(OrderViewModel::class)
    abstract fun bindOrderViewModel(viewModel: OrderViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ListeningViewModel::class)
    abstract fun bindListeningViewModel(viewModel: ListeningViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(GeneralViewModel::class)
    abstract fun bindGeneralViewModel(viewModel: GeneralViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DetectViewModel::class)
    abstract fun bindDetectViewModel(viewModel: DetectViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ContextPlacementViewModel::class)
    abstract fun bindContextPlacementViewModel(viewModel: ContextPlacementViewModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(AIVoiceViewModel::class)
    abstract fun bindAIVoiceViewModel(viewModel: AIVoiceViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AILatterViewModel::class)
    abstract fun bindAILatterViewModel(viewModel: AILatterViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AIContextViewModel::class)
    abstract fun bindAIContextViewModel(viewModel: AIContextViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AIContentViewModel::class)
    abstract fun bindAIContentViewModel(viewModel: AIContentViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}