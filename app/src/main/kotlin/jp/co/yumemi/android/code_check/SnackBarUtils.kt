package jp.co.yumemi.android.code_check

import android.view.View
import com.google.android.material.snackbar.Snackbar

object SnackBarUtils {
    fun showSnackBar(view: View, message: String) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()
    }
}