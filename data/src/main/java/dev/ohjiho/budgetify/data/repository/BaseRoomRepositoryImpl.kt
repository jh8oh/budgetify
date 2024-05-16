package dev.ohjiho.budgetify.data.repository

import androidx.annotation.WorkerThread
import dev.ohjiho.budgetify.data.room.dao.BaseDao
import dev.ohjiho.budgetify.domain.repository.BaseRoomRepository

internal abstract class BaseRoomRepositoryImpl<E, D : BaseDao<E>>(private val dao: D): BaseRoomRepository<E> {

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