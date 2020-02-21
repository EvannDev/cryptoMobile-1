package fr.isen.gerbisnucleaires.secugerbisnucleaires

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_home.*
import java.util.concurrent.Executor

class HomeActivity : AppCompatActivity() {

    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

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
    }

    private fun bioSetup() {
        executor = ContextCompat.getMainExecutor(this)
        biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    Toast.makeText(applicationContext, "Problème d'authentification : $errString", Toast.LENGTH_SHORT)
                        .show()
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    Toast.makeText(applicationContext, "Authentification réussie", Toast.LENGTH_SHORT).show()
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(applicationContext, "Problème d'authentification", Toast.LENGTH_SHORT).show()
                }
            })
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


        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Accéder à vos informations")
            .setSubtitle("Utiliser l'authentification biométrique pour continuer")
            .setNegativeButtonText("Utiliser un mot de passe")
            .build()
    }

    // Start new activity
    private fun newIntent(context: Context, clazz: Class<*>) {
        startActivity(Intent(context, clazz))
    }

    companion object {
        private const val TAG = "SGN"
    }
}
