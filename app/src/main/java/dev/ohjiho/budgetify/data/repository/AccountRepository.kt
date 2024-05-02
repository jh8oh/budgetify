package dev.ohjiho.budgetify.data.repository

import dev.ohjiho.budgetify.data.local.dao.AccountDao
import dev.ohjiho.budgetify.data.local.entity.AccountEntity
import dev.ohjiho.budgetify.data.model.Account
import kotlinx.coroutines.flow.Flow

interface AccountRepository {
    fun getAllAccounts(): Flow<List<Account>>
    suspend fun getAccount(uid: Int): Account
}

class AccountRepositoryImpl(private val dao: AccountDao) : BaseRepositoryImpl<AccountEntity, AccountDao>(dao), AccountRepository {
    override fun getAllAccounts() = dao.getAllAccounts()
    override suspend fun getAccount(uid: Int) = dao.getAccount(uid)
}