package dev.ohjiho.budgetify.category.recyclerview

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.ohjiho.budgetify.domain.model.Category

class CategoryRecyclerView @JvmOverloads constructor(
    context: Context,
    attr: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : RecyclerView(context, attr, defStyleAttr) {

    private var listener: Listener? = if (context is Listener) context else null

    interface Listener {
        fun onClick(category: Category)
    }

    init {
        adapter = CategoryAdapter { listener?.onClick(it) }

        layoutManager = LinearLayoutManager(context)
    }

    fun setListener(listener: Listener) {
        this.listener = listener
    }

    fun setCategories(isExpense: Boolean, categoryList: List<Category>) {
        (adapter as CategoryAdapter).setCategoryList(isExpense, categoryList)
    }
}