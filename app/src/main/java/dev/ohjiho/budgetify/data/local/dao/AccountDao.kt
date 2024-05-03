package dev.ohjiho.budgetify.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import dev.ohjiho.budgetify.data.local.entity.AccountEntity
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal

/**
 * Data Access Object for the accounts table
 */
@Dao
interface AccountDao : BaseDao<AccountEntity> {
    /**
     * Updates a single account's current amount
     */
    @Query("UPDATE accounts SET currentAmount = (currentAmount + :amount) WHERE uid = :uid")
    fun addToAccountCurrentAmount(uid: Int, amount: BigDecimal)

    /**
     * Returns a list of all accounts in the database
     */
    @Query("SELECT * FROM accounts")
    fun getAllAccounts(): Flow<List<AccountEntity>>

    /**
     * Return the single account that matches the uid
     *
     * @param uid - Unique ID of the account to be returned
     */
    @Query("SELECT * FROM accounts WHERE uid = :uid")
    suspend fun getAccount(uid: Int): AccountEntity
}