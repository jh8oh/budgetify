package com.ohjiho.budgetify

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import dev.ohjiho.budgetify.data.sharedprefs.SetUpSharedPrefs
import dev.ohjiho.budgetify.setup.SetUpActivity
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity: AppCompatActivity() {

    @Inject
    internal lateinit var setUpSharedPrefs: SetUpSharedPrefs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkIfSetUp()
    }

    private fun checkIfSetUp() {
        val isSetUp = setUpSharedPrefs.isSetUp
        if (!isSetUp) {
            startActivity(Intent(this, SetUpActivity::class.java))
            finish()
        }
    }
}