package com.android.platform.di.module

import com.android.platform.di.factory.CallQueueManager
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton

@Module
class QueueModule {

    @Singleton
    @Provides
    fun provideCallQueueManager(@CoroutineModule.IoScope ioScope: CoroutineScope,
                                @CoroutineModule.ApplicationScope applicationScope: CoroutineScope,
                                @CoroutineModule.MainScope mainScope: CoroutineScope): CallQueueManager =
        CallQueueManager(ioScope,applicationScope,mainScope)
}