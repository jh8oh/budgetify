package dev.ohjiho.budgetify.data.repository

import dev.ohjiho.budgetify.data.local.dao.TransactionDao
import dev.ohjiho.budgetify.data.local.entity.Account
import dev.ohjiho.budgetify.data.local.entity.Category
import dev.ohjiho.budgetify.data.local.entity.Transaction
import javax.inject.Singleton

@Singleton
class TransactionRepository(private val dao: TransactionDao) : BaseRepository<Transaction, TransactionDao>(dao) {
    fun getTransaction(tagName: String) = dao.getTransactions(tagName)
    fun getTransaction(category: Category) = dao.getCategoryTransactions(category.uid)
    fun getTransaction(account: Account) = dao.getAccountTransactions(account.uid)
}