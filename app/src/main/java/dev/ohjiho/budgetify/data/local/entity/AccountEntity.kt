package dev.ohjiho.budgetify.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.ohjiho.budgetify.data.model.Account
import dev.ohjiho.budgetify.data.model.AccountType
import java.math.BigDecimal
import java.util.Currency

@Entity(tableName = "accounts")
data class AccountEntity(
    override var name: String,
    override var color: String,
    override var startingAmount: BigDecimal,
    override var currency: Currency,
    override var isFavourite: Boolean,
    override var accountType: AccountType
) : Account {
    @PrimaryKey(autoGenerate = true)
    override var uid: Int = 0

    override var currentAmount: BigDecimal = startingAmount
}