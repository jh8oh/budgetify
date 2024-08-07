package dev.ohjiho.budgetify.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal
import java.util.Currency

enum class AccountType {
    LIQUID, DEBT, INVESTMENTS;

    override fun toString(): String {
        return when (this) {
            LIQUID -> "Cash Accounts"
            DEBT -> "Credit & Loans"
            INVESTMENTS -> "Investments"
        }
    }
}

@Entity(tableName = "accounts")
data class AccountEntity(
    var name: String,
    var institution: String,
    var accountType: AccountType,
    var balance: BigDecimal,
    var currency: Currency,
) {
    @PrimaryKey(autoGenerate = true)
    var uid: Int = 0

    companion object {
        fun newInstance(currency: Currency = Currency.getInstance("USD")) = AccountEntity(
            "",
            "",
            AccountType.LIQUID,
            BigDecimal.ZERO,
            currency
        )
    }
}