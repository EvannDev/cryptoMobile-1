package fr.isen.gerbisnucleaires.secugerbisnucleaires

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import fr.isen.gerbisnucleaires.secugerbisnucleaires.dataclass.Nurse
import kotlinx.android.synthetic.main.activity_sign_up.*


class SignUpActivity : AppCompatActivity() {


    private val TAG = "SignUpActivity"
    private lateinit var mAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        mAuth = FirebaseAuth.getInstance()

        buttonsignup.setOnClickListener{
            registerUser()
        }
    }

    private fun registerUser() {
        val email = emailSignUpEdit.text.toString()
        val password = passwordSignUpEdit.text.toString()
        val firstname = firstnameSignUpEdit.text.toString()
        val lastname = lastnameSignUpEdit.text.toString()
        val phone = phoneSignUpEdit.text.toString()
        val adminCode = codeAdminEdit.text.toString()


        if( email.isEmpty()     ||
            password.isEmpty()  ||
            firstname.isEmpty() ||
            lastname.isEmpty()  ||
            phone.isEmpty()     ||
            adminCode.isEmpty()){
            Toast.makeText(this,"You should fill everything ! ", Toast.LENGTH_LONG).show()
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(this,"Invalid Email ", Toast.LENGTH_LONG).show()
        }
        else if (password.length < 12){
            Toast.makeText(this,"Password should be longer than 12 characters ", Toast.LENGTH_LONG).show()
        }
        else if (!Patterns.PHONE.matcher(phone).matches()){
            Toast.makeText(this,"Invalid phone number ", Toast.LENGTH_LONG).show()
        }

        else if (!adminCode.equals("1234")){
            Toast.makeText(this,"Invalid Admin Code ", Toast.LENGTH_LONG).show()
        }
        else{
            createAccount()
        }
    }

    private fun createAccount() {

        val email = emailSignUpEdit.text.toString()
        val password = passwordSignUpEdit.text.toString()
        val firstname = firstnameSignUpEdit.text.toString()
        val lastname = lastnameSignUpEdit.text.toString()
        val phone = phoneSignUpEdit.text.toString()
        val adminCode = codeAdminEdit.text.toString()

        val nurseId = FirebaseDatabase.getInstance().reference.push().key.toString()

        val nurse = Nurse(nurseId, firstname, lastname, phone, email)

        FirebaseDatabase.getInstance().getReference("Nurse").child(nurseId).setValue(nurse).addOnCompleteListener {
            Toast.makeText(this, "Registered", Toast.LENGTH_LONG).show()
        }

        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) {
                if (it.isSuccessful) {
                    Toast.makeText(this, "Registered", Toast.LENGTH_LONG).show()
                    sendEmailVerification()
                    goToLogin()
                    finish()
                } else {
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun sendEmailVerification(){
        val user = mAuth.currentUser
        user?.sendEmailVerification()
            ?.addOnCompleteListener(this) { task ->

                if (task.isSuccessful) {
                    Toast.makeText(baseContext,
                        "Verification email sent to ${user.email} ",
                        Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(baseContext,
                        "Failed to send verification email.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun goToLogin() {
        val homeIntent = Intent(
            this,
            LoginActivity::class.java
        )
        startActivity(homeIntent)
    }



}