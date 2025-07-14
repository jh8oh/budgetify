package dev.ohjiho.budgetify.data.repository

import dev.ohjiho.budgetify.data.room.dao.AccountDao
import dev.ohjiho.budgetify.domain.model.Account
import dev.ohjiho.budgetify.domain.repository.AccountRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class AccountRepositoryImpl @Inject constructor(private val dao: AccountDao) :
    BaseRoomRepositoryImpl<Account, AccountDao>(dao), AccountRepository {
    override suspend fun getAccount(uid: Int): Account? = dao.getAccount(uid)
    override fun getAllAccounts(): Flow<List<Account>> = dao.getAllAccounts()
    override fun getAllUniqueInstitutions(): Flow<List<String>> = dao.getAllUniqueInstitutions()
}