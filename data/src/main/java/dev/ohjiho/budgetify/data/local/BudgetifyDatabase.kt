package dev.ohjiho.budgetify.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dev.ohjiho.budgetify.data.local.dao.AccountDao
import dev.ohjiho.budgetify.data.local.util.Converters
import dev.ohjiho.budgetify.domain.model.AccountEntity

@Database(
    entities = [AccountEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
internal abstract class BudgetifyDatabase : RoomDatabase() {
    abstract fun accountDao(): AccountDao
}