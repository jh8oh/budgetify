package dev.ohjiho.budgetify.theme.viewholder

import androidx.recyclerview.widget.RecyclerView
import dev.ohjiho.budgetify.theme.databinding.ItemHeaderBinding

class HeaderViewHolder(private val binding: ItemHeaderBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(headerText: String){
        binding.headerTitle.text = headerText
    }
}