package dev.ohjiho.budgetify.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import dev.ohjiho.budgetify.data.local.entity.TransactionEntity
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal

/**
 * Data Access Object for the transactions table
 */
@Dao
interface TransactionDao : BaseDao<TransactionEntity> {
    /**
     * Returns this transaction's amount
     *
     * @param uid Transaction's unique ID
     */
    @Query("SELECT amount FROM transactions WHERE uid = :uid")
    suspend fun getTransactionAmount(uid: Int): BigDecimal

    /**
     * Returns a list of transactions that falls under the account
     *
     * @param accountId ID of the account
     */
    @Query("SELECT * FROM transactions WHERE accountId = :accountId")
    fun getAccountTransactions(accountId: Int): Flow<List<TransactionEntity>>

    /**
     * Returns a list of transactions that falls under the category
     *
     * @param categoryId ID of the category
     */
    @Query("SELECT * FROM transactions WHERE categoryId = :categoryId")
    fun getCategoryTransactions(categoryId: Int): Flow<List<TransactionEntity>>
}