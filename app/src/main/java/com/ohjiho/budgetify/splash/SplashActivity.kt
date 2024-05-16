package com.ohjiho.budgetify.splash

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import dev.ohjiho.budgetify.setup.SetUpActivity

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private val viewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkIfSetUp()
    }

    private fun checkIfSetUp() {
        val isSetUp = viewModel.isSetUp
        if (!isSetUp) {
            startActivity(Intent(this, SetUpActivity::class.java))
            finish()
        }
    }
}