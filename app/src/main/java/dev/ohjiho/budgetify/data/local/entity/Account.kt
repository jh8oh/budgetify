package dev.ohjiho.budgetify.data.local.entity

import android.graphics.Color
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal
import java.util.Currency

/**
 * An account that the user holds
 *
 * @property uid - The unique ID that refers to this account.
 * @param name - The name of the account.
 * @param color - The color of the icon.
 * @param startingAmount - The amount that the account started out with.
 * @param amount - The amount that the account currently has.
 * @param currency - The currency that the account uses.
 * @param isFavourite - Whether the account has been tagged as "favourite" by the user.
 * @param isSavings - Whether the account is considered as being set aside for future use.
 * @param isEmergencySavings - Whether the account is considered as being set aside for unexpected expenses.
 */
@Entity(tableName = "accounts")
data class Account(
    var name: String,
    var color: Color,
    var startingAmount: BigDecimal,
    var amount: BigDecimal,
    var currency: Currency,
    var isFavourite: Boolean,
    var isSavings: Boolean,
    var isEmergencySavings: Boolean,
) {
    @PrimaryKey(autoGenerate = true)
    var uid: Int = 0
}