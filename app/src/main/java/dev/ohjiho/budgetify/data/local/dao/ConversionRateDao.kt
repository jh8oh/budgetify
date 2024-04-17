package dev.ohjiho.budgetify.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import dev.ohjiho.budgetify.data.local.entity.ConversionRate
import java.util.Currency

/**
 * Data Access Object for the conversion_rates table
 */
@Dao
interface ConversionRateDao : BaseDao<ConversionRate> {
    /**
     * Returns all the conversion rate for the currency (in either from or to)
     *
     * @param currency - Currency to look for in conversion rate
     */
    @Query("SELECT * FROM conversion_rates WHERE to_iso_code = :currency AND date = (SELECT MAX(date) FROM conversion_rates WHERE to_iso_code = :currency)")
    suspend fun getLatestConversionRate(currency: Currency): ConversionRate
}