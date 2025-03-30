package dev.ohjiho.budgetify.domain.enums

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import dev.ohjiho.budgetify.theme.R

enum class Icon(@DrawableRes val drawableRes: Int, @ColorRes val colorRes: Int, private val isExpense: Boolean) {
    // Transfer Icons
    TRANSFER(R.drawable.category_ic_transfer, R.color.black_400, false),

    // Expense Icons
    APPAREL(R.drawable.category_ic_apparel, R.color.brown, true),
    DONATION(R.drawable.category_ic_donation, R.color.dark_golden_rod, true),
    EDUCATION(R.drawable.category_ic_education, R.color.dark_slate_blue, true),
    GIFT(R.drawable.category_ic_gift, R.color.dark_cyan, true),
    GROCERY(R.drawable.category_ic_grocery, R.color.steel_blue, true),
    HOME(R.drawable.category_ic_home, R.color.light_blue, true),
    HOUSEHOLD_SUPPLIES(R.drawable.category_ic_householdsupplies, R.color.dark_sea_green, true),
    MAKEUP(R.drawable.category_ic_makeup, R.color.maroon, true),
    MEDICAL(R.drawable.category_ic_medical, R.color.dark_magenta, true),
    MOVIE(R.drawable.category_ic_movie, R.color.indian_red, true),
    PET(R.drawable.category_ic_pet, R.color.lime_green, true),
    PIANO(R.drawable.category_ic_piano, R.color.black_700, true),
    POWER(R.drawable.category_ic_power, R.color.navajo_white, true),
    REPAIR(R.drawable.category_ic_repair, R.color.coral, true),
    RESTAURANT(R.drawable.category_ic_restaurant, R.color.dark_olive_green, true),
    SHOPPING(R.drawable.category_ic_shopping, R.color.pale_violet_red, true),
    SNACK(R.drawable.category_ic_snack, R.color.purple, true),
    SPA(R.drawable.category_ic_spa, R.color.khaki, true),
    SPORTS(R.drawable.category_ic_sports, R.color.orange, true),
    STROLLER(R.drawable.category_ic_stroller, R.color.pale_green, true),
    SUBSCRIPTIONS(R.drawable.category_ic_subscriptions, R.color.lavender, true),
    TAXI(R.drawable.category_ic_taxi, R.color.violet, true),
    TRANSPORT(R.drawable.category_ic_transport, R.color.plum, true),
    TRAVEL(R.drawable.category_ic_travel, R.color.blue_violet, true),
    VIDEOGAMES(R.drawable.category_ic_videogames, R.color.dodger_blue, true),

    // Income Icons
    BILLS(R.drawable.category_ic_bills, R.color.spring_green, false),
    BITCOIN(R.drawable.category_ic_bitcoin, R.color.forest_green, false),
    CREDIT(R.drawable.category_ic_credit, R.color.burly_wood, false),
    INVESTMENT(R.drawable.category_ic_investment, R.color.green_yellow, false),
    MONEY_IN(R.drawable.category_ic_money_in, R.color.royal_blue, false),
    REFUND(R.drawable.category_ic_refund, R.color.cyan, false);

    companion object {
        fun getExpenseIcons() = Icon.entries.filter { it.isExpense }
        fun getIncomeIcons() = Icon.entries.filter { !it.isExpense && it != TRANSFER }
    }
}