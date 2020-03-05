package fr.isen.gerbisnucleaires.secugerbisnucleaires

import android.content.Intent
import android.graphics.Paint
import android.os.Build
import android.os.Bundle
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import fr.isen.gerbisnucleaires.secugerbisnucleaires.dataclass.Nurse
import fr.isen.gerbisnucleaires.secugerbisnucleaires.dataclass.SecuGerbis
import kotlinx.android.synthetic.main.activity_login.*
import java.util.*
import javax.crypto.KeyGenerator


class LoginActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES,"AndroidKeyStore")
        val kenGenParameterSpec = KeyGenParameterSpec.Builder("SecureGerbisKey",
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
            .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
            .build()
        keyGenerator.init(kenGenParameterSpec)
        keyGenerator.generateKey()

        mAuth = FirebaseAuth.getInstance()

        Log.d("EMULATOR", "Is that an emulator = " + isProbablyAnEmulator())

        textbuttonsignin.setPaintFlags(textbuttonsignin.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)

        buttonLogin.setOnClickListener {
            doLogin()
        }

        buttonLogout.setOnClickListener {
            doLogout()
        }

        textbuttonsignin.setOnClickListener {
            goToSignUp()
        }
    }

    // To see if the app is running on an emulator device
    fun isProbablyAnEmulator() = Build.FINGERPRINT.startsWith("generic")
            || Build.FINGERPRINT.startsWith("unknown")
            || Build.MODEL.contains("google_sdk")
            || Build.MODEL.contains("Emulator")
            || Build.MODEL.contains("Android SDK built for x86")
            || Build.BOARD == "QC_Reference_Phone" //bluestacks
            || Build.MANUFACTURER.contains("Genymotion")
            || Build.HOST.startsWith("Build") //MSI App Player
            || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
            || "google_sdk" == Build.PRODUCT

    private fun doLogout() {
        mAuth.signOut()
        Toast.makeText(this, "Deconnected", Toast.LENGTH_LONG).show()
    }

    private fun doLogin() {

        if (UserEdit.text.toString().isEmpty() || PasswordEdit.text.toString().isEmpty()) {
            Toast.makeText(this, "You should fill everything", Toast.LENGTH_SHORT).show()
        }
        else {
            val email = UserEdit.text.toString()
            val password = PasswordEdit.text.toString()

            mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        if(mAuth.currentUser?.isEmailVerified!!){
                            Toast.makeText(this, "Welcome", Toast.LENGTH_SHORT).show()
                            goToHome()
                        }
                        else {
                            Toast.makeText(this, "Email must be verified", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this, "Authentication Failed", Toast.LENGTH_LONG).show()
                    }
                }
        }
    }

    private fun goToHome() {
        val homeIntent = Intent(
            this,
            HomeActivity::class.java
        )
        startActivity(homeIntent)
    }

    private fun goToSignUp() {
        val signUpIntent = Intent(
            this,
            SignUpActivity::class.java
        )
        startActivity(signUpIntent)
    }

    override fun onBackPressed() {
    }
}