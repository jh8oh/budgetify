/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

// Original file from com.google.android.material.textfield.CutoutDrawable

package dev.ohjiho.budgetify.presentation.widget

import android.annotation.SuppressLint
import android.graphics.Canvas
import android.graphics.RectF
import android.graphics.drawable.Drawable
import androidx.core.graphics.withSave
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel

@SuppressLint("RestrictedApi")
class CutoutDrawable private constructor(private var drawableState: CutoutDrawableState) : MaterialShapeDrawable(drawableState) {

    override fun mutate(): Drawable {
        drawableState = CutoutDrawableState(drawableState)
        return this
    }

    override fun drawStrokeShape(canvas: Canvas) {
        if (drawableState.cutoutBounds.isEmpty) {
            super.drawStrokeShape(canvas)
        } else {
            // Saves the canvas so we can restore the clip after drawing the stroke.
            canvas.withSave {
                clipOutRect(drawableState.cutoutBounds)
                super.drawStrokeShape(this)
            }
        }
    }

    fun setCutout(left: Float, top: Float, right: Float, bottom: Float) {
        // Avoid expensive redraws by only calling invalidateSelf if one of the cutout's dimensions has
        // changed.
        if (left != drawableState.cutoutBounds.left || top != drawableState.cutoutBounds.top || right != drawableState.cutoutBounds.right || bottom != drawableState.cutoutBounds.bottom) {
            drawableState.cutoutBounds.set(left, top, right, bottom)
            invalidateSelf()
        }
    }

    companion object {
        fun create(shapeAppearanceModel: ShapeAppearanceModel): CutoutDrawable {
            return CutoutDrawable(CutoutDrawableState(shapeAppearanceModel, RectF()))
        }

        private class CutoutDrawableState : MaterialShapeDrawableState {
            var cutoutBounds: RectF

            constructor(shapeAppearanceModel: ShapeAppearanceModel, cutoutBounds: RectF) : super(shapeAppearanceModel, null) {
                this.cutoutBounds = cutoutBounds
            }

            constructor(state: CutoutDrawableState) : super(state) {
                this.cutoutBounds = state.cutoutBounds
            }

            override fun newDrawable(): Drawable {
                return CutoutDrawable(this).apply {
                    invalidateSelf()
                }
            }
        }
    }
}