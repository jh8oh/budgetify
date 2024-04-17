package dev.ohjiho.budgetify.data.repository

import dev.ohjiho.budgetify.data.local.dao.ConversionRateDao
import dev.ohjiho.budgetify.data.local.entity.ConversionRate
import java.util.Currency
import javax.inject.Singleton

@Singleton
class ConversionRateRepository(private val dao: ConversionRateDao) : BaseRepository<ConversionRate, ConversionRateDao>(dao) {
    suspend fun getLatestConversionRate(currency: Currency) = dao.getLatestConversionRate(currency)
}