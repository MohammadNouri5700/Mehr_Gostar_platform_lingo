package com.android.platform.di.module

import android.content.Context
import android.content.SharedPreferences
import com.android.platform.di.factory.Preferences
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class SharedPreferencesModule {

    private val Pname = "sharedPreferences"

    @Singleton
    @Provides
    fun provideSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(Pname, Context.MODE_PRIVATE)
    }

    @Singleton
    @Provides
    fun providePreferencesHelper(sharedPreferences: SharedPreferences): Preferences {
        return Preferences(sharedPreferences)
    }
}