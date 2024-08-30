package dev.ohjiho.budgetify.theme.icons

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import dev.ohjiho.budgetify.theme.R

enum class Icon(@DrawableRes val res: Int, @ColorRes val colorRes: Int, private val isExpense: Boolean) {
    APPAREL(R.drawable.cat_exp_apparel, R.color.saddle_brown, true),
    BANK(R.drawable.cat_exp_bank, R.color.forest_green, true),
    BASKETBALL(R.drawable.cat_exp_basketball, R.color.orange, true),
    DINING(R.drawable.cat_exp_dining, R.color.olive, true),
    EDUCATION(R.drawable.cat_exp_education, R.color.dark_slate_blue, true),
    GIFT(R.drawable.cat_exp_gift, R.color.dark_cyan, true),
    GROCERY(R.drawable.cat_exp_grocery, R.color.steel_blue, true),
    HOME(R.drawable.cat_exp_home, R.color.dark_blue, true),
    HOUSEHOLD_SUPPLIES(R.drawable.cat_exp_householdsupplies, R.color.dark_sea_green, true),
    ICE_CREAM(R.drawable.cat_exp_icecream, R.color.fuchsia, true),
    LIGHTBULB(R.drawable.cat_exp_lightbulb, R.color.yellow, true),
    MEDICAL(R.drawable.cat_exp_medical, R.color.dark_magenta, true),
    MOVIE(R.drawable.cat_exp_movie, R.color.red, true),
    PET(R.drawable.cat_exp_pet, R.color.lime, true),
    PIANO(R.drawable.cat_exp_piano, com.google.android.material.R.attr.colorOnBackground, true),
    PLANE(R.drawable.cat_exp_plane, R.color.blue_violet, true),
    SHOPPING(R.drawable.cat_exp_shopping, R.color.crimson, true),
    STROLLER(R.drawable.cat_exp_stroller, R.color.pale_green, true),
    SUBSCRIPTION(R.drawable.cat_exp_subscription, R.color.blue, true),
    TAXI(R.drawable.cat_exp_taxi, R.color.burly_wood, true),
    TOOLS(R.drawable.cat_exp_tools, R.color.coral, true),
    TRAIN(R.drawable.cat_exp_train, R.color.deep_pink, true),
    VIDEOGAMES(R.drawable.cat_exp_videogames, R.color.dodger_blue, true),
    BILLS(R.drawable.cat_inc_bills, R.color.pale_violet_red, false),
    COINS(R.drawable.cat_inc_coins, R.color.light_blue, false),
    INVESTMENT(R.drawable.cat_inc_investment, R.color.green_yellow, false),
    MONEY_IN(R.drawable.cat_inc_money_in, R.color.violet, false),
    REFUND(R.drawable.cat_inc_refund, R.color.cyan, false);

    fun getExpenseIcons() = Icon.entries.filter { it.isExpense }
    fun getIncomeIcons() = Icon.entries.filter { !it.isExpense }
}