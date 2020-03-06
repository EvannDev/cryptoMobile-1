package fr.isen.gerbisnucleaires.secugerbisnucleaires

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.scottyab.rootbeer.RootBeer
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Offline capabilities
//        FirebaseDatabase.getInstance().setPersistenceEnabled(true)

        checkRootMethod()

        mAuth = FirebaseAuth.getInstance()

        Log.d("EMULATOR", "Is that an emulator = " + isProbablyAnEmulator())

        textbuttonsignin.paintFlags = textbuttonsignin.paintFlags or Paint.UNDERLINE_TEXT_FLAG

        buttonLogin.setOnClickListener {
            doLogin()
        }

        textbuttonsignin.setOnClickListener {
            newIntent(applicationContext, SignUpActivity::class.java)
        }
    }

    // To see if the app is running on an emulator device
    private fun isProbablyAnEmulator() = Build.FINGERPRINT.startsWith("generic")
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
        Toast.makeText(applicationContext, "Disconnected", Toast.LENGTH_LONG).show()
    }

    private fun doLogin() {
        val email = userEdit.text.toString()
        val password = passwordEdit.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(applicationContext, "You should fill everything", Toast.LENGTH_SHORT).show()
        } else {
            mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        if (mAuth.currentUser?.isEmailVerified!!) {
                            Toast.makeText(applicationContext, "Welcome", Toast.LENGTH_SHORT).show()
                            newIntent(applicationContext, HomeActivity::class.java)
                        } else {
                            Toast.makeText(applicationContext, "Email must be verified", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(applicationContext, "Authentication Failed", Toast.LENGTH_LONG).show()
                    }
                }
        }
    }

    // Start new activity
    private fun newIntent(context: Context, clazz: Class<*>) {
        startActivity(Intent(context, clazz))
    }

    override fun onBackPressed() {
    }

    private fun checkRootMethod() {
        val rootBeer = RootBeer(applicationContext)
        if (rootBeer.isRooted) {
            Log.d("ROOT", "Device is ROOT")
        } else {
            Log.d("ROOT", "Device is not ROOT")
        }
    }
}