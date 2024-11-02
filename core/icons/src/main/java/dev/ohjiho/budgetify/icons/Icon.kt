package dev.ohjiho.budgetify.icons

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import dev.ohjiho.budgetify.theme.R as themeR

enum class Icon(@DrawableRes val drawableRes: Int, @ColorRes val colorRes: Int, private val isExpense: Boolean) {
    // Transfer Icons
    TRANSFER(R.drawable.ic_transfer, themeR.color.black_400, false),

    // Expense Icons
    APPAREL(R.drawable.ic_exp_apparel, themeR.color.brown, true),
    BANK(R.drawable.ic_exp_bank, themeR.color.forest_green, true),
    BASKETBALL(R.drawable.ic_exp_basketball, themeR.color.orange, true),
    DINING(R.drawable.ic_exp_dining, themeR.color.dark_olive_green, true),
    DONATE(R.drawable.ic_exp_donate, themeR.color.dark_golden_rod, true),
    EDUCATION(R.drawable.ic_exp_education, themeR.color.dark_slate_blue, true),
    GIFT(R.drawable.ic_exp_gift, themeR.color.dark_cyan, true),
    GROCERY(R.drawable.ic_exp_grocery, themeR.color.steel_blue, true),
    HOME(R.drawable.ic_exp_home, themeR.color.light_blue, true),
    HOUSEHOLD_SUPPLIES(R.drawable.ic_exp_householdsupplies, themeR.color.dark_sea_green, true),
    ICE_CREAM(R.drawable.ic_exp_icecream, themeR.color.purple, true),
    LIGHTBULB(R.drawable.ic_exp_lightbulb, themeR.color.navajo_white, true),
    MEDICAL(R.drawable.ic_exp_medical, themeR.color.dark_magenta, true),
    MOVIE(R.drawable.ic_exp_movie, themeR.color.indian_red, true),
    PET(R.drawable.ic_exp_pet, themeR.color.lime_green, true),
    PIANO(R.drawable.ic_exp_piano, themeR.color.black_700, true),
    PLANE(R.drawable.ic_exp_plane, themeR.color.blue_violet, true),
    RECEIPT(R.drawable.ic_exp_receipt, themeR.color.yellow_green, true),
    SELFCARE(R.drawable.ic_exp_selfcare, themeR.color.maroon, true),
    SHOPPING(R.drawable.ic_exp_shopping, themeR.color.pale_violet_red, true),
    SPA(R.drawable.ic_exp_spa, themeR.color.khaki, true),
    STROLLER(R.drawable.ic_exp_stroller, themeR.color.pale_green, true),
    SUBSCRIPTION(R.drawable.ic_exp_subscription, themeR.color.lavender, true),
    TAXI(R.drawable.ic_exp_taxi, themeR.color.violet, true),
    TOOLS(R.drawable.ic_exp_tools, themeR.color.coral, true),
    TRAIN(R.drawable.ic_exp_train, themeR.color.plum, true),
    VIDEOGAMES(R.drawable.ic_exp_videogames, themeR.color.dodger_blue, true),

    // Income Icons
    BILLS(R.drawable.ic_inc_bills, themeR.color.spring_green, false),
    COINS(R.drawable.ic_inc_coins, themeR.color.burly_wood, false),
    INVESTMENT(R.drawable.ic_inc_investment, themeR.color.green_yellow, false),
    MONEY_IN(R.drawable.ic_inc_money_in, themeR.color.royal_blue, false),
    REFUND(R.drawable.ic_inc_refund, themeR.color.cyan, false);

    companion object {
        fun getExpenseIcons() = Icon.entries.filter { it.isExpense }
        fun getIncomeIcons() = Icon.entries.filter { !it.isExpense && it != TRANSFER }
    }
}