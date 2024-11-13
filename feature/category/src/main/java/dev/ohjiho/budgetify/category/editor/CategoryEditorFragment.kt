package dev.ohjiho.budgetify.category.editor

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
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
class CategoryEditorFragment : EditorFragment() {

    private val viewModel by viewModels<CategoryEditorViewModel>()
    private lateinit var binding: FragmentCategoryEditorBinding

    // Dialog
    private val iconAdapter: IconAdapter by lazy {
        IconAdapter(requireContext()) {
            viewModel.updateIconState(it)
            iconDialog.dismiss()
        }
    }

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            arguments?.getInt(CATEGORY_ID)?.let {
                viewModel.initWithId(it)
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
                        iconAdapter.setIsExpense(category.transactionType == TransactionType.EXPENSE)
                        if (category.transactionType == TransactionType.EXPENSE) {
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

    override fun onDelete() {
        viewModel.deleteCategory()
    }

    override fun saveState() {
        with(binding) {
            viewModel.updateState(categoryName.text.toString(), needOrWantToggleGroup.checkedButtonId == needButton.id)
        }
    }

    companion object {
        private const val DIALOG_HORIZONTAL_PADDING = 12
        private const val DIALOG_VERTICAL_PADDING = 16

        private const val CATEGORY_ID = "CATEGORY_ID"

        fun newInstance(categoryId: Int, fromSetUp: Boolean = false) = CategoryEditorFragment().apply {
            arguments = Bundle().apply {
                putInt(CATEGORY_ID, categoryId)
                putBoolean(FROM_SET_UP_ARG, fromSetUp)
            }
        }
    }
}