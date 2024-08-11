package com.android.platform.di.component


import com.android.platform.PlatformApplication
import com.android.platform.di.module.AppModule
import com.android.platform.di.module.RetrofitModule
import com.android.platform.di.module.RoomModule
import com.android.platform.di.module.ViewModelModule
import com.android.platform.ui.course.CourseActivity
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
    ViewModelModule::class
])
interface AppComponent {
    fun inject(application: PlatformApplication)
    fun inject(activity: CourseActivity)
}