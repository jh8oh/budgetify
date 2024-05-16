package dev.ohjiho.budgetify.data.injection

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.ohjiho.budgetify.data.sharedprefs.SetUpSharedPrefs
import dev.ohjiho.budgetify.data.sharedprefs.SetUpSharedPrefs.Companion.SET_UP_SHARED_PREF
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SharedPrefsModule {

    @Singleton
    @Provides
    fun provideSetUpSharedPreferences(@ApplicationContext context: Context): SetUpSharedPrefs {
        return SetUpSharedPrefs(context.getSharedPreferences(SET_UP_SHARED_PREF, Context.MODE_PRIVATE))
    }
}