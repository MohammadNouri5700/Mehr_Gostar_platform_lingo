package com.android.platform.di.component



import com.android.platform.PlatformApplication
import com.android.platform.di.module.ActivityBindingModule
import com.android.platform.di.module.AppModule
import com.android.platform.di.module.GrpcModule
import com.android.platform.di.module.RetrofitModule
import com.android.platform.di.module.RoomModule
import com.android.platform.di.module.ViewModelModule
import com.android.platform.ui.home.HomeFragment
import com.android.platform.ui.level.LevelFragment
import com.android.platform.ui.main.MainActivity
import com.android.platform.ui.profile.ProfileFragment
import com.android.platform.ui.registeration.SignFragment
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
    ActivityBindingModule::class
])
interface AppComponent {
    fun inject(application: PlatformApplication)

//    Activity's
//    fun inject(activity: CourseActivity)
    fun inject(mainActivity: MainActivity)

//    Fragment's
    fun inject(homeFragment: HomeFragment)
    fun inject(levelFragment: LevelFragment)
    fun inject(reportFragment: ReportFragment)
    fun inject(profileFragment: ProfileFragment)
    fun inject(signFragment: SignFragment)

}