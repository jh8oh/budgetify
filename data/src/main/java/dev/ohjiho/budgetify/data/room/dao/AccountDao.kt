package dev.ohjiho.budgetify.data.room.dao

import androidx.room.Dao
import androidx.room.Query
import dev.ohjiho.budgetify.domain.model.Account
import kotlinx.coroutines.flow.Flow

@Dao
internal interface AccountDao : BaseDao<Account> {
    @Query("SELECT * FROM accounts WHERE uid = :uid")
    suspend fun getAccount(uid: Int): Account

    @Query("SELECT * FROM accounts ORDER BY type ASC, name ASC")
    fun getAllAccounts(): Flow<List<Account>>

    @Query("SELECT DISTINCT institution FROM accounts")
    fun getAllUniqueInstitutions(): Flow<List<String>>
}