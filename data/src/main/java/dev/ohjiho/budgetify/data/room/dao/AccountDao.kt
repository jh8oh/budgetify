package dev.ohjiho.budgetify.data.room.dao

import androidx.room.Dao
import androidx.room.Query
import dev.ohjiho.budgetify.domain.model.AccountEntity
import kotlinx.coroutines.flow.Flow

@Dao
internal interface AccountDao : BaseDao<AccountEntity> {
    @Query("SELECT * FROM accounts WHERE uid = :uid")
    suspend fun getAccount(uid: Int): AccountEntity

    @Query("SELECT * FROM accounts")
    fun getAllAccounts(): Flow<List<AccountEntity>>
}