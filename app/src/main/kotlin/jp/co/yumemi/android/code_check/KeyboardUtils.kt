package jp.co.yumemi.android.code_check

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

object KeyboardUtils {
    fun hideKeyboard(focusView: View) {
        val inputMethodManager: InputMethodManager =
            focusView.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(
            focusView.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )
    }
}