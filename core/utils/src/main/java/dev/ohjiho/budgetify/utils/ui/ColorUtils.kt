package dev.ohjiho.budgetify.utils.ui

import android.content.Context
import android.content.res.Configuration
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import com.google.android.material.color.MaterialColors

@ColorInt
fun Context.getColor(
    @AttrRes colorAttributeResId: Int,
    @ColorRes defaultDayColorResId: Int,
    @ColorRes defaultNightColorResId: Int = defaultDayColorResId,
): Int {
    val defaultColor = if (resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_NO) {
        getColor(defaultDayColorResId)
    } else {
        getColor(defaultNightColorResId)
    }

    return MaterialColors.getColor(this, colorAttributeResId, defaultColor)
}