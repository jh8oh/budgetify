package dev.ohjiho.budgetify.data.repository

import dev.ohjiho.budgetify.data.local.dao.AccountDao
import dev.ohjiho.budgetify.data.local.dao.TransactionDao
import dev.ohjiho.budgetify.data.local.entity.TransactionEntity
import dev.ohjiho.budgetify.data.model.Transaction
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface TransactionRepository : BaseRepository<TransactionEntity> {
    fun getAccountTransactions(accountId: Int): Flow<List<Transaction>>
    fun getCategoryTransactions(categoryId: Int): Flow<List<Transaction>>
}

class TransactionRepositoryImpl @Inject constructor(
    private val dao: TransactionDao,
    private val accountDao: AccountDao,
) : BaseRepositoryImpl<TransactionEntity, TransactionDao>(dao), TransactionRepository {

    override suspend fun insert(entity: TransactionEntity): Long {
        accountDao.addToAccountCurrentAmount(entity.accountId, entity.amount)
        return super.insert(entity)
    }

    override suspend fun update(entity: TransactionEntity) {
        val prevTransaction = dao.getTransaction(entity.uid)
        if (prevTransaction.accountId == entity.accountId) {
            val difference = entity.amount - prevTransaction.amount
            accountDao.addToAccountCurrentAmount(entity.accountId, difference)
        } else {
            accountDao.addToAccountCurrentAmount(prevTransaction.accountId, prevTransaction.amount.negate())
            accountDao.addToAccountCurrentAmount(entity.accountId, entity.amount)
        }

        super.update(entity)
    }

    override suspend fun delete(vararg entity: TransactionEntity) {
        for (e in entity) {
            accountDao.addToAccountCurrentAmount(e.accountId, e.amount.negate())
        }
        super.delete(*entity)
    }

    override fun getAccountTransactions(accountId: Int) = dao.getAccountTransactions(accountId)
    override fun getCategoryTransactions(categoryId: Int) = dao.getCategoryTransactions(categoryId)
}