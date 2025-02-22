package dev.ohjiho.budgetify.category.editor

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import dev.ohjiho.budgetify.category.R
import dev.ohjiho.budgetify.category.databinding.FragmentCategoryEditorBinding
import dev.ohjiho.budgetify.domain.model.TransactionType
import dev.ohjiho.budgetify.theme.fragment.EditorFragment
import dev.ohjiho.budgetify.utils.ui.ScreenMetricsCompat
import dev.ohjiho.budgetify.utils.ui.ScreenMetricsCompat.toPx
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CategoryEditorFragment private constructor() : EditorFragment() {

    private val viewModel by viewModels<CategoryEditorViewModel>()
    private lateinit var binding: FragmentCategoryEditorBinding

    // Resources
    override val newTitle by lazy { resources.getString(R.string.fragment_category_editor_add_title) }
    override val updateTitle by lazy { resources.getString(R.string.fragment_category_editor_update_title) }
    private val categoryNameBlankError by lazy { resources.getString(R.string.fragment_category_editor_name_blank_error) }

    private val iconPressAnimation by lazy {
        AnimationUtils.loadAnimation(requireContext(), R.anim.anim_icon_on_press)
    }
    private val iconReleaseAnimation by lazy {
        AnimationUtils.loadAnimation(requireContext(), R.anim.anim_icon_on_release)
    }

    // Adapter
    private val iconAdapter: IconAdapter by lazy {
        IconAdapter(requireContext()) {
            viewModel.updateIconState(it)
            iconDialog.dismiss()
        }
    }

    // Dialog
    private val iconDialog: AlertDialog by lazy {
        val dialogView = FrameLayout(requireContext()).apply {
            layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            setPadding(
                DIALOG_HORIZONTAL_PADDING.toPx(ScreenMetricsCompat.getDensity(context)),
                DIALOG_VERTICAL_PADDING.toPx(ScreenMetricsCompat.getDensity(context)),
                DIALOG_HORIZONTAL_PADDING.toPx(ScreenMetricsCompat.getDensity(context)),
                DIALOG_VERTICAL_PADDING.toPx(ScreenMetricsCompat.getDensity(context))
            )
            addView(RecyclerView(requireContext()).apply {
                layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                layoutManager = GridLayoutManager(requireContext(), 6)
                adapter = iconAdapter
            })
        }

        AlertDialog.Builder(requireContext()).apply {
            setView(dialogView)
            setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }
        }.create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            val transactionType = arguments?.getString(TRANSACTION_TYPE_ARG)
            val categoryId = arguments?.getInt(CATEGORY_ID_ARG) ?: 0

            if (transactionType != null) {
                viewModel.initNew(TransactionType.valueOf(transactionType))
            } else if (categoryId != 0) {
                try {
                    viewModel.initExisting(categoryId)
                } catch (e: NullPointerException) {
                    Log.e(CATEGORY_EDITOR_TAG, e.message ?: "")
                    requireActivity().onBackPressedDispatcher.onBackPressed()
                }
            } else {
                Log.e(CATEGORY_EDITOR_TAG, CATEGORY_EDITOR_NO_ARGS_ERROR)
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
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
                        iconAdapter.setIsExpense(category.type == TransactionType.EXPENSE)
                        if (category.type == TransactionType.EXPENSE) {
                            needOrWantLabel.visibility = View.VISIBLE
                            needOrWantToggleGroup.visibility = View.VISIBLE
                            needOrWantToggleGroup.check(if (category.isNeed == true) needButton.id else wantButton.id)
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
            categoryIcon.setOnTouchListener { v, event ->
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> v.startAnimation(iconPressAnimation)
                    MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> v.startAnimation(iconReleaseAnimation)
                }

                false
            }
            categoryIcon.setOnClickListener {
                iconDialog.show()
            }
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

    override fun saveState() {
        with(binding) {
            viewModel.updateState(categoryName.text.toString(), needOrWantToggleGroup.checkedButtonId == needButton.id)
        }
    }

    override fun onDelete() {
        viewModel.deleteCategory()
    }

    companion object {
        private const val CATEGORY_EDITOR_TAG = "CategoryEditor"

        private const val TRANSACTION_TYPE_ARG = "TRANSACTION_TYPE"
        private const val CATEGORY_ID_ARG = "CATEGORY_ID"

        private const val CATEGORY_EDITOR_NO_ARGS_ERROR = "No arguments found for $TRANSACTION_TYPE_ARG or $CATEGORY_ID_ARG"

        private const val DIALOG_HORIZONTAL_PADDING = 12
        private const val DIALOG_VERTICAL_PADDING = 16

        fun getSetUpInstance(categoryId: Int?) = CategoryEditorFragment().apply {
            arguments = Bundle().apply {
                putBoolean(FROM_SET_UP_ARG, true)

                if (categoryId == null) {
                    putString(TRANSACTION_TYPE_ARG, TransactionType.EXPENSE.name)
                } else {
                    putInt(CATEGORY_ID_ARG, categoryId)
                }
            }
        }

        fun getNewInstance(type: TransactionType) = CategoryEditorFragment().apply {
            arguments = Bundle().apply {
                putString(TRANSACTION_TYPE_ARG, type.name)
            }
        }

        fun getUpdateInstance(categoryId: Int) = CategoryEditorFragment().apply {
            arguments = Bundle().apply {
                putInt(CATEGORY_ID_ARG, categoryId)
            }
        }
    }
}