package dev.ohjiho.component.recyclerview

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class SpaceItemDecoration(private val verticalSpace: Int = 0, private val horizontalSpace: Int = 0) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        parent.adapter?.itemCount?.let { itemCount ->
            if (parent.getChildAdapterPosition(view) < itemCount) {
                outRect.bottom = verticalSpace
                outRect.right = horizontalSpace
            }
        }
    }
}