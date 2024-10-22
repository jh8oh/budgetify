package dev.ohjiho.budgetify.category.editor

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import dev.ohjiho.budgetify.category.databinding.ItemIconBinding
import dev.ohjiho.budgetify.theme.icon.Icon

class IconAdapter(private val onClick: (Icon) -> Unit) : RecyclerView.Adapter<IconAdapter.ViewHolder>() {
    private var icons = emptyList<Icon>()

    inner class ViewHolder(private val binding: ItemIconBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(icon: Icon) {
            binding.icon.apply {
                setImageResource(icon.drawableRes)
                setBackgroundColor(ContextCompat.getColor(itemView.context, icon.colorRes))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemIconBinding.inflate(LayoutInflater.from(parent.context), parent, false)).apply {
            itemView.setOnClickListener { onClick(icons[adapterPosition]) }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(icons[position])
    }

    override fun getItemCount() = icons.size

    @SuppressLint("NotifyDataSetChanged")
    fun setIsExpense(isExpense: Boolean) {
        icons = if (isExpense) Icon.getExpenseIcons() else Icon.getIncomeIcons()

        // TODO Create diff util so that we don't have to use notifyDataSetChanged()
        notifyDataSetChanged()
    }
}