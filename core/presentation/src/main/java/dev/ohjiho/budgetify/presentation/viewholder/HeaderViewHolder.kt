package dev.ohjiho.budgetify.presentation.viewholder

import androidx.recyclerview.widget.RecyclerView
import dev.ohjiho.budgetify.presentation.databinding.ItemHeaderBinding

class HeaderViewHolder(private val binding: ItemHeaderBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(headerText: String){
        binding.headerTitle.text = headerText
    }
}