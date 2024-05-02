package dev.ohjiho.budgetify.data.repository

import dev.ohjiho.budgetify.data.local.dao.TransactionDao
import dev.ohjiho.budgetify.data.local.entity.TransactionEntity
import dev.ohjiho.budgetify.data.model.Transaction
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface TransactionRepository {
    fun getAccountTransactions(accountId: Int): Flow<List<Transaction>>
    fun getCategoryTransactions(categoryId: Int): Flow<List<Transaction>>
}

class TransactionRepositoryImpl @Inject constructor(
    private val dao: TransactionDao,
) : BaseRepositoryImpl<TransactionEntity, TransactionDao>(dao), TransactionRepository {
    override fun getAccountTransactions(accountId: Int) = dao.getAccountTransactions(accountId)
    override fun getCategoryTransactions(categoryId: Int) = dao.getCategoryTransactions(categoryId)
}