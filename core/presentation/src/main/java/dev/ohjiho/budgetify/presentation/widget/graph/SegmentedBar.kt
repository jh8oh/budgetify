package dev.ohjiho.budgetify.presentation.widget.graph

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect
import android.graphics.RectF
import android.text.TextPaint
import android.util.AttributeSet
import dev.ohjiho.budgetify.presentation.R
import dev.ohjiho.budgetify.utils.data.sumOf
import dev.ohjiho.budgetify.utils.data.toCurrencyFormat
import dev.ohjiho.budgetify.utils.ui.getColor
import kotlin.math.min
import com.google.android.material.R as materialR
import dev.ohjiho.budgetify.theme.R as themeR

class SegmentedBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0,
) : SegmentedGraph(context, attrs, defStyleAttr, defStyleRes) {

    // Resources
    private var barHeight = resources.getDimension(R.dimen.widget_segmented_bar_height)
    private val cornerRadius = resources.getDimension(R.dimen.widget_segmented_bar_corner_radius)
    private val textTopMargin = resources.getDimension(R.dimen.widget_segmented_bar_text_top_margin)
    private val textEndMargin = resources.getDimension(R.dimen.widget_segmented_bar_text_end_margin)

    // Draw
    private val backgroundRectF = RectF()
    private val segmentRectF = RectF()
    private val segmentPaint = Paint()
    private val segmentPath = Path()
    private val textBounds = Rect()
    private val textPaint = TextPaint().apply {
        color = context.getColor(materialR.attr.colorOnBackground, themeR.color.black_800, themeR.color.black_100)
        textSize = resources.getDimension(themeR.dimen.theme_budgetify_label_text_size)
        typeface = resources.getFont(themeR.font.montserrat_regular)
    }

    private val startSegmentCorners = floatArrayOf(cornerRadius, cornerRadius, 0f, 0f, 0f, 0f, cornerRadius, cornerRadius)
    private val endSegmentCorners by lazy {
        floatArrayOf(0f, 0f, cornerRadius, cornerRadius, cornerRadius, cornerRadius, 0f, 0f)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // Get text bounds
        generateText().let {
            textPaint.getTextBounds(it, 0, it.length, textBounds)
        }
        barHeight = min(barHeight, MeasureSpec.getSize(heightMeasureSpec) - (textBounds.height() + textTopMargin))

        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), (barHeight + textBounds.height() + textTopMargin).toInt())
    }

    override fun onDraw(canvas: Canvas) {
        // Draw background
        backgroundRectF.set(BORDER_STROKE_WIDTH, BORDER_STROKE_WIDTH, width.toFloat() - BORDER_STROKE_WIDTH, barHeight - BORDER_STROKE_WIDTH)
        canvas.drawRoundRect(backgroundRectF, cornerRadius, cornerRadius, backgroundPaint)

        // Draw each segment
        var drawnSegmentWidth = 0f
        val cap = getCap()
        val segmentWidths = segments.map { (it.amount / cap) * width }
        segmentWidths.forEachIndexed { index, fl ->
            if (fl > 0) {
                segmentPaint.apply {
                    isAntiAlias = true
                    color = resources.getColor(segments[index].category.icon.colorRes, context.theme)
                    style = Paint.Style.FILL
                }

                if (index == 0) {
                    // Drawing first segment requires rounded start corners
                    segmentPath.apply {
                        reset()
                        addRoundRect(BORDER_STROKE_WIDTH, BORDER_STROKE_WIDTH, fl, barHeight - BORDER_STROKE_WIDTH, startSegmentCorners, Path.Direction.CW)
                    }
                    canvas.drawPath(segmentPath, segmentPaint)
                } else if (drawnSegmentWidth + fl >= width - BORDER_STROKE_WIDTH) {
                    // Drawing last segment requires rounded end corners when reaching the end
                    segmentPath.apply {
                        reset()
                        addRoundRect(drawnSegmentWidth - 1f, BORDER_STROKE_WIDTH, drawnSegmentWidth + fl - BORDER_STROKE_WIDTH, barHeight - BORDER_STROKE_WIDTH, endSegmentCorners, Path.Direction.CW)
                    }
                    canvas.drawPath(segmentPath, segmentPaint)
                } else {
                    segmentRectF.set(drawnSegmentWidth - 1f, BORDER_STROKE_WIDTH, drawnSegmentWidth + fl, barHeight - BORDER_STROKE_WIDTH)
                    canvas.drawRect(segmentRectF, segmentPaint)
                }

                drawnSegmentWidth += fl
            }
        }

        // Draw border
        canvas.drawRoundRect(backgroundRectF, cornerRadius, cornerRadius, borderPaint)

        // Draw text underneath
        canvas.drawText(
            generateText(),
            width - textBounds.width().toFloat() - textEndMargin,
            barHeight + textBounds.height().toFloat() + textTopMargin,
            textPaint
        )
    }

    private fun generateText() =
        "${segments.sumOf { it.amount }.toCurrencyFormat(currency, context)} / ${maxProgress.toCurrencyFormat(currency, context)}"
}