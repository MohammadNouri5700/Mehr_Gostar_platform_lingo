package com.android.platform.di.module

import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

import javax.inject.Qualifier



@Module
class CoroutineModule {

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class ApplicationScope

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class IoScope

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class MainScope

    @ApplicationScope
    @Singleton
    @Provides
    fun provideApplicationScope(): CoroutineScope =
        CoroutineScope(SupervisorJob() + Dispatchers.Default)

    @IoScope
    @Provides
    fun provideIoScope(): CoroutineScope =
        CoroutineScope(SupervisorJob() + Dispatchers.IO)

    @MainScope
    @Provides
    fun provideMainScope(): CoroutineScope =
        CoroutineScope(SupervisorJob() + Dispatchers.Main)
}