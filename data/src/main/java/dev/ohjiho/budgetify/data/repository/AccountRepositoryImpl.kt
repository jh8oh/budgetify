package dev.ohjiho.budgetify.data.repository

import dev.ohjiho.budgetify.data.room.dao.AccountDao
import dev.ohjiho.budgetify.data.sharedprefs.AccountSharedPrefs
import dev.ohjiho.budgetify.domain.model.AccountEntity
import dev.ohjiho.budgetify.domain.repository.AccountRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class AccountRepositoryImpl @Inject constructor(private val dao: AccountDao) :
    BaseRoomRepositoryImpl<AccountEntity, AccountDao>(dao), AccountRepository {
    override suspend fun getAccount(uid: Int): AccountEntity = dao.getAccount(uid)
    override fun getAllAccounts(): Flow<List<AccountEntity>> = dao.getAllAccounts()
}