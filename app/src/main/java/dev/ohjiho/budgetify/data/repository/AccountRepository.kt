package dev.ohjiho.budgetify.data.repository

import dev.ohjiho.budgetify.data.local.dao.AccountDao
import dev.ohjiho.budgetify.data.local.entity.Account
import javax.inject.Singleton

@Singleton
class AccountRepository(private val dao: AccountDao) : BaseRepository<Account, AccountDao>(dao) {
    fun getAllAccounts() = dao.getAllAccounts()
    fun getAccount(uid: Int) = dao.getAccount(uid)
}