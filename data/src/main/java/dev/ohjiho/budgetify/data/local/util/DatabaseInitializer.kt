package dev.ohjiho.budgetify.data.local.util

import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import dev.ohjiho.budgetify.data.local.dao.AccountDao
import dev.ohjiho.budgetify.domain.model.AccountEntity
import dev.ohjiho.budgetify.domain.model.AccountType
import dev.ohjiho.budgetify.utils.getLocale
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.util.Currency
import javax.inject.Provider

internal class DatabaseInitializer(private val accountProvider: Provider<AccountDao>) : RoomDatabase.Callback() {
    private val applicationScope = CoroutineScope(SupervisorJob())

    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        applicationScope.launch(Dispatchers.IO) {
            populateAccounts()
        }
    }

    private suspend fun populateAccounts() {
        val currency = Currency.getInstance(getLocale())

        with(accountProvider.get()) {
            insert(AccountEntity("Chequing", "#42a4f5", AccountType.CHEQUING, BigDecimal.ZERO, currency))
            insert(AccountEntity("Savings", "#f59342", AccountType.SAVINGS, BigDecimal.ZERO, currency))
        }
    }
}