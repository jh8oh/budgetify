package dev.ohjiho.budgetify.data.injection

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.ohjiho.budgetify.data.sharedprefs.AccountSharedPrefs
import dev.ohjiho.budgetify.data.sharedprefs.AccountSharedPrefs.Companion.ACCOUNT_SHARED_PREFS
import dev.ohjiho.budgetify.data.sharedprefs.SetUpSharedPrefs
import dev.ohjiho.budgetify.data.sharedprefs.SetUpSharedPrefs.Companion.SET_UP_SHARED_PREFS
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object SharedPrefsModule {

    @Singleton
    @Provides
    fun provideSetUpSharedPreferences(@ApplicationContext context: Context): SetUpSharedPrefs {
        return SetUpSharedPrefs(context.getSharedPreferences(SET_UP_SHARED_PREFS, Context.MODE_PRIVATE))
    }

    @Singleton
    @Provides
    fun provideAccountSharedPreferences(@ApplicationContext context: Context): AccountSharedPrefs {
        return AccountSharedPrefs(context.getSharedPreferences(ACCOUNT_SHARED_PREFS, Context.MODE_PRIVATE))
    }
}