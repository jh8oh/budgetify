package dev.ohjiho.budgetify.data.injection

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.ohjiho.budgetify.data.room.BudgetifyDatabase
import dev.ohjiho.budgetify.data.room.dao.AccountDao
import dev.ohjiho.budgetify.data.room.dao.CategoryDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object RoomModule {
    private const val BUDGETIFY_DATABASE_NAME = "budgetify"

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): BudgetifyDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            BudgetifyDatabase::class.java,
            BUDGETIFY_DATABASE_NAME
        ).createFromAsset("budgetify.db").build()
    }

    @Provides
    fun provideAccountDao(database: BudgetifyDatabase): AccountDao = database.accountDao()

    @Provides
    fun provideCategoryDao(database: BudgetifyDatabase): CategoryDao = database.categoryDao()
}