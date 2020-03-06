package fr.isen.gerbisnucleaires.secugerbisnucleaires

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_login_dialog.view.*
import java.util.concurrent.Executor

class HomeActivity : AppCompatActivity(), LoginDialog.LoginDialogListener {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        mAuth = FirebaseAuth.getInstance()

        executor = ContextCompat.getMainExecutor(applicationContext)

        // Click on book icon
        personnalInfoButton.setOnClickListener {
            bioSetup(PersonalInfoActivity::class.java)
            bioAuth(PersonalInfoActivity::class.java)
        }

        // Click on book text
        textInfirmiers.setOnClickListener {
            bioSetup(PersonalInfoActivity::class.java)
            bioAuth(PersonalInfoActivity::class.java)
        }

        // Click on patient icon
        patientsInfoButton.setOnClickListener {
            bioSetup(PatientsInfoActivity::class.java)
            bioAuth(PatientsInfoActivity::class.java)
        }

        // Click on patient text
        textPatient.setOnClickListener {
            bioSetup(PatientsInfoActivity::class.java)
            bioAuth(PatientsInfoActivity::class.java)
        }

        logoutHomeButton.setOnClickListener {
            mAuth.signOut()
            Toast.makeText(applicationContext, "Disconnected", Toast.LENGTH_LONG).show()
            newIntent(applicationContext, LoginActivity::class.java)
        }
    }

    private fun passwordAuth(clazz: Class<*>) {
        val logDialog = LoginDialog(clazz)
        logDialog.show(supportFragmentManager, "login")
    }

    override fun onDialogNegativeClick(dialog: Dialog?) {
        dialog?.cancel()
    }

    override fun onDialogPositiveClick(dialog: DialogFragment, view: View, clazz: Class<*>) {
        val email = view.emailDialog.text.toString()
        val pass = view.passDialog.text.toString()

        if (email.isEmpty() || pass.isEmpty()) {
            Toast.makeText(applicationContext, "Fill the fields first", Toast.LENGTH_SHORT).show()
        } else {
            mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    if (mAuth.currentUser?.isEmailVerified!!) {
                        Toast.makeText(applicationContext, "Success", Toast.LENGTH_SHORT).show()
                        newIntent(applicationContext, clazz)
                    } else {
                        Toast.makeText(applicationContext, "Email must be verified", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(applicationContext, "Authentication Failed", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun bioAuth(clazz: Class<*>) {
        val biometricManager = BiometricManager.from(applicationContext)

        when (biometricManager.canAuthenticate()) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                val str = "The app can use biometric auth"
                Toast.makeText(applicationContext, str, Toast.LENGTH_SHORT).show()
            }

            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                val str = "The phone does not support biometric auth"
                Toast.makeText(applicationContext, str, Toast.LENGTH_SHORT).show()
                passwordAuth(clazz)
            }

            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                val str = "Biometric auth is not available"
                Toast.makeText(applicationContext, str, Toast.LENGTH_SHORT).show()
                passwordAuth(clazz)
            }

            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                val str = "The user didn't set up biometric auth"
                Toast.makeText(applicationContext, str, Toast.LENGTH_SHORT).show()
            }
        }

        biometricPrompt.authenticate(promptInfo)
    }

    private fun bioSetup(clazz: Class<*>) {
        biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    // errorCode 13 ==> NegativeButton Event
                    val text: String = if (errorCode == 13)
                        "Authentication canceled"
                    else
                        "Authentication issue : $errString"

                    Toast.makeText(applicationContext, text, Toast.LENGTH_SHORT).show()
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    newIntent(applicationContext, clazz)
                    Toast.makeText(applicationContext, "Authentication succeeded", Toast.LENGTH_SHORT).show()
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(applicationContext, "Authentication issue", Toast.LENGTH_SHORT).show()
                }
            })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Get access to your information")
            .setSubtitle("Please use the biometric authentication to continue")
            .setNegativeButtonText("Cancel")
            .build()
    }

    override fun onStart() {
        super.onStart()
        mAuth = FirebaseAuth.getInstance()
        checkIfAuth(mAuth)
    }

    private fun checkIfAuth(mAuth: FirebaseAuth) {
        if (mAuth.currentUser == null) {
            newIntent(applicationContext, LoginActivity::class.java)
        }
    }

    // Start new activity
    private fun newIntent(context: Context, clazz: Class<*>) {
        startActivity(Intent(context, clazz))
    }

    override fun onBackPressed() {
    }
}
