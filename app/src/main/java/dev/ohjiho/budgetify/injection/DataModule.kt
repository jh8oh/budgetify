package dev.ohjiho.budgetify.injection

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.ohjiho.budgetify.data.local.BudgetifyDatabase
import dev.ohjiho.budgetify.data.local.dao.BudgetDao
import dev.ohjiho.budgetify.data.local.dao.CategoryDao
import javax.inject.Singleton

@Module
object DataModule {
    private const val BUDGETIFY_DATABASE_NAME = "budgetify"

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): BudgetifyDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            BudgetifyDatabase::class.java,
            BUDGETIFY_DATABASE_NAME
        ).build()
    }

    @Provides
    fun provideBudgetDao(database: BudgetifyDatabase): BudgetDao = database.budgetDao()

    @Provides
    fun provideCategoryDao(database: BudgetifyDatabase): CategoryDao = database.categoryDao()
}