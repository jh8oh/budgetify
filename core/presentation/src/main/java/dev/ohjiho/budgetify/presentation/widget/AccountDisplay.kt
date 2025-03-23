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
import dev.ohjiho.budgetify.domain.model.Account
import dev.ohjiho.budgetify.presentation.databinding.WidgetAccountDisplayBinding

class AccountDisplay @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0) :
    ConstraintLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val binding: WidgetAccountDisplayBinding = WidgetAccountDisplayBinding.inflate(LayoutInflater.from(context), this, true)
    private val listPopUpWindow: ListPopupWindow by lazy {
        ListPopupWindow(context)
    }

    private var adapter = Adapter(context, android.R.layout.simple_dropdown_item_1line, emptyList())
        set(value) {
            field = value
            listPopUpWindow.setAdapter(value)
        }
    var accounts: List<Account> = emptyList()
        set(value) {
            field = value
            adapter = Adapter(context, android.R.layout.simple_dropdown_item_1line, value)
        }
    var selectedAccount: Account? = null
        set(value) {
            field = value
            updateView()
        }

    init {
        setUpDisplay()
        updateView()
    }

    private fun setUpDisplay() {
        listPopUpWindow.apply {
            isModal = true
            anchorView = binding.root
            inputMethodMode = ListPopupWindow.INPUT_METHOD_NOT_NEEDED
            setAdapter(this@AccountDisplay.adapter)
            height = ListPopupWindow.WRAP_CONTENT
            setOnItemClickListener { _, _, position, _ ->
                selectedAccount = if (position < 0) selectedItem as Account else adapter.getItem(position)
                dismiss()
            }
        }

        binding.clickArea.setOnClickListener {
            listPopUpWindow.show()

        }
    }

    private fun updateView() {
        with(binding) {
            name.text = selectedAccount?.name
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        listPopUpWindow.dismiss()
    }

    class Adapter(context: Context, private val resource: Int, objects: List<Account>) :
        ArrayAdapter<Account>(context, resource, 0, objects) {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val view = convertView ?: LayoutInflater.from(context).inflate(resource, parent, false)
            val item = getItem(position)
            (view as TextView).text = item?.name
            return view
        }
    }

    companion object {
        private const val MAX_ITEMS_MEASURED = 15
    }
}