package dev.ohjiho.budgetify.data.injection

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.ohjiho.budgetify.data.room.BudgetifyDatabase
import dev.ohjiho.budgetify.data.room.dao.AccountDao
import dev.ohjiho.budgetify.data.room.dao.BudgetDao
import dev.ohjiho.budgetify.data.room.dao.CategoryDao
import dev.ohjiho.budgetify.data.room.dao.TransactionDao
import dev.ohjiho.budgetify.data.room.util.insertFromAssets
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object RoomModule {
    private const val BUDGETIFY_DATABASE_NAME = "budgetify"

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): BudgetifyDatabase {
        val database = Room.databaseBuilder(
            context.applicationContext,
            BudgetifyDatabase::class.java,
            BUDGETIFY_DATABASE_NAME
        ).addCallback(object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                db.apply {
                    beginTransaction()
                    try {
                        insertFromAssets(context, "category.sql")
                        setTransactionSuccessful()
                    } finally {
                        endTransaction()
                    }
                }
            }
        }).fallbackToDestructiveMigration().build()

        return database
    }

    @Provides
    fun provideAccountDao(database: BudgetifyDatabase): AccountDao = database.accountDao()

    @Provides
    fun provideBudgetDao(database: BudgetifyDatabase): BudgetDao = database.budgetDao()

    @Provides
    fun provideCategoryDao(database: BudgetifyDatabase): CategoryDao = database.categoryDao()

    @Provides
    fun provideTransactionDao(database: BudgetifyDatabase): TransactionDao = database.transactionDao()
}