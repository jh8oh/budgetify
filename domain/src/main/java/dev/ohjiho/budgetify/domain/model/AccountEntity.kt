package dev.ohjiho.budgetify.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal
import java.util.Currency

enum class AccountType {
    LIQUID, CREDIT, INVESTMENTS;

    override fun toString(): String {
        return when (this) {
            LIQUID -> "Cash Accounts"
            CREDIT -> "Credit"
            INVESTMENTS -> "Investments"
        }
    }
}

@Entity(tableName = "accounts")
data class AccountEntity(
    var name: String,
    var institution: String,
    var type: AccountType,
    var balance: BigDecimal,
    var currency: Currency,
) {
    @PrimaryKey(autoGenerate = true)
    var uid: Int = 0
}