package dev.ohjiho.budgetify.data.injection

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.ohjiho.budgetify.data.local.BudgetifyDatabase
import dev.ohjiho.budgetify.data.local.util.DatabaseInitializer
import dev.ohjiho.budgetify.data.local.dao.AccountDao
import javax.inject.Provider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DataModule {
    private const val BUDGETIFY_DATABASE_NAME = "budgetify"

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context, accountProvider: Provider<AccountDao>): BudgetifyDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            BudgetifyDatabase::class.java,
            BUDGETIFY_DATABASE_NAME
        ).addCallback(DatabaseInitializer(accountProvider)).build()
    }

    @Provides
    fun provideAccountDao(database: BudgetifyDatabase): AccountDao = database.accountDao()
}