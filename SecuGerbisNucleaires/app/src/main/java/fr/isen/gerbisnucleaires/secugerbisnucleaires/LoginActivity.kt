package fr.isen.gerbisnucleaires.secugerbisnucleaires

import android.content.Intent
import android.os.Bundle
import android.graphics.Paint
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.scottyab.rootbeer.RootBeer
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private var postListener: ValueEventListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        getKey()
        checkRootMethod()

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
        } else {
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

    private fun checkRootMethod() {
        val rootBeer = RootBeer(applicationContext)
        if(rootBeer.isRooted()) {
            Log.d("ROOT", "Le device est root")
        } else {
            Log.d("ROOT", "Le device n'est pas root")
        }
    }

    private fun getKey() {
        val ref = FirebaseDatabase.getInstance().getReference("KeyStore")
        val postListener = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                println("COUCOUCOUCOUCOUCOCUOCUCOUCOU")
            }

            override fun onDataChange(p0: DataSnapshot) {
                val key = p0.child("KeySymUser").value.toString()
                println("OUIOUIUOIUOIEUJOIEUROIJFOIUF $key")

            }
        }

        ref.addValueEventListener(postListener)

    }
}