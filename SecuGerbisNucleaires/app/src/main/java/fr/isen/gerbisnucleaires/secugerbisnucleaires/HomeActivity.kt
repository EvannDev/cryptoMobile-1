package fr.isen.gerbisnucleaires.secugerbisnucleaires

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_home.*
import java.util.concurrent.Executor

class HomeActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        mAuth = FirebaseAuth.getInstance()
        bioSetup()

        // Click on book icon
        personnalInfoButton.setOnClickListener {
            bioAuth()
        }

        // Click on book text
        textInfirmiers.setOnClickListener {
            bioAuth()
        }

        // Click on patient icon
        patientsInfoButton.setOnClickListener {
            newIntent(this, PatientsInfoActivity::class.java)
        }

        // Click on patient text
        textPatient.setOnClickListener {
            newIntent(this, PatientsInfoActivity::class.java)
        }

        logoutHomeButton.setOnClickListener {
            mAuth.signOut()
            newIntent(this, LoginActivity::class.java)
        }
    }

    private fun bioAuth() {
        val biometricManager = BiometricManager.from(this)

        when (biometricManager.canAuthenticate()) {
            BiometricManager.BIOMETRIC_SUCCESS ->
                Log.d(TAG, "L'app peut utiliser la biométrie")
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE ->
                Log.e(TAG, "Le support ne possède pas de biométrie")
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE ->
                Log.e(TAG, "La biométrie n'est pas disponible")
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED ->
                Log.e(TAG, "L'utilisateur n'a pas configuré la biométrie")

        }

        biometricPrompt.authenticate(promptInfo)
    }

    private fun bioSetup() {
        executor = ContextCompat.getMainExecutor(this)
        biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    // errorCode 13 ==> NegativeButton Event
                    val text: String = if (errorCode == 13)
                        "Authentification annulée"
                    else
                        "Problème d'authentification : $errString"

                    Toast.makeText(applicationContext, text, Toast.LENGTH_SHORT).show()
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    newIntent(applicationContext, PersonalInfoActivity::class.java)
                    Toast.makeText(applicationContext, "Authentification réussie", Toast.LENGTH_SHORT).show()
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(applicationContext, "Problème d'authentification", Toast.LENGTH_SHORT).show()
                }
            })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Accéder à vos informations")
            .setSubtitle("Utiliser l'authentification biométrique pour continuer")
            .setNegativeButtonText("Annuler")
            .build()
    }

    override fun onStart() {
        super.onStart()
        mAuth = FirebaseAuth.getInstance()
        checkIfAuth(mAuth)
    }

    private fun checkIfAuth(mAuth : FirebaseAuth){
        if(mAuth.currentUser == null){
            newIntent(this@HomeActivity, LoginActivity::class.java)
        }
    }
    // Start new activity
    private fun newIntent(context: Context, clazz: Class<*>) {
        startActivity(Intent(context, clazz))
    }

    override fun onBackPressed() {
    }

    companion object {
        private const val TAG = "SGN"
    }
}
