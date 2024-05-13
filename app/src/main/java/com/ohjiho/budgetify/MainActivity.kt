package com.ohjiho.budgetify

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dev.ohjiho.budgetify.setup.SetUpActivity
import dev.ohjiho.budgetify.utils.IS_SET_UP_KEY
import dev.ohjiho.budgetify.utils.SET_UP_SHARED_PREF

class MainActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkIfSetUp()
    }

    private fun checkIfSetUp() {
        val sharedPreferences = getSharedPreferences(SET_UP_SHARED_PREF, MODE_PRIVATE)
        val isSetUp = sharedPreferences.getBoolean(IS_SET_UP_KEY, false)
        if (!isSetUp) {
            startActivity(Intent(this, SetUpActivity::class.java))
            finish()
        }
    }
}