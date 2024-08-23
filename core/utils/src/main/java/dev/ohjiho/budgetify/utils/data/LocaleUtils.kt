package dev.ohjiho.budgetify.utils.data

import android.content.Context
import java.util.Locale

private var LOCALE_INSTANCE: Locale? = null

fun getLocale(context: Context): Locale = context.resources.configuration.locales[0].also { LOCALE_INSTANCE = it }

fun getLocale(): Locale = LOCALE_INSTANCE ?: Locale.getDefault()