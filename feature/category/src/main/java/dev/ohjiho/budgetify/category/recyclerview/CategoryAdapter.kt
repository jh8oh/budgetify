package dev.ohjiho.budgetify.category.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import dev.ohjiho.budgetify.category.databinding.ItemCategoryBinding
import dev.ohjiho.budgetify.domain.model.Category
import dev.ohjiho.budgetify.theme.databinding.ItemHeaderBinding
import dev.ohjiho.budgetify.theme.viewholder.HeaderViewHolder

internal class CategoryAdapter(private val onClick: ((Category) -> Unit)) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var categories = emptyList<Any>()

    inner class CategoryViewHolder(private val binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(category: Category) {
            with(binding) {
                categoryIcon.setImageResource(category.icon.drawableRes)
                categoryIcon.setBackgroundColor(ContextCompat.getColor(itemView.context, category.icon.colorRes))
                categoryName.text = category.name
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (categories[position]) {
            is String -> HEADER_VIEW_TYPE
            else -> CATEGORY_VIEW_TYPE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            HEADER_VIEW_TYPE -> HeaderViewHolder(ItemHeaderBinding.inflate(layoutInflater, parent, false))
            else -> CategoryViewHolder(ItemCategoryBinding.inflate(layoutInflater, parent, false)).apply {
                itemView.setOnClickListener {
                    onClick(categories[adapterPosition] as Category)
                }
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            HEADER_VIEW_TYPE -> (holder as HeaderViewHolder).bind(categories[position] as String)
            else -> (holder as CategoryViewHolder).bind(categories[position] as Category)
        }
    }

    override fun getItemCount() = categories.size

    fun setCategoryList(isExpense: Boolean, categoryList: List<Category>) {
        if (isExpense) {
            categoryList.groupBy { it.isNeed }.let { map ->
                val needCategories = map[true]?.let { listOf(NEED_HEADER) + it } ?: emptyList()
                val wantCategories = map[false]?.let { listOf(WANT_HEADER) + it } ?: emptyList()
                categories = needCategories + wantCategories
            }
        } else {
            categories = categoryList
        }
    }

    companion object {
        private const val HEADER_VIEW_TYPE = 0
        private const val CATEGORY_VIEW_TYPE = 1

        private const val NEED_HEADER = "Needs"
        private const val WANT_HEADER = "Wants"
    }
}