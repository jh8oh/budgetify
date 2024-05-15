package dev.ohjiho.budgetify.domain.repository

interface BaseRepository<E>{
    suspend fun insert(entity: E): Long
    suspend fun update(entity: E)
    suspend fun delete(vararg entity: E)
}