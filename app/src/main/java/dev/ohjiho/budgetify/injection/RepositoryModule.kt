package dev.ohjiho.budgetify.injection

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.ohjiho.budgetify.data.repository.BudgetRepository
import dev.ohjiho.budgetify.data.repository.BudgetRepositoryImpl
import dev.ohjiho.budgetify.data.repository.CategoryRepository
import dev.ohjiho.budgetify.data.repository.CategoryRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Singleton
    @Binds
    abstract fun bindBudgetRepository(repository: BudgetRepositoryImpl): BudgetRepository

    @Singleton
    @Binds
    abstract fun bindCategoryRepository(repository: CategoryRepositoryImpl): CategoryRepository
}