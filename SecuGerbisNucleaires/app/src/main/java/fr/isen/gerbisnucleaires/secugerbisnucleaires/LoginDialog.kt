package fr.isen.gerbisnucleaires.secugerbisnucleaires

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment

class LoginDialog(clazz: Class<*>) : DialogFragment() {
    private val dest = clazz
    private lateinit var listener: LoginDialogListener

    interface LoginDialogListener {
        fun onDialogPositiveClick(dialog: DialogFragment, view: View, clazz: Class<*>)
        fun onDialogNegativeClick(dialog: Dialog?)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        try {
            listener = context as LoginDialogListener
        } catch (e: ClassCastException) {
            throw ClassCastException(" must implement LoginDialogListener")
        }
    }

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val view = requireActivity().layoutInflater.inflate(R.layout.activity_login_dialog, null)

            builder.setTitle("Please confirm your identity")
                .setView(view)
                .setPositiveButton(R.string.login) { _, _ ->
                    listener.onDialogPositiveClick(this, view, dest)
                }
                .setNegativeButton(R.string.cancel) { _, _ ->
                    listener.onDialogNegativeClick(dialog)
                }

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}