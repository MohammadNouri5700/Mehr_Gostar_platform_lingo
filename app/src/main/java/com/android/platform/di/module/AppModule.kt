package com.android.platform.di.module

import android.content.Context
import com.android.platform.PlatformApplication
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val application: PlatformApplication) {

    @Provides
    @Singleton
    fun provideContext(): Context = application
}