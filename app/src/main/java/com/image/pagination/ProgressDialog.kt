package com.image.pagination

import android.app.Dialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

/*
        To use dialog-
        val dialog:ProgressDialog = ProgressDialog.init("Loading ..")
        dialog.show(supportFragmentManager, ProgressDialog.TAG);

        dialog.dismiss()
*/
class ProgressDialog : DialogFragment() {
    private lateinit var dialog: AlertDialog
    private var textView: TextView? = null
    private var message: String? = null

    companion object {
        var DIALOG_MESSAGE = "dialog message"
        val TAG = ProgressDialog::class.java.simpleName

        fun init(message: String): ProgressDialog {
            val dialog = ProgressDialog()
            val arguments = Bundle()
            arguments.putString(DIALOG_MESSAGE, message)
            dialog.arguments = arguments

            return dialog
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (arguments != null && requireArguments().containsKey(DIALOG_MESSAGE) && message == null) {
            message = requireArguments().getString(DIALOG_MESSAGE)!!
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder =
            AlertDialog.Builder(requireContext(), R.style.Theme_AppCompat_Light_Dialog_Alert)

        val inflater = requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_progress, null)
        textView = dialogView.findViewById(R.id.dialog_msg)
        textView!!.text = message

        builder.setCancelable(false)
        builder.setView(dialogView)
        dialog = builder.create()
        dialog.setCanceledOnTouchOutside(false)
        return dialog
    }

    fun changeDialogMessage(message: String) {
        if(textView != null)
            textView!!.text = message
        else
            this.message = message
    }
}

