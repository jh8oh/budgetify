package dev.ohjiho.budgetify.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import dev.ohjiho.budgetify.data.local.entity.AccountEntity
import dev.ohjiho.budgetify.data.model.Account
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for the accounts table
 */
@Dao
interface AccountDao : BaseDao<AccountEntity> {
    /**
     * Returns a list of all accounts in the database
     */
    @Query("SELECT * FROM accounts")
    fun getAllAccounts(): Flow<List<Account>>

    /**
     * Return the single account that matches the uid
     *
     * @param uid - Unique ID of the account to be returned
     */
    @Query("SELECT * FROM accounts WHERE uid = :uid")
    suspend fun getAccount(uid: Int): Account
}