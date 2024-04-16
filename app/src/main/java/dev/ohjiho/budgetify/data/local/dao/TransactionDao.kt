package dev.ohjiho.budgetify.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.RewriteQueriesToDropUnusedColumns
import dev.ohjiho.budgetify.data.local.entity.Transaction
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for the transactions table
 */
@Dao
interface TransactionDao: BaseDao<Transaction>{
    /**
     * Returns a list of transactions that contain the tag
     *
     * @param tagName - Name of the tag
     */
    @RewriteQueriesToDropUnusedColumns
    @Query("SELECT * FROM transactions INNER JOIN transaction_tag_cross_refs ON transaction_id = uid WHERE tag_name = :tagName")
    fun getTransactions(tagName: String): Flow<List<Transaction>>

    /**
     * Returns a list of transactions that falls under the category
     *
     * @param categoryId - Id of the category
     */
    @Query("SELECT * FROM transactions WHERE category_id = :categoryId")
    fun getCategoryTransactions(categoryId: Int): Flow<List<Transaction>>

    /**
     * Returns a list of transactions that falls under the account
     *
     * @param accountId - Id of the account
     */
    @Query("SELECT * FROM transactions WHERE account_id = :accountId")
    fun getAccountTransactions(accountId: Int): Flow<List<Transaction>>
}