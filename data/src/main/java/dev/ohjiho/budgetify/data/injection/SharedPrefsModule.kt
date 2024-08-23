package dev.ohjiho.budgetify.data.injection

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.ohjiho.budgetify.data.sharedprefs.CurrencySharedPrefs
import dev.ohjiho.budgetify.data.sharedprefs.CurrencySharedPrefs.Companion.CURRENCY_SHARED_PREFS
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
    fun provideCurrencySharedPreference(@ApplicationContext context: Context): CurrencySharedPrefs {
        return CurrencySharedPrefs(context.getSharedPreferences(CURRENCY_SHARED_PREFS, Context.MODE_PRIVATE))
    }
}