package dev.ohjiho.budgetify.data.local

import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import dev.ohjiho.budgetify.data.local.dao.AccountDao
import dev.ohjiho.budgetify.data.local.entity.AccountEntity
import dev.ohjiho.budgetify.data.model.AccountType
import dev.ohjiho.budgetify.util.getLocale
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.util.Currency
import javax.inject.Provider

class DatabaseInitializer(private val accountProvider: Provider<AccountDao>) : RoomDatabase.Callback() {
    private val applicationScope = CoroutineScope(SupervisorJob())

    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        applicationScope.launch(Dispatchers.IO) {
            populateAccounts()
        }
    }

    private suspend fun populateAccounts() {
        val currentCurrency = Currency.getInstance(getLocale())

        with(accountProvider.get()) {
            insert(AccountEntity("Chequing", "#42a4f5", BigDecimal.ZERO, currentCurrency, false, AccountType.CHEQUING))
            insert(AccountEntity("Savings", "#f59342", BigDecimal.ZERO, currentCurrency, false, AccountType.SAVINGS))
        }
    }
}