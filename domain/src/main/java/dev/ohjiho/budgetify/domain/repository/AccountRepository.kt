package dev.ohjiho.budgetify.domain.repository

import dev.ohjiho.budgetify.domain.model.AccountEntity
import kotlinx.coroutines.flow.Flow

interface AccountRepository : BaseRoomRepository<AccountEntity> {
    suspend fun getAccount(uid: Int): AccountEntity
    fun getAllAccounts(): Flow<List<AccountEntity>>
    fun getAllUniqueInstitutions(): Flow<List<String>>
}