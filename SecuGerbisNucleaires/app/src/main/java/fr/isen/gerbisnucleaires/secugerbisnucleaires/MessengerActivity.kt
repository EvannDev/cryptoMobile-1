package fr.isen.gerbisnucleaires.secugerbisnucleaires

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_messenger.*

class MessengerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_messenger)

        buttonRegister.setOnClickListener {
            val email = emailRegisterText.text.toString()
            val password = passwordRegisterText.text.toString()

            //Firebase Authentication to create a user with email and password

            FirebaseAuth.getInstance(). createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener{
                    if(!it.isSuccessful) return@addOnCompleteListener

                    Log.d("Main", "Successfully created user")
                    Toast.makeText(this, "Registered", Toast.LENGTH_LONG)
                }
        }

        alreadyHaveAccountText.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}
