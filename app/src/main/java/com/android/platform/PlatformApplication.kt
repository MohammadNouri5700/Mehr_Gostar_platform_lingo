package com.android.platform

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.android.platform.di.component.AppComponent
import com.android.platform.di.component.DaggerAppComponent
import com.android.platform.di.factory.UserSessionTracker
import com.android.platform.di.module.AppModule
import com.android.platform.repository.data.database.UserLogDao
import com.android.platform.utils.extension.showToast

import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext
import java.time.LocalDate
import javax.inject.Inject

class PlatformApplication : Application(), HasAndroidInjector {

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>
    lateinit var appComponent: AppComponent

    @Inject
    lateinit var userLogDao: UserLogDao

    private lateinit var userSessionTracker: UserSessionTracker

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
        appComponent.inject(this)



        userSessionTracker = UserSessionTracker(userLogDao)


        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            private var activityReferences = 0
            private var isActivityChangingConfigurations = false

            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}

            override fun onActivityStarted(activity: Activity) {
                if (activityReferences == 0 && !isActivityChangingConfigurations) {
                    userSessionTracker.startSession()
                }
                activityReferences++
            }

            override fun onActivityResumed(activity: Activity) {}

            override fun onActivityPaused(activity: Activity) {}

            override fun onActivityStopped(activity: Activity) {
                activityReferences--
                isActivityChangingConfigurations = activity.isChangingConfigurations

                if (activityReferences == 0 && !isActivityChangingConfigurations) {
                    userSessionTracker.endSession()
                }
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

            override fun onActivityDestroyed(activity: Activity) {}
        })


    }

    override fun androidInjector(): AndroidInjector<Any> = androidInjector
}