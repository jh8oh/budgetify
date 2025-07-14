package dev.ohjiho.budgetify.utils.ui

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

    fun getDensity(context: Context): Float = api.getDensity(context)
    fun getScreenSize(context: Context, isDp: Boolean = false): Size = api.getScreenSize(context, isDp)

    fun Number.toPx(density: Float) = (this.toFloat() * density).toInt()
    fun Number.toDp(density: Float) = (this.toFloat() / density).toInt()

    @Suppress("DEPRECATION")
    private open class Api {
        open fun getDensity(context: Context): Float {
            val display = context.getSystemService(WindowManager::class.java).defaultDisplay
            val metrics = if (display != null) {
                DisplayMetrics().also { display.getMetrics(it) }
            } else {
                Resources.getSystem().displayMetrics
            }
            return metrics.density
        }

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
        override fun getDensity(context: Context): Float {
            return context.getSystemService(WindowManager::class.java).currentWindowMetrics.density
        }

        override fun getScreenSize(context: Context, isDp: Boolean): Size {
            val metrics: WindowMetrics = context.getSystemService(WindowManager::class.java).currentWindowMetrics
            val widthPx = metrics.bounds.width()
            val heightPx = metrics.bounds.height()

            val width = if (isDp) widthPx.toDp(metrics.density) else widthPx
            val height = if (isDp) heightPx.toDp(metrics.density) else heightPx
            return Size(width, height)
        }
    }
}