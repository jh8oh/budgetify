package dev.ohjiho.budgetify.data.injection

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.ohjiho.budgetify.data.repository.AccountRepositoryImpl
import dev.ohjiho.budgetify.data.repository.SetUpRepositoryImpl
import dev.ohjiho.budgetify.domain.repository.AccountRepository
import dev.ohjiho.budgetify.domain.repository.SetUpRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class RepositoryModule {
    // Room
    @Singleton
    @Binds
    abstract fun bindAccountRepository(repository: AccountRepositoryImpl): AccountRepository

    // SharedPrefs
    @Singleton
    @Binds
    abstract fun bindSetUpRepository(repository: SetUpRepositoryImpl): SetUpRepository
}