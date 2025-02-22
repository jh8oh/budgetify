package com.ohjiho.budgetify.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewTreeObserver
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.ohjiho.budgetify.R
import dev.ohjiho.budgetify.databinding.ActivityMainBinding
import dev.ohjiho.budgetify.databinding.DialogAddTransactionBinding
import dev.ohjiho.budgetify.domain.model.TransactionType
import dev.ohjiho.budgetify.setup.SetUpActivity
import dev.ohjiho.budgetify.transaction.TransactionEditorFragment

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    // Dialog
    private val addTransactionDialog: AlertDialog by lazy {
        val binding = DialogAddTransactionBinding.inflate(LayoutInflater.from(this)).apply {
            newExpense.setOnClickListener {
                findNavController(R.id.fragment_container).navigate(
                    R.id.nav_transaction_editor_fragment,
                    TransactionEditorFragment.bundle(TransactionType.EXPENSE)
                )
                addTransactionDialog.dismiss()
            }

            newIncome.setOnClickListener {
                findNavController(R.id.fragment_container).navigate(
                    R.id.nav_transaction_editor_fragment,
                    TransactionEditorFragment.bundle(TransactionType.INCOME)
                )
                addTransactionDialog.dismiss()
            }

            newTransfer.setOnClickListener {
                findNavController(R.id.fragment_container).navigate(
                    R.id.nav_transaction_editor_fragment,
                    TransactionEditorFragment.bundle(TransactionType.TRANSFER)
                )
                addTransactionDialog.dismiss()
            }
        }

        AlertDialog.Builder(this).apply {
            setView(binding.root)
        }.create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            root.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    checkIfSetUp()
                    return if (viewModel.isReady) {
                        root.viewTreeObserver.removeOnPreDrawListener(this)
                        true
                    } else {
                        false
                    }
                }
            })

            fab.setOnClickListener {
                addTransactionDialog.show()
            }
            bottomNav.setupWithNavController((supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment).navController)
        }
    }

    private fun checkIfSetUp() {
        val isSetUp = true
        if (!isSetUp) {
            startActivity(Intent(this, SetUpActivity::class.java))
            finish()
        }
    }
}