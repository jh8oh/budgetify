package dev.ohjiho.budgetify.domain.repository

import dev.ohjiho.budgetify.domain.model.Account
import kotlinx.coroutines.flow.Flow

interface AccountRepository : BaseRoomRepository<Account> {
    suspend fun getAccount(uid: Int): Account
    fun getAllAccounts(): Flow<List<Account>>
    fun getAllUniqueInstitutions(): Flow<List<String>>
}