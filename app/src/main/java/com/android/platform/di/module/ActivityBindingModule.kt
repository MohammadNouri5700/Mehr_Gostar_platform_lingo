package com.android.platform.di.module

import com.android.platform.ui.main.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBindingModule {
//    @ContributesAndroidInjector(modules = [])
//    abstract fun contributeCourseActivity(): CourseActivity

    @ContributesAndroidInjector(modules = [])
    abstract fun contributeMainActivity(): MainActivity

}