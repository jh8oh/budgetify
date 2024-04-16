package dev.ohjiho.budgetify.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import dev.ohjiho.budgetify.data.local.entity.ConversionRate
import kotlinx.coroutines.flow.Flow
import java.util.Currency

/**
 * Data Access Object for the conversion_rates table
 */
@Dao
interface ConversionRateDao: BaseDao<ConversionRate> {
    /**
     * Returns all the conversion rate for the currency (in either from or to)
     *
     * @param currency - Currency to look for in conversion rate
     */
    @Query("SELECT * FROM conversion_rates WHERE (from_iso_code = :currency OR to_iso_code = :currency)")
    fun getConversionRate(currency: Currency): Flow<List<ConversionRate>>
}