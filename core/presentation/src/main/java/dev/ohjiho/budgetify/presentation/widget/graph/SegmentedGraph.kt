package dev.ohjiho.budgetify.presentation.widget.graph

import android.content.Context
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import dev.ohjiho.budgetify.domain.model.Category
import dev.ohjiho.budgetify.utils.data.getLocale
import dev.ohjiho.budgetify.utils.data.sumOf
import dev.ohjiho.budgetify.utils.ui.getColor
import java.math.BigDecimal
import java.util.Currency
import com.google.android.material.R as materialR
import dev.ohjiho.budgetify.theme.R as themeR

abstract class SegmentedGraph @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0,
) : View(context, attrs, defStyleAttr, defStyleRes) {

    var maxProgress: Float = DEFAULT_MAX_PROGRESS
    var currency: Currency = Currency.getInstance(getLocale(context))
        set(value) {
            field = value
            invalidate()
        }
    internal var segments: MutableList<Segment> = mutableListOf()

    // Resources
    internal val borderPaint by lazy {
        Paint().apply {
            isAntiAlias = true
            color = context.getColor(materialR.attr.colorOnBackground, themeR.color.black_800, themeR.color.black_100)
            style = Paint.Style.STROKE
        }
    }
    internal val backgroundPaint by lazy {
        Paint().apply {
            isAntiAlias = true
            color = context.getColor(materialR.attr.colorSurface, themeR.color.black_100, themeR.color.black_800)
            style = Paint.Style.FILL
        }
    }

    internal fun getCap() = segments.sumOf { it.amount }.let {
        if (it > maxProgress) it else maxProgress
    }

    fun setSegmentCategories(categories: List<Category>) {
        segments = categories.map { Segment(it, 0f) }.toMutableList()
        sortSegmentList()
        invalidate()
    }

    fun setSegmentAmount(categoryId: Int, amount: BigDecimal) {
        setSegmentAmount(categoryId, amount.toFloat())
    }

    fun setSegmentAmount(categoryId: Int, amount: Float) {
        for (i in 0 until segments.size) {
            if (segments[i].category.uid == categoryId) {
                segments[i] = segments[i].copy(amount = amount)
                return
            }
        }
    }

    private fun sortSegmentList() {
        segments.sortWith(compareBy({ it.category.isNeed == false }, { it.category.name }))
    }

    companion object {
        const val DEFAULT_MAX_PROGRESS = 1f
    }
}