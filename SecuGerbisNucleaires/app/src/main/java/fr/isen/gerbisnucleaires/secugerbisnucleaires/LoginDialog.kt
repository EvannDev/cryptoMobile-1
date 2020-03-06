package fr.isen.gerbisnucleaires.secugerbisnucleaires

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import java.lang.ClassCastException
import java.lang.IllegalStateException

class LoginDialog(clazz: Class<*>) : DialogFragment() {
    private lateinit var listener: LoginDialogListener
    private val dest = clazz

    interface LoginDialogListener {
        fun onDialogPositiveClick(dialog: DialogFragment, clazz: Class<*>)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        try {
            listener = context as LoginDialogListener
        } catch (e: ClassCastException) {
            throw ClassCastException(("$context must implements LoginDialogListener"))
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)

            builder.setView(layoutInflater.inflate(R.layout.activity_login_dialog, null))
                .setPositiveButton(R.string.login) { _, _ ->
                    listener.onDialogPositiveClick(this, dest)
                }
                .setNegativeButton(R.string.cancel) { _, _ ->
                    dialog?.cancel()
                }

            builder.create()
        } ?: throw  IllegalStateException("Activity cannot be null !")
    }
}