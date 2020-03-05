package fr.isen.gerbisnucleaires.secugerbisnucleaires

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login.*
import java.io.BufferedReader
import java.io.InputStreamReader


class LoginActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        checkRootMethod()

        mAuth = FirebaseAuth.getInstance()

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
                        Toast.makeText(this, "Welcome", Toast.LENGTH_SHORT).show()
                        goToHome()
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
            Toast.makeText(this, "Connected", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Wrong password", Toast.LENGTH_LONG).show();
        }
    }

    private fun checkRootMethod(): Boolean {
        var process: Process? = null
        return try {
            process = Runtime.getRuntime().exec(arrayOf("/system/xbin/which", "su"))
            val bF = BufferedReader(InputStreamReader(process.inputStream))
            if(bF.readLine() != null){
                Log.d("TAG", "true")
                Toast.makeText(this, "True", Toast.LENGTH_LONG).show()
                true
            }
            else{
                Log.d("TAG", "false")
                Toast.makeText(this, "false", Toast.LENGTH_LONG).show()
                false
            }
        } catch (t: Throwable) {
            false
        } finally {
            process?.destroy()
        }
    }
}