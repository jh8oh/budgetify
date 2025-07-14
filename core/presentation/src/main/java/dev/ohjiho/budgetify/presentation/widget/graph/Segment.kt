package dev.ohjiho.budgetify.presentation.widget.graph

import dev.ohjiho.budgetify.domain.model.Category

internal data class Segment(
    val category: Category,
    val amount: Float,
)