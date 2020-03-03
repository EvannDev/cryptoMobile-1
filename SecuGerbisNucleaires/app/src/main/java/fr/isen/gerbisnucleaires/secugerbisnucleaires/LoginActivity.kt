package fr.isen.gerbisnucleaires.secugerbisnucleaires

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mAuth = FirebaseAuth.getInstance()

        buttonLogin.setOnClickListener{
            doLogin()
        }

        buttonLogout.setOnClickListener{
            doLogout()
        }

        textbuttonsignin.setOnClickListener{
            goToSignUp()
        }
    }

    private fun doLogout(){
        mAuth.signOut()
        Toast.makeText(this,"Logout successfull", Toast.LENGTH_SHORT)
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

    private fun goToSignUp(){
        val signUpIntent = Intent(
            this,
            SignUpActivity::class.java
        )
        startActivity(signUpIntent)
    }
}