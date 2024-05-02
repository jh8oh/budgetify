package dev.ohjiho.budgetify.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import dev.ohjiho.budgetify.data.local.entity.TransactionEntity
import dev.ohjiho.budgetify.data.model.Transaction
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for the transactions table
 */
@Dao
interface TransactionDao : BaseDao<TransactionEntity> {
    /**
     * Returns a list of transactions that falls under the account
     *
     * @param accountId - Id of the account
     */
    @Query("SELECT * FROM transactions WHERE accountId = :accountId")
    fun getAccountTransactions(accountId: Int): Flow<List<Transaction>>

    /**
     * Returns a list of transactions that falls under the category
     *
     * @param categoryId - Id of the category
     */
    @Query("SELECT * FROM transactions WHERE categoryId = :categoryId")
    fun getCategoryTransactions(categoryId: Int): Flow<List<Transaction>>
}