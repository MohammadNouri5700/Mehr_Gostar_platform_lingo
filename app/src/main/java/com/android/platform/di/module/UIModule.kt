package com.android.platform.di.module

import android.content.Context
import com.android.platform.di.factory.LoadingDialog
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class UIModule {

    @Provides
    @Singleton
    fun provideLoadingDialog(context: Context): LoadingDialog {
        return LoadingDialog(context)
    }
}