
package com.embeltech.meterreading.utils

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.view.View
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.embeltech.meterreading.R

/**
 * Singleton class responsible for displaying Dialogs and Snack bars across the app
 */

object DialogUtils {
    private var isShowing: Boolean = false

    fun showYesNoAlert(context: Context?, title: String, message: String, yes : Int, no : Int, listner: OnDialogYesNoActionListener?) {
        if (isShowing) return
        showYesNoAlert(context, yes, no, title, message, listner)

    }

    fun showYesNoAlert(context: Context?, okButtonText: Int? = null, cancelButtonText: Int? = null, title: String, message: String, listner: OnDialogYesNoActionListener?) {
        if (isShowing) return
        var alertDialog: Dialog? = null

        if (context != null && !(context as Activity).isFinishing) {
            val builder = AlertDialog.Builder(context)

            //builder.setTitle(Html.fromHtml("<font color='#000000'>" + title + "</font>")).setMessage(message);
            builder.setTitle(title).setMessage(message)

            val accept: Int = okButtonText ?: android.R.string.ok
            val decline: Int = cancelButtonText ?: android.R.string.cancel


            // Add the buttons
            builder.setPositiveButton(accept) { dialog, _ ->
                isShowing = false
                dialog.dismiss()
                listner?.onYesClick()
            }
            builder.setNegativeButton(decline) { dialog, _ ->
                isShowing = false
                dialog.dismiss()
                listner?.onNoClick()
            }

            // Create the AlertDialog
            alertDialog = builder.create()
            alertDialog!!.setCancelable(false)
            alertDialog.setOnDismissListener {
                isShowing = false
            }
            alertDialog.show()
            isShowing = true
        }

    }

    /***********************************************************************
     * @ Purpose : This methods are used to show Alert with Ok button
     */
    fun showOkAlert(context: Context, title: String, message: String, func: (() -> Unit)? = null) {
        if (isShowing) return
        val alertDialog: Dialog?

        val builder = AlertDialog.Builder(context)

        builder.setTitle(title).setMessage(message)

        builder.setPositiveButton(android.R.string.ok) { dialog, _ ->
            dialog.dismiss()
            isShowing = false
            func?.invoke()
        }
        alertDialog = builder.create()
        alertDialog!!.setCancelable(false)
        alertDialog.setOnDismissListener {
            isShowing = false
        }
        isShowing = true
        alertDialog.show()
    }

    fun showSnackBarMsg(context: Context, parentView: View?, string: String?, duration: Int) {
        val snackbar = Snackbar.make(parentView!!, string!!, duration)
        customizeSnackbar(context, snackbar)

        snackbar.show()
    }

    fun showSnackBarMsg(context: Context, parentView: View, string: String, duration: Int, actionMsg: String?, actionClickListener: (Any) -> Unit) {
        val snackbar = Snackbar.make(parentView, string, duration)
        snackbar.setAction(actionMsg, actionClickListener)
        customizeSnackbar(context, snackbar)
        snackbar.show()
    }

    private fun customizeSnackbar(context: Context, snackbar: Snackbar) {
        snackbar.setActionTextColor(Color.WHITE)
        val sbView = snackbar.view
        sbView.setBackgroundColor(ContextCompat.getColor(context, R.color.purple_500))
    }

    interface OnDialogYesNoActionListener {
        fun onYesClick()

        fun onNoClick()
    }
}
