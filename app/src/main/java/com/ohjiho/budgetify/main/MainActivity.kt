package com.ohjiho.budgetify.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.ohjiho.budgetify.R
import dev.ohjiho.budgetify.databinding.ActivityMainBinding
import dev.ohjiho.budgetify.setup.SetUpActivity
import dev.ohjiho.budgetify.setup.databinding.ActivitySetUpBinding

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding){
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