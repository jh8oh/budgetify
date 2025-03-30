package dev.ohjiho.budgetify.presentation.fragment

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import dev.ohjiho.budgetify.presentation.R
import dev.ohjiho.budgetify.utils.ui.getColor
import com.google.android.material.R as materialR
import dev.ohjiho.budgetify.theme.R as themeR

abstract class EditorFragment : Fragment() {

    // Resources
    abstract val newTitle: String
    abstract val updateTitle: String

    private val onAppBarSetUpColor = requireContext().getColor(materialR.attr.colorOnPrimary, themeR.color.white)
    private val onAppBarNonSetUpColor =
        requireContext().getColor(materialR.attr.colorOnBackground, themeR.color.black_800, themeR.color.black_100)

    protected fun setUpEditorAppBar(isNew: Boolean) {
        (activity as AppCompatActivity).apply {
            if (isNew) {
                title = newTitle
            } else {
                title = updateTitle
                addMenuProvider(object : MenuProvider {
                    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                        menuInflater.inflate(R.menu.menu_editor, menu)
                        menu.findItem(R.id.delete).icon = ContextCompat.getDrawable(requireContext(), themeR.drawable.ic_delete)?.apply {
                            setTint(if (arguments?.getBoolean(FROM_SET_UP_ARG) == true) onAppBarSetUpColor else onAppBarNonSetUpColor)
                        }
                    }

                    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                        return when (menuItem.itemId) {
                            R.id.delete -> {
                                onDelete()
                                requireActivity().onBackPressedDispatcher.onBackPressed()
                                true
                            }

                            else -> false
                        }
                    }
                }, viewLifecycleOwner, Lifecycle.State.RESUMED)
            }

            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        saveState()
    }

    abstract fun saveState()

    abstract fun onDelete()

    companion object {
        @JvmStatic
        protected val FROM_SET_UP_ARG = "FROM_SET_UP"
    }
}