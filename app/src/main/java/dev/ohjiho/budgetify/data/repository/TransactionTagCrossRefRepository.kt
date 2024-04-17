package dev.ohjiho.budgetify.data.repository

import dev.ohjiho.budgetify.data.local.dao.TransactionTagCrossRefDao
import dev.ohjiho.budgetify.data.local.entity.TransactionTagCrossRef
import javax.inject.Singleton

@Singleton
class TransactionTagCrossRefRepository(dao: TransactionTagCrossRefDao) :
    BaseRepository<TransactionTagCrossRef, TransactionTagCrossRefDao>(dao)