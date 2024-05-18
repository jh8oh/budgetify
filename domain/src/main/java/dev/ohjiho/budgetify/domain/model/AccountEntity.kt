package dev.ohjiho.budgetify.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal
import java.time.LocalDate
import java.util.Currency

@Entity(tableName = "accounts")
data class AccountEntity(
    var previousId: Int,
    var name: String,
    var color: String,
    var balance: BigDecimal,
    var currency: Currency,
    var creationDate: LocalDate = LocalDate.now()
) {
    @PrimaryKey(autoGenerate = true)
    var uid: Int = 0
}