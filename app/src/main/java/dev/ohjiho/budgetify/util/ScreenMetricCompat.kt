package dev.ohjiho.budgetify.util

import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.util.DisplayMetrics
import android.util.Size
import android.view.WindowManager
import android.view.WindowMetrics
import androidx.annotation.RequiresApi

object ScreenMetricsCompat {
    private val api: Api =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) ApiLevel34()
        else Api()

    /**
     * Returns screen size in pixels.
     */
    fun getScreenSize(context: Context, isDp: Boolean): Size = api.getScreenSize(context, isDp)

    fun Number.toDp(density: Float) = (this.toFloat() / density).toInt()

    @Suppress("DEPRECATION")
    private open class Api {
        open fun getScreenSize(context: Context, isDp: Boolean): Size {
            val display = context.getSystemService(WindowManager::class.java).defaultDisplay
            val metrics = if (display != null) {
                DisplayMetrics().also { display.getRealMetrics(it) }
            } else {
                Resources.getSystem().displayMetrics
            }
            val width = if (isDp) metrics.widthPixels.toDp(metrics.density) else metrics.widthPixels
            val height = if (isDp) metrics.heightPixels.toDp(metrics.density) else metrics.heightPixels
            return Size(width, height)
        }
    }

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    private class ApiLevel34 : Api() {
        override fun getScreenSize(context: Context, isDp: Boolean): Size {
            val metrics: WindowMetrics = context.getSystemService(WindowManager::class.java).currentWindowMetrics
            val width = if (isDp) metrics.bounds.width().toDp(metrics.density) else metrics.bounds.width()
            val height = if (isDp) metrics.bounds.height().toDp(metrics.density) else metrics.bounds.height()
            return Size(width, height)
        }
    }
}