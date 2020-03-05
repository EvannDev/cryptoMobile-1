package fr.isen.gerbisnucleaires.secugerbisnucleaires

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import fr.isen.gerbisnucleaires.secugerbisnucleaires.dataclass.Nurse
import fr.isen.gerbisnucleaires.secugerbisnucleaires.dataclass.SecuGerbis
import kotlinx.android.synthetic.main.activity_sign_up.*


class SignUpActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        mAuth = FirebaseAuth.getInstance()

        buttonCancel.setOnClickListener {
            newIntent(applicationContext, LoginActivity::class.java)
        }

        buttonsignup.setOnClickListener {
            registerUser()
        }
    }

    private fun registerUser() {
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("Code_Admin")

        val signUpListener = object : ValueEventListener {

            @RequiresApi(Build.VERSION_CODES.FROYO)
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (childSnapshot in dataSnapshot.children) {
                    checkField(childSnapshot.value.toString())
                }
            }

            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(applicationContext, "Can't read information from Firebase", Toast.LENGTH_LONG).show()
            }
        }

        myRef.addValueEventListener(signUpListener)
    }

    @RequiresApi(Build.VERSION_CODES.FROYO)
    private fun checkField(adminCodeKey: String) {
        val email = emailSignUpEdit.text.toString()
        val password = passwordSignUpEdit.text.toString()
        val confirmPassword = confirmPasswordSignUpEdit.text.toString()
        val firstName = firstnameSignUpEdit.text.toString()
        val lastName = lastnameSignUpEdit.text.toString()
        val phone = phoneSignUpEdit.text.toString()
        val adminCode = codeAdminEdit.text.toString()

        if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || firstName.isEmpty()
            || lastName.isEmpty() || phone.isEmpty() || adminCode.isEmpty()
        ) {
            Toast.makeText(applicationContext, "Every Field must be fill ! ", Toast.LENGTH_LONG).show()
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(applicationContext, "Invalid Email ", Toast.LENGTH_LONG).show()
        } else if (password.length < 12) {
            Toast.makeText(applicationContext, "Password should be longer than 12 characters ", Toast.LENGTH_LONG).show()
        } else if (!Patterns.PHONE.matcher(phone).matches()) {
            Toast.makeText(applicationContext, "Invalid phone number ", Toast.LENGTH_LONG).show()
        } else if (password != confirmPassword) {
            Toast.makeText(applicationContext, "Password and Confirm Password fields must be equals ", Toast.LENGTH_LONG).show()
        } else if (adminCode != adminCodeKey) {
            Toast.makeText(applicationContext, "Invalid Admin Code ", Toast.LENGTH_LONG).show()
        } else {
            createAccount()
        }
    }

    private fun createAccount() {
        val realEmail = emailSignUpEdit.text.toString()
        val realPassword = passwordSignUpEdit.text.toString()

        val email = SecuGerbis(emailSignUpEdit.text.toString()).encrypt()
        val password = SecuGerbis(passwordSignUpEdit.text.toString()).encrypt()
        val firstName = SecuGerbis(firstnameSignUpEdit.text.toString()).encrypt()
        val lastName = SecuGerbis(lastnameSignUpEdit.text.toString()).encrypt()
        val phone = SecuGerbis(phoneSignUpEdit.text.toString()).encrypt()


        mAuth.createUserWithEmailAndPassword(realEmail, realPassword)
            .addOnCompleteListener(this) {
                if (it.isSuccessful) {
                    Toast.makeText(applicationContext, "$firstName $lastName has been added to Nurses list", Toast.LENGTH_LONG).show()
                    sendEmailVerification()

                    fillRealTimeDatabase(firstName, lastName, email, phone, password)

                    newIntent(applicationContext, LoginActivity::class.java)
                    finish()
                } else {
                    Toast.makeText(
                        baseContext, "$firstName $lastName is already registered",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun fillRealTimeDatabase(firstName: String, lastName: String, email: String, phone: String, password: String) {
        val nurseId = mAuth.currentUser?.uid.toString()
        val nurse = Nurse(nurseId, firstName, lastName, phone, email, password)

        FirebaseDatabase.getInstance().getReference("Nurse").child(nurseId).setValue(nurse).addOnCompleteListener {
            Toast.makeText(applicationContext, "Registered", Toast.LENGTH_LONG).show()
        }

    }

    private fun sendEmailVerification() {
        val user = mAuth.currentUser
        user?.sendEmailVerification()
            ?.addOnCompleteListener(this) { task ->

                if (task.isSuccessful) {
                    Toast.makeText(baseContext, "Verification email sent to ${user.email} ", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(baseContext, "Failed to send verification email.", Toast.LENGTH_SHORT).show()
                }
            }
    }

    // Start new activity
    private fun newIntent(context: Context, clazz: Class<*>) {
        startActivity(Intent(context, clazz))
    }
}
