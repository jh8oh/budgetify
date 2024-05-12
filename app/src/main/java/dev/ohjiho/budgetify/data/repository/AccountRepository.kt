package dev.ohjiho.budgetify.data.repository

import dev.ohjiho.budgetify.data.local.dao.AccountDao
import dev.ohjiho.budgetify.data.local.dao.TransactionDao
import dev.ohjiho.budgetify.data.local.entity.AccountEntity
import dev.ohjiho.budgetify.data.model.Account
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.single
import javax.inject.Inject

interface AccountRepository : BaseRepository<AccountEntity> {
    fun getAllAccounts(): Flow<List<Account>>
    suspend fun getAccount(uid: Int): Account
}

class AccountRepositoryImpl @Inject constructor(private val dao: AccountDao, private val transactionDao: TransactionDao) :
    BaseRepositoryImpl<AccountEntity, AccountDao>(dao), AccountRepository {

    override suspend fun update(entity: AccountEntity) {
        val prevAccount = dao.getAccount(entity.uid)
        entity.currentAmount += (entity.startingAmount - prevAccount.startingAmount)
        super.update(entity)
    }

    override fun getAllAccounts() = dao.getAllAccounts()
    override suspend fun getAccount(uid: Int) = dao.getAccount(uid)
}