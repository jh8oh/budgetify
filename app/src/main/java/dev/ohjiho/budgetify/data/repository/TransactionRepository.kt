package dev.ohjiho.budgetify.data.repository

import dev.ohjiho.budgetify.data.local.dao.TransactionDao
import dev.ohjiho.budgetify.data.local.entity.TransactionEntity
import javax.inject.Inject

interface TransactionRepository {
}

class TransactionRepositoryImpl @Inject constructor(
    private val dao: TransactionDao,
) : BaseRepositoryImpl<TransactionEntity, TransactionDao>(dao), TransactionRepository