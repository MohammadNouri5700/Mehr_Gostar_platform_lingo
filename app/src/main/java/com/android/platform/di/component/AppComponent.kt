package com.android.platform.di.component


import com.android.platform.PlatformApplication
import com.android.platform.di.module.ActivityBindingModule
import com.android.platform.di.module.AppModule
import com.android.platform.di.module.RetrofitModule
import com.android.platform.di.module.RoomModule
import com.android.platform.di.module.ViewModelModule
import com.android.platform.ui.course.CourseActivity
import com.android.platform.ui.home.HomeFragment
import com.android.platform.ui.learn.LearnFragment
import com.android.platform.ui.main.MainActivity
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
    RoomModule::class,
    RetrofitModule::class,
    ViewModelModule::class,
    ActivityBindingModule::class
])
interface AppComponent {
    fun inject(application: PlatformApplication)

//    Activity's
    fun inject(activity: CourseActivity)
    fun inject(mainActivity: MainActivity)

//    Fragment's
    fun inject(homeFragment: HomeFragment)
    fun inject(learnFragment: LearnFragment)
    fun inject(reportFragment: ReportFragment)

}