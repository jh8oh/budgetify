package dev.ohjiho.budgetify.injection

import android.content.Context
import androidx.room.Room
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.ohjiho.budgetify.data.local.BudgetifyDatabase
import dev.ohjiho.budgetify.data.local.dao.AccountDao
import dev.ohjiho.budgetify.data.local.dao.BudgetDao
import dev.ohjiho.budgetify.data.local.dao.CategoryDao
import dev.ohjiho.budgetify.data.local.dao.ConversionRateDao
import dev.ohjiho.budgetify.data.local.dao.TagDao
import dev.ohjiho.budgetify.data.local.dao.TransactionDao
import dev.ohjiho.budgetify.data.local.dao.TransactionTagCrossRefDao
import dev.ohjiho.budgetify.data.repository.AccountRepository
import dev.ohjiho.budgetify.data.repository.BudgetRepository
import dev.ohjiho.budgetify.data.repository.CategoryRepository
import dev.ohjiho.budgetify.data.repository.ConversionRateRepository
import dev.ohjiho.budgetify.data.repository.TagRepository
import dev.ohjiho.budgetify.data.repository.TransactionRepository
import dev.ohjiho.budgetify.data.repository.TransactionTagCrossRefRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModules {
    @Singleton
    @Binds
    abstract fun bindAccountRepository(repository: AccountRepository): AccountRepository

    @Singleton
    @Binds
    abstract fun bindBudgetRepository(repository: BudgetRepository): BudgetRepository

    @Singleton
    @Binds
    abstract fun bindCategoryRepository(repository: CategoryRepository): CategoryRepository

    @Singleton
    @Binds
    abstract fun bindConversionRateRepository(repository: ConversionRateRepository): ConversionRateRepository

    @Singleton
    @Binds
    abstract fun bindTagRepository(repository: TagRepository): TagRepository

    @Singleton
    @Binds
    abstract fun bindTransactionRepository(repository: TransactionRepository): TransactionRepository

    @Singleton
    @Binds
    abstract fun bindTransactionTagCrossRefRepository(repository: TransactionTagCrossRefRepository): TransactionTagCrossRefRepository
}

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModules {
    private const val BUDGET_DATABASE = "budget"

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): BudgetifyDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            BudgetifyDatabase::class.java,
            BUDGET_DATABASE
        ).build()
    }

    @Provides
    fun provideAccountDao(database: BudgetifyDatabase): AccountDao = database.accountDao()

    @Provides
    fun provideBudgetDao(database: BudgetifyDatabase): BudgetDao = database.budgetDao()

    @Provides
    fun provideCategoryDao(database: BudgetifyDatabase): CategoryDao = database.categoryDao()

    @Provides
    fun provideConversionRateDao(database: BudgetifyDatabase): ConversionRateDao = database.conversionRateDao()

    @Provides
    fun provideTagDao(database: BudgetifyDatabase): TagDao = database.tagDao()

    @Provides
    fun provideTransactionDao(database: BudgetifyDatabase): TransactionDao = database.transactionDao()

    @Provides
    fun provideTransactionTagCrossRefDao(database: BudgetifyDatabase): TransactionTagCrossRefDao = database.transactionTagCrossRefDao()
}