package dev.ohjiho.budgetify.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.widget.ListPopupWindow
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.withStyledAttributes
import dev.ohjiho.budgetify.presentation.R
import dev.ohjiho.budgetify.presentation.databinding.WidgetDropdownMenuBinding

class DropdownMenu @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0) :
    ConstraintLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val binding: WidgetDropdownMenuBinding = WidgetDropdownMenuBinding.inflate(LayoutInflater.from(context), this, true)
    private val listPopUpWindow: ListPopupWindow by lazy {
        ListPopupWindow(context)
    }

    private val adapter = Adapter(context, android.R.layout.simple_dropdown_item_1line, emptyList())
    var items: List<String> = emptyList()
        set(value) {
            field = value
            adapter.apply {
                clear()
                addAll(value)
                notifyDataSetChanged()
            }
        }
    var selectedItemIndex: Int = 0
        set(value) {
            field = value
            updateSelectedName()
        }

    init {
        setUpDisplay(attrs)
        updateSelectedName()
    }

    private fun setUpDisplay(attrs: AttributeSet?) {
        context.withStyledAttributes(attrs, R.styleable.DropdownMenu) {
            setTitle(getString(R.styleable.DropdownMenu_title))
        }

        listPopUpWindow.apply {
            isModal = true
            anchorView = binding.root
            inputMethodMode = ListPopupWindow.INPUT_METHOD_NOT_NEEDED
            setAdapter(this@DropdownMenu.adapter)
            height = ListPopupWindow.WRAP_CONTENT
            setOnItemClickListener { _, _, position, _ ->
                selectedItemIndex = position
                dismiss()
            }
        }

        binding.clickArea.setOnClickListener {
            listPopUpWindow.show()

        }
    }

    private fun updateSelectedName() {
        if (items.isNotEmpty()) {
            binding.selectedItem.text = adapter.getItem(selectedItemIndex)
        }
    }

    fun setTitle(title: String?) {
        binding.title.text = title
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        listPopUpWindow.dismiss()
    }

    class Adapter(context: Context, private val resource: Int, objects: List<String>) :
        ArrayAdapter<String>(context, resource, 0, objects.toMutableList()) {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val view = convertView ?: LayoutInflater.from(context).inflate(resource, parent, false)
            (view as TextView).text = getItem(position)
            return view
        }
    }
}