package dev.ohjiho.budgetify.data.room.util

import android.content.Context
import android.util.Log
import androidx.sqlite.db.SupportSQLiteDatabase
import java.io.BufferedReader
import java.io.InputStreamReader

fun SupportSQLiteDatabase.insertFromAssets(context: Context, assetsFilePath: String) {
    val reader = BufferedReader(InputStreamReader(context.assets.open(assetsFilePath)))

    while (reader.ready()) {
        reader.readLine().let {
            if (it.isNotBlank() && !it.startsWith("--")) {
                execSQL(it)
                Log.d("Executed line", it)
            }
        }
    }

    reader.close()
}