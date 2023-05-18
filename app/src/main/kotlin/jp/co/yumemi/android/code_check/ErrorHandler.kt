package jp.co.yumemi.android.code_check

import org.json.JSONException
import java.io.IOException

class ErrorHandler {
    companion object{
        fun handleIOException(e: IOException){}

        fun handleJSONException(e: JSONException){}

        fun handleException(e: java.lang.Exception){}
    }
}