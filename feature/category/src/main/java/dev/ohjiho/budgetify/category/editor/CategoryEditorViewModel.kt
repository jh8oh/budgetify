package dev.ohjiho.budgetify.category.editor

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.ohjiho.budgetify.domain.model.Category
import dev.ohjiho.budgetify.domain.model.TransactionType
import dev.ohjiho.budgetify.domain.repository.CategoryRepository
import dev.ohjiho.budgetify.domain.enums.Icon
import dev.ohjiho.budgetify.utils.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.properties.Delegates

@HiltViewModel
internal class CategoryEditorViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val categoryRepository: CategoryRepository,
) : ViewModel() {

    var isNew by Delegates.notNull<Boolean>()
    val category = combine(
        savedStateHandle.getStateFlow(UID_SAVED_STATE_KEY, 0),
        savedStateHandle.getStateFlow(NAME_SAVED_STATE_KEY, ""),
        savedStateHandle.getStateFlow(TRANSACTION_TYPE_SAVED_STATE_KEY, TransactionType.EXPENSE),
        savedStateHandle.getStateFlow(ICON_SAVED_STATE_KEY, Icon.HOME),
        savedStateHandle.getStateFlow<Boolean?>(IS_NEED_SAVED_STATE_KEY, null),
        viewModelScope
    ) { uid, name, transactionType, icon, isNeed ->
        Category(name, transactionType, icon, isNeed).apply { this.uid = uid }
    }

    fun initNew(type: TransactionType) {
        isNew = true
        savedStateHandle[TRANSACTION_TYPE_SAVED_STATE_KEY] = type
        savedStateHandle[IS_NEED_SAVED_STATE_KEY] = if (type == TransactionType.EXPENSE) true else null
    }

    fun initExisting(id: Int) {
        isNew = false
        viewModelScope.launch {
            val c = categoryRepository.getCategory(id) ?: throw NullPointerException(NO_CATEGORY_FOUND_ERROR)
            savedStateHandle[UID_SAVED_STATE_KEY] = c.uid
            savedStateHandle[NAME_SAVED_STATE_KEY] = c.name
            savedStateHandle[TRANSACTION_TYPE_SAVED_STATE_KEY] = c.type
            savedStateHandle[ICON_SAVED_STATE_KEY] = c.icon
            savedStateHandle[IS_NEED_SAVED_STATE_KEY] = c.isNeed
        }
    }

    fun updateIconState(icon: Icon) {
        savedStateHandle[ICON_SAVED_STATE_KEY] = icon
    }

    fun updateState(name: String, isNeed: Boolean) {
        savedStateHandle[UID_SAVED_STATE_KEY] = category.value.uid
        savedStateHandle[NAME_SAVED_STATE_KEY] = name
        savedStateHandle[IS_NEED_SAVED_STATE_KEY] = if (category.value.type == TransactionType.EXPENSE) isNeed else null
    }

    fun saveCategory() {
        viewModelScope.launch {
            if (isNew) {
                categoryRepository.insert(category.value)
            } else {
                categoryRepository.update(category.value)
            }
        }
    }

    fun deleteCategory() {
        viewModelScope.launch {
            categoryRepository.delete(category.value)
        }
    }

    companion object {
        private const val UID_SAVED_STATE_KEY = "UIDSavedState"
        private const val NAME_SAVED_STATE_KEY = "NameSavedState"
        private const val TRANSACTION_TYPE_SAVED_STATE_KEY = "TransactionTypeSavedStateKey"
        private const val ICON_SAVED_STATE_KEY = "IconSavedState"
        private const val IS_NEED_SAVED_STATE_KEY = "IsNeedSavedState"

        private const val NO_CATEGORY_FOUND_ERROR = "No category found with id: "
    }
}