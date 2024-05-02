package dev.ohjiho.budgetify.data.model

enum class CategoryType {
    INCOME,
    EXPENSE_NEED,
    EXPENSE_WANT
}

interface Category {
    var uid: Int
    var name: String
    var color: String
    var categoryType: CategoryType
}