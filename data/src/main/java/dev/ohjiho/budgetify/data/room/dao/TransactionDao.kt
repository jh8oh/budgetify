package dev.ohjiho.budgetify.data.room.dao

import androidx.room.Dao
import androidx.room.Query
import dev.ohjiho.budgetify.domain.model.Transaction

@Dao
internal interface TransactionDao: BaseDao<Transaction> {
    @Query("SELECT * FROM transactions WHERE :uid = uid")
    suspend fun getTransaction(uid: Int): Transaction
}