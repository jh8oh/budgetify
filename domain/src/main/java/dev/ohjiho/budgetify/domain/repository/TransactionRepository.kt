package dev.ohjiho.budgetify.domain.repository

import dev.ohjiho.budgetify.domain.model.Transaction

interface TransactionRepository: BaseRoomRepository<Transaction> {
    suspend fun getTransaction(uid: Int): Transaction
}