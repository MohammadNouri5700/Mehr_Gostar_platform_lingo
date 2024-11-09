package com.android.platform.di.module

import com.android.platform.ui.course.course.CourseActivity
import com.android.platform.ui.course.list.CourseList
import com.android.platform.ui.main.MainActivity
import com.android.platform.ui.registeration.Login
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBindingModule {
//    @ContributesAndroidInjector(modules = [])
//    abstract fun contributeCourseActivity(): CourseActivity

    @ContributesAndroidInjector(modules = [])
    abstract fun contributeMainActivity(): MainActivity

    @ContributesAndroidInjector(modules = [])
    abstract fun contributeLogin(): Login

    @ContributesAndroidInjector(modules = [])
    abstract fun contributeCourseList(): CourseList

    @ContributesAndroidInjector(modules = [])
    abstract fun contributeCourseActivity(): CourseActivity
}