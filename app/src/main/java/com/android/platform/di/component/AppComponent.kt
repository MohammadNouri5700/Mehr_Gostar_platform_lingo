package com.android.platform.di.component



import com.android.platform.PlatformApplication
import com.android.platform.di.module.ActivityBindingModule
import com.android.platform.di.module.AppModule
import com.android.platform.di.module.CoroutineModule
import com.android.platform.di.module.GrpcModule
import com.android.platform.di.module.QueueModule
import com.android.platform.di.module.RetrofitModule
import com.android.platform.di.module.RoomModule
import com.android.platform.di.module.SharedPreferencesModule
import com.android.platform.di.module.UIModule
import com.android.platform.di.module.ViewModelModule
import com.android.platform.ui.course.course.CourseActivity
import com.android.platform.ui.course.list.CourseList
import com.android.platform.ui.exercises.ExerciseActivity
import com.android.platform.ui.exercises.ai_content.AIContentFragment
import com.android.platform.ui.exercises.ai_context.AIContextFragment
import com.android.platform.ui.exercises.ai_letter.AILetterFragment
import com.android.platform.ui.exercises.ai_voice.AIVoiceFragment
import com.android.platform.ui.exercises.context_placement.ContextPlacementFragment
import com.android.platform.ui.exercises.detect.DetectFragment
import com.android.platform.ui.exercises.general.GeneralFragment
import com.android.platform.ui.exercises.listening.ListeningFragment
import com.android.platform.ui.exercises.order.OrderFragment
import com.android.platform.ui.exercises.placement.PlacementFragment
import com.android.platform.ui.home.HomeFragment
import com.android.platform.ui.level.LevelFragment
import com.android.platform.ui.main.MainActivity
import com.android.platform.ui.profile.ProfileFragment
import com.android.platform.ui.registeration.Login
import com.android.platform.ui.registeration.old.SignFragment
import com.android.platform.ui.report.ReportFragment
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AndroidInjectionModule::class,
    AndroidSupportInjectionModule::class,
    AppModule::class,
    RetrofitModule::class,
    RoomModule::class,
    ViewModelModule::class,
    GrpcModule::class,
    CoroutineModule::class,
    QueueModule::class,
    UIModule::class,
    SharedPreferencesModule::class,
    ActivityBindingModule::class
])
interface AppComponent {
    fun inject(application: PlatformApplication)

//    Activity's
//    fun inject(activity: CourseActivity)
    fun inject(activity: MainActivity)
    fun inject(activity: Login)
    fun inject(activity: CourseList)
    fun inject(courseActivity: CourseActivity)
    fun inject(exerciseActivity: ExerciseActivity)

//    Fragment's
    fun inject(homeFragment: HomeFragment)
    fun inject(levelFragment: LevelFragment)
    fun inject(reportFragment: ReportFragment)
    fun inject(profileFragment: ProfileFragment)
    fun inject(signFragment: SignFragment)
    // Exercise
    fun inject(placementFragment: PlacementFragment)
    fun inject(orderFragment: OrderFragment)
    fun inject(listeningFragment: ListeningFragment)
    fun inject(generalFragment: GeneralFragment)
    fun inject(detectFragment: DetectFragment)
    fun inject(contextPlacementFragment: ContextPlacementFragment)
    fun inject(aiVoiceFragment: AIVoiceFragment)
    fun inject(aiLatterFragment: AILetterFragment)
    fun inject(aiContextFragment: AIContextFragment)
    fun inject(aiContentFragment: AIContentFragment)

}