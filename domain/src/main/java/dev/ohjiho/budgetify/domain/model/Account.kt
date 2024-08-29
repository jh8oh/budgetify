package dev.ohjiho.budgetify.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal
import java.util.Currency

enum class AccountType {
    CASH, CREDIT, INVESTMENTS;

    override fun toString(): String {
        return when (this) {
            CASH -> "Cash Accounts"
            CREDIT -> "Credit"
            INVESTMENTS -> "Investments"
        }
    }
}


/**
 * An account that the user holds
 *
 * @property uid The unique ID that refers to this account.
 * @param name The name of the account.
 * @param institution The name of the company that holds this account.
 * @param type The type of account (Cash, Credit, or Investment)
 * @param balance The amount of money that the account currently has.
 * @param currency The currency that the account uses.
 */
@Entity(tableName = "accounts")
data class Account(
    val name: String,
    val institution: String,
    val type: AccountType,
    val balance: BigDecimal,
    val currency: Currency,
) {
    @PrimaryKey(autoGenerate = true)
    var uid: Int = 0
}