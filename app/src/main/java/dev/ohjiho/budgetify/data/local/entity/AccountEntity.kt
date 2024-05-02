package dev.ohjiho.budgetify.data.local.entity

import android.icu.util.Currency
import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.ohjiho.budgetify.data.model.Account
import java.math.BigDecimal

@Entity(tableName = "accounts")
data class AccountEntity(
    override var name: String,
    override var color: String,
    override var startingAmount: BigDecimal,
    override var currentAmount: BigDecimal,
    override var currency: Currency,
    override var isFavourite: Boolean,
    override var isSavings: Boolean,
) : Account {
    @PrimaryKey(autoGenerate = true)
    override var uid: Int = 0
}