package dev.ohjiho.budgetify.theme.icons

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import dev.ohjiho.budgetify.theme.R

enum class Icon(@DrawableRes val drawableRes: Int, @ColorRes val colorRes: Int, private val isExpense: Boolean) {
    // Expense Icons
    APPAREL(R.drawable.cat_exp_apparel, R.color.brown, true),
    BANK(R.drawable.cat_exp_bank, R.color.forest_green, true),
    BASKETBALL(R.drawable.cat_exp_basketball, R.color.orange, true),
    DINING(R.drawable.cat_exp_dining, R.color.dark_olive_green, true),
    DONATE(R.drawable.cat_exp_donate, R.color.dark_golden_rod,true),
    EDUCATION(R.drawable.cat_exp_education, R.color.dark_slate_blue, true),
    GIFT(R.drawable.cat_exp_gift, R.color.dark_cyan, true),
    GROCERY(R.drawable.cat_exp_grocery, R.color.steel_blue, true),
    HOME(R.drawable.cat_exp_home, R.color.light_blue, true),
    HOUSEHOLD_SUPPLIES(R.drawable.cat_exp_householdsupplies, R.color.dark_sea_green, true),
    ICE_CREAM(R.drawable.cat_exp_icecream, R.color.purple, true),
    LIGHTBULB(R.drawable.cat_exp_lightbulb, R.color.navajo_white, true),
    MEDICAL(R.drawable.cat_exp_medical, R.color.dark_magenta, true),
    MOVIE(R.drawable.cat_exp_movie, R.color.indian_red, true),
    PET(R.drawable.cat_exp_pet, R.color.lime_green, true),
    PIANO(R.drawable.cat_exp_piano, com.google.android.material.R.attr.colorOnBackground, true),
    PLANE(R.drawable.cat_exp_plane, R.color.blue_violet, true),
    RECEIPT(R.drawable.cat_exp_receipt, R.color.yellow_green,true),
    SELFCARE(R.drawable.cat_exp_selfcare, R.color.maroon,true),
    SHOPPING(R.drawable.cat_exp_shopping, R.color.pale_violet_red, true),
    SPA(R.drawable.cat_exp_spa, R.color.khaki, true),
    STROLLER(R.drawable.cat_exp_stroller, R.color.pale_green, true),
    SUBSCRIPTION(R.drawable.cat_exp_subscription, R.color.lavender, true),
    TAXI(R.drawable.cat_exp_taxi, R.color.violet, true),
    TOOLS(R.drawable.cat_exp_tools, R.color.coral, true),
    TRAIN(R.drawable.cat_exp_train, R.color.plum, true),
    VIDEOGAMES(R.drawable.cat_exp_videogames, R.color.dodger_blue, true),
    // Income Icons
    BILLS(R.drawable.cat_inc_bills, R.color.spring_green, false),
    COINS(R.drawable.cat_inc_coins, R.color.burly_wood, false),
    INVESTMENT(R.drawable.cat_inc_investment, R.color.green_yellow, false),
    MONEY_IN(R.drawable.cat_inc_money_in, R.color.royal_blue, false),
    REFUND(R.drawable.cat_inc_refund, R.color.cyan, false);

    fun getExpenseIcons() = Icon.entries.filter { it.isExpense }
    fun getIncomeIcons() = Icon.entries.filter { !it.isExpense }
}