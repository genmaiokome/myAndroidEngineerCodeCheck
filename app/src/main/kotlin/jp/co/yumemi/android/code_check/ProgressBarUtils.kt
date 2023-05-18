import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Handler
import android.os.Looper
import android.widget.ProgressBar
import android.widget.RelativeLayout

object ProgressBarUtils {
    private var dialog: Dialog? = null
    private val mainHandler = Handler(Looper.getMainLooper())

    fun showProgressBar(context: Context) {
        if (dialog?.isShowing == true) {
            return
        }

        mainHandler.post {
            val progressBar = createProgressBar(context)
            val layoutParams = createLayoutParams()
            dialog = createDialog(context, progressBar, layoutParams)
            dialog?.show()
        }
    }

    fun hideProgressBar() {
        mainHandler.post {
            if (dialog?.isShowing == true) {
                dialog?.dismiss()
            }
            dialog = null
        }
    }

    private fun createProgressBar(context: Context): ProgressBar {
        val progressBar = ProgressBar(context)
        progressBar.isIndeterminate = true
        return progressBar
    }

    private fun createLayoutParams(): RelativeLayout.LayoutParams {
        return RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            addRule(RelativeLayout.CENTER_IN_PARENT)
        }
    }

    private fun createDialog(
        context: Context,
        progressBar: ProgressBar,
        layoutParams: RelativeLayout.LayoutParams
    ): Dialog {
        val dialog = Dialog(context)
        dialog.setCancelable(false)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(progressBar, layoutParams)

        return dialog
    }
}