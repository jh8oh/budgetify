package dev.ohjiho.budgetify.domain.repository

interface SetUpRepository {
    fun isSetUp(): Boolean
    fun completeSetUp()
}