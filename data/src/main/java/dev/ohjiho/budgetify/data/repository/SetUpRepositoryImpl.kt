package dev.ohjiho.budgetify.data.repository

import dev.ohjiho.budgetify.data.sharedprefs.SetUpSharedPrefs
import dev.ohjiho.budgetify.domain.repository.SetUpRepository
import javax.inject.Inject

internal class SetUpRepositoryImpl @Inject constructor(private val sharedPrefs: SetUpSharedPrefs) : SetUpRepository {
    override fun isSetUp(): Boolean = sharedPrefs.isSetUp

    override fun completeSetUp() {
        sharedPrefs.isSetUp = true
    }
}