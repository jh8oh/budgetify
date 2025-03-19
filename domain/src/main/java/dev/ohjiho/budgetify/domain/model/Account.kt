package dev.ohjiho.budgetify.domain.model

import androidx.annotation.DrawableRes
import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.ohjiho.budgetify.theme.R
import java.math.BigDecimal
import java.util.Currency

enum class AccountType {
    CASH, CREDIT, INVESTMENTS;

    @DrawableRes
    fun getIconRes(): Int {
        return when (this) {
            CASH -> R.drawable.ic_inc_bills
            CREDIT -> R.drawable.ic_inc_credit
            INVESTMENTS -> R.drawable.ic_inc_investment
        }
    }

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
 * @property uid The unique identifier that refers to this account.
 * @property name The name of the account.
 * @property institution The name of the company that holds this account.
 * @property type The type of account (Cash, Credit, or Investment)
 * @property balance The amount of money that the account currently has.
 * @property currency The currency that the account uses.
 */
@Entity(tableName = "accounts")
data class Account(
    @PrimaryKey(autoGenerate = true) val uid: Int,
    val name: String,
    val institution: String,
    val type: AccountType,
    val balance: BigDecimal,
    val currency: Currency,
)