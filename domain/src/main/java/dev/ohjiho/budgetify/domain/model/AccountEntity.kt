package dev.ohjiho.budgetify.domain.model

import androidx.annotation.ColorInt
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal
import java.util.Currency

enum class AccountType {
    CHEQUING, SAVINGS, CREDIT, INVESTMENTS;

    override fun toString(): String {
        return name.first() + name.substring(1).lowercase()
    }
}

@Entity(tableName = "accounts")
data class AccountEntity(
    var name: String,
    @ColorInt var colorInt: Int,
    var accountType: AccountType,
    var balance: BigDecimal,
    var currency: Currency,
) {
    @PrimaryKey(autoGenerate = true)
    var uid: Int = 0

    var previousId: Int = 0

    companion object {
        // Translates to grey_500
        private const val DEFAULT_COLOR = -6381922

        fun newInstance(currency: Currency = Currency.getInstance("USD")) = AccountEntity(
            "",
            DEFAULT_COLOR,
            AccountType.CHEQUING,
            BigDecimal.ZERO,
            currency
        )
    }
}