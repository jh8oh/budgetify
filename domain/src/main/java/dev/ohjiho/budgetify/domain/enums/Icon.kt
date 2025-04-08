package dev.ohjiho.budgetify.domain.enums

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import dev.ohjiho.budgetify.theme.R

enum class Icon(@DrawableRes val drawableRes: Int, @ColorRes val colorRes: Int, private val isExpense: Boolean) {
    // Transfer Icons
    TRANSFER(R.drawable.category_ic_transfer, R.color.black_400, false),

    // Expense Icons
    APPAREL(R.drawable.category_ic_apparel, R.color.orchid, true),
    DONATION(R.drawable.category_ic_donation, R.color.forest_green, true),
    EDUCATION(R.drawable.category_ic_education, R.color.cadet_blue, true),
    GIFT(R.drawable.category_ic_gift, R.color.brick_red, true),
    GROCERY(R.drawable.category_ic_grocery, R.color.olive_drab, true),
    HOME(R.drawable.category_ic_home, R.color.goldenrod, true),
    HOUSEHOLD_SUPPLIES(R.drawable.category_ic_householdsupplies, R.color.deep_purple, true),
    MAKEUP(R.drawable.category_ic_makeup, R.color.hot_pink, true),
    MEDICAL(R.drawable.category_ic_medical, R.color.crimson, true),
    MOVIE(R.drawable.category_ic_movie, R.color.dark_violet, true),
    PET(R.drawable.category_ic_pet, R.color.thistle, true),
    PIANO(R.drawable.category_ic_piano, R.color.black_700, true),
    POWER(R.drawable.category_ic_power, R.color.light_khaki, true),
    REPAIR(R.drawable.category_ic_repair, R.color.aqua_marine, true),
    RESTAURANT(R.drawable.category_ic_restaurant, R.color.dodger_blue, true),
    SHOPPING(R.drawable.category_ic_shopping, R.color.mulberry, true),
    SNACK(R.drawable.category_ic_snack, R.color.raspberry, true),
    SPA(R.drawable.category_ic_spa, R.color.medium_sea_green, true),
    SPORTS(R.drawable.category_ic_sports, R.color.burnt_orange, true),
    STROLLER(R.drawable.category_ic_stroller, R.color.tomato, true),
    SUBSCRIPTIONS(R.drawable.category_ic_subscriptions, R.color.slate_blue, true),
    TAXI(R.drawable.category_ic_taxi, R.color.amber, true),
    TRANSPORT(R.drawable.category_ic_transport, R.color.sapphire, true),
    TRAVEL(R.drawable.category_ic_travel, R.color.deep_sky_blue, true),
    VIDEOGAMES(R.drawable.category_ic_videogames, R.color.light_slate, true),

    // Income Icons
    BILLS(R.drawable.category_ic_bills, R.color.forest_green, false),
    BITCOIN(R.drawable.category_ic_bitcoin, R.color.sapphire, false),
    CREDIT(R.drawable.category_ic_credit, R.color.amber, false),
    INVESTMENT(R.drawable.category_ic_investment, R.color.cadet_blue, false),
    MONEY_IN(R.drawable.category_ic_money_in, R.color.dark_violet, false),
    REFUND(R.drawable.category_ic_refund, R.color.deep_purple, false);

    companion object {
        fun getExpenseIcons() = Icon.entries.filter { it.isExpense }
        fun getIncomeIcons() = Icon.entries.filter { !it.isExpense && it != TRANSFER }
    }
}