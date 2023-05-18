package jp.co.yumemi.android.code_check

import android.util.Log
import org.json.JSONException
import java.io.IOException

object ErrorHandler {
    fun handleIOException(e: IOException) {
        Log.e("Error", "IOException occurred: ${e.message}")
        // その他の処理...
    }

    fun handleJSONException(e: JSONException) {
        Log.e("Error", "JSONException occurred: ${e.message}")
        // その他の処理...
    }

    fun handleException(e: Exception) {
        Log.e("Error", "Exception occurred: ${e.message}")
        // その他の処理...
    }
}