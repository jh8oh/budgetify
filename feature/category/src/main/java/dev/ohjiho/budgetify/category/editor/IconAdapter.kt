package dev.ohjiho.budgetify.category.editor

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import dev.ohjiho.budgetify.category.R
import dev.ohjiho.budgetify.category.databinding.ItemIconBinding
import dev.ohjiho.budgetify.icons.Icon

class IconAdapter(private val context: Context, private val onClick: (Icon) -> Unit) :
    RecyclerView.Adapter<IconAdapter.ViewHolder>() {
    private var icons = emptyList<Icon>()

    // Animation
    private val iconPressAnimation by lazy {
        AnimationUtils.loadAnimation(context, R.anim.anim_icon_on_press)
    }
    private val iconReleaseAnimation by lazy {
        AnimationUtils.loadAnimation(context, R.anim.anim_icon_on_release)
    }

    inner class ViewHolder(private val binding: ItemIconBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(icon: Icon) {
            binding.icon.apply {
                setImageResource(icon.drawableRes)
                setBackgroundColor(ContextCompat.getColor(itemView.context, icon.colorRes))
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemIconBinding.inflate(LayoutInflater.from(parent.context), parent, false)).apply {
            itemView.setOnTouchListener { v, event ->
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> v.startAnimation(iconPressAnimation)
                    MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> v.startAnimation(iconReleaseAnimation)
                }

                false
            }
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