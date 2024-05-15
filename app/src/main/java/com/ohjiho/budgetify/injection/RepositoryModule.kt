package com.ohjiho.budgetify.injection

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.ohjiho.budgetify.data.repository.AccountRepositoryImpl
import dev.ohjiho.budgetify.domain.repository.AccountRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Singleton
    @Binds
    abstract fun bindAccountRepository(repository: AccountRepositoryImpl): AccountRepository
}