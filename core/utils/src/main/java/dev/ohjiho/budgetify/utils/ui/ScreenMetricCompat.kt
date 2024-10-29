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
        open var widthPx: Int = 0
        open var heightPx: Int = 0
        open var density: Float = 0f

        open fun getDensity(context: Context): Float {
            return if (density != 0f) {
                density
            } else {
                val display = context.getSystemService(WindowManager::class.java).defaultDisplay
                val metrics = if (display != null) {
                    DisplayMetrics().also { display.getRealMetrics(it) }
                } else {
                    Resources.getSystem().displayMetrics
                }
                metrics.density
                    .also {
                        density = it
                    }
            }
        }

        open fun getScreenSize(context: Context, isDp: Boolean): Size {
            if (widthPx == 0 || heightPx == 0 || density == 0f) {
                val display = context.getSystemService(WindowManager::class.java).defaultDisplay
                val metrics = if (display != null) {
                    DisplayMetrics().also { display.getRealMetrics(it) }
                } else {
                    Resources.getSystem().displayMetrics
                }

                widthPx = metrics.widthPixels
                heightPx = metrics.heightPixels
                density = metrics.density
            }

            val width = if (isDp) widthPx.toDp(density) else widthPx
            val height = if (isDp) heightPx.toDp(density) else heightPx
            return Size(width, height)
        }
    }

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    private class ApiLevel34 : Api() {
        override fun getDensity(context: Context): Float {
            return if (density != 0f) {
                density
            } else {
                context.getSystemService(WindowManager::class.java).currentWindowMetrics.density.also {
                    density = it
                }
            }
        }

        override fun getScreenSize(context: Context, isDp: Boolean): Size {
            if (widthPx == 0 || heightPx == 0 || density == 0f) {
                val metrics: WindowMetrics = context.getSystemService(WindowManager::class.java).currentWindowMetrics

                widthPx = metrics.bounds.width()
                heightPx = metrics.bounds.height()
                density = metrics.density
            }

            val width = if (isDp) widthPx.toDp(density) else widthPx
            val height = if (isDp) heightPx.toDp(density) else heightPx
            return Size(width, height)
        }
    }
}