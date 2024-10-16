package dev.ohjiho.budgetify.domain.repository

interface BaseRoomRepository<E>{
    suspend fun insert(entity: E): Long
    suspend fun update(entity: E)
    suspend fun delete(vararg entity: E)
}