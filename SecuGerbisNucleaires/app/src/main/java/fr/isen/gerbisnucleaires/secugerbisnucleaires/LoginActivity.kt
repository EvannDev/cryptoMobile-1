package fr.isen.gerbisnucleaires.secugerbisnucleaires

import android.content.Intent
import android.graphics.Paint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mAuth = FirebaseAuth.getInstance()

        Log.d("EMULATOR", "Is that an emulator = " + isProbablyAnEmulator())

        textbuttonsignin.setPaintFlags(textbuttonsignin.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)

        buttonLogin.setOnClickListener {
            doLogin()
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

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            Toast.makeText(this, "Connected", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, "Wrong password", Toast.LENGTH_LONG).show()
        }
    }
}