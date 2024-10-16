package dev.ohjiho.budgetify.category.editor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import dev.ohjiho.budgetify.category.R
import dev.ohjiho.budgetify.category.databinding.FragmentCategoryEditorBinding
import dev.ohjiho.budgetify.domain.model.ExpenseCategory
import dev.ohjiho.budgetify.theme.fragment.EditorFragment
import dev.ohjiho.budgetify.theme.icon.Icon
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CategoryEditorFragment : EditorFragment() {

    private val viewModel by viewModels<CategoryEditorViewModel>()
    private lateinit var binding: FragmentCategoryEditorBinding

    private var icon: Icon = Icon.HOME

    // Resources
    override val newTitle by lazy { resources.getString(R.string.fragment_category_editor_add_title) }
    override val updateTitle by lazy { resources.getString(R.string.fragment_category_editor_update_title) }
    private val categoryNameBlankError by lazy { resources.getString(R.string.fragment_category_editor_name_blank_error) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            arguments?.let {
                val isExpense = it.getBoolean(IS_EXPENSE)
                val categoryId = it.getInt(CATEGORY_ID)
                viewModel.initWithId(isExpense, categoryId)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentCategoryEditorBinding.inflate(inflater)

        setUpEditorAppBar(viewModel.isNew)

        with(binding) {
            viewLifecycleOwner.lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.category.collect { category ->
                        categoryName.setText(category.name)
                        categoryIcon.setImageResource(category.icon.drawableRes)
                        categoryIcon.setBackgroundColor(ContextCompat.getColor(requireContext(), category.icon.colorRes))
                        if (category is ExpenseCategory) {
                            needOrWantLabel.visibility = View.VISIBLE
                            needOrWantToggleGroup.visibility = View.VISIBLE
                            needOrWantToggleGroup.check(if (category.isNeed) needButton.id else wantButton.id)
                        } else {
                            needOrWantLabel.visibility = View.GONE
                            needOrWantToggleGroup.visibility = View.GONE
                        }
                    }
                }
            }

            // Name
            categoryName.doAfterTextChanged {
                // Remove error once any text has been inputted
                categoryName.error = null
            }
            // Icon
            // TODO open dialog with icons
            // Save button
            saveButton.setOnClickListener {
                if (categoryName.text.isNullOrBlank()) {
                    categoryName.setText("")    // Clears text in case it only contains whitespace
                    categoryName.error = categoryNameBlankError
                } else {
                    saveState()
                    viewModel.saveCategory()
                    requireActivity().onBackPressedDispatcher.onBackPressed()
                }
            }
        }

        return binding.root
    }

    override fun onDelete() {
        viewModel.deleteCategory()
    }

    override fun saveState() {
        with(binding) {
            viewModel.updateState(categoryName.text.toString(), icon, needOrWantToggleGroup.checkedButtonId == needButton.id)
        }
    }

    companion object {
        private const val IS_EXPENSE = "IS_EXPENSE"
        private const val CATEGORY_ID = "CATEGORY_ID"

        fun newInstance(isExpense: Boolean, categoryId: Int, fromSetUp: Boolean = false) = CategoryEditorFragment().apply {
            arguments = Bundle().apply {
                putBoolean(IS_EXPENSE, isExpense)
                putInt(CATEGORY_ID, categoryId)
                putBoolean(FROM_SET_UP_ARG, fromSetUp)
            }
        }
    }
}