package dev.ohjiho.budgetify.data.repository

import dev.ohjiho.budgetify.data.room.dao.TransactionDao
import dev.ohjiho.budgetify.domain.model.Transaction
import dev.ohjiho.budgetify.domain.repository.TransactionRepository
import javax.inject.Inject

internal class TransactionRepositoryImpl @Inject constructor(private val dao: TransactionDao) :
    BaseRoomRepositoryImpl<Transaction, TransactionDao>(dao), TransactionRepository {
    override suspend fun getTransaction(uid: Int) = dao.getTransaction(uid)
}