package dev.ohjiho.budgetify.data.repository

import dev.ohjiho.budgetify.data.local.dao.TagDao
import dev.ohjiho.budgetify.data.local.entity.Tag
import javax.inject.Singleton

@Singleton
class TagRepository(private val dao: TagDao): BaseRepository<Tag, TagDao>(dao) {
    fun getTags(transactionId: Int) = dao.getTags(transactionId)
}