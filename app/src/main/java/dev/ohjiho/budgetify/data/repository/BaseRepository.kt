package dev.ohjiho.budgetify.data.repository

import androidx.annotation.WorkerThread
import dev.ohjiho.budgetify.data.local.dao.BaseDao

interface BaseRepository<E>{
    suspend fun insert(entity: E): Long
    suspend fun update(entity: E)
    suspend fun delete(vararg entity: E)
}

abstract class BaseRepositoryImpl<E, D : BaseDao<E>>(private val dao: D): BaseRepository<E> {

    @WorkerThread
    override suspend fun insert(entity: E): Long {
        return dao.insert(entity)
    }

    @WorkerThread
    override suspend fun update(entity: E) {
        dao.update(entity)
    }

    @WorkerThread
    override suspend fun delete(vararg entity: E) {
        dao.delete(*entity)
    }
}