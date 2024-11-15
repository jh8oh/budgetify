package dev.ohjiho.budgetify.data.injection

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.ohjiho.budgetify.data.repository.AccountRepositoryImpl
import dev.ohjiho.budgetify.data.repository.CategoryRepositoryImpl
import dev.ohjiho.budgetify.data.repository.CurrencyRepositoryImpl
import dev.ohjiho.budgetify.data.repository.SetUpRepositoryImpl
import dev.ohjiho.budgetify.data.repository.TransactionRepositoryImpl
import dev.ohjiho.budgetify.domain.repository.AccountRepository
import dev.ohjiho.budgetify.domain.repository.CategoryRepository
import dev.ohjiho.budgetify.domain.repository.CurrencyRepository
import dev.ohjiho.budgetify.domain.repository.SetUpRepository
import dev.ohjiho.budgetify.domain.repository.TransactionRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class RepositoryModule {
    // Room
    @Singleton
    @Binds
    abstract fun bindAccountRepository(repository: AccountRepositoryImpl): AccountRepository

    @Singleton
    @Binds
    abstract fun bindCategoryRepository(repository: CategoryRepositoryImpl): CategoryRepository

    @Singleton
    @Binds
    abstract fun bindTransactionRepository(repository: TransactionRepositoryImpl): TransactionRepository

    // SharedPrefs
    @Singleton
    @Binds
    abstract fun bindSetUpRepository(repository: SetUpRepositoryImpl): SetUpRepository

    @Singleton
    @Binds
    abstract fun bindCurrencyRepository(repository: CurrencyRepositoryImpl): CurrencyRepository
}