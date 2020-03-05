package fr.isen.gerbisnucleaires.secugerbisnucleaires

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import fr.isen.gerbisnucleaires.secugerbisnucleaires.dataclass.SecuGerbis
import kotlinx.android.synthetic.main.activity_edit_personal.*

class EditPersonalActivity : AppCompatActivity() {

    lateinit var firstNameText: EditText
    lateinit var lastNameText: EditText
    lateinit var phoneText: EditText
    lateinit var emailText: EditText
    lateinit var passwordText: EditText
    lateinit var confirmedPass1: EditText
    lateinit var confirmedPass2: EditText
    lateinit var buttonSave: Button

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_personal)

        val firstName = intent.getStringExtra("firstname")
        val lastName = intent.getStringExtra("lastname")
        val phone = intent.getStringExtra("phone")
        val email = intent.getStringExtra("email")

        firstNameText = findViewById(R.id.firstnameModify)
        lastNameText = findViewById(R.id.lastnameModify)
        phoneText = findViewById(R.id.phoneModify)
        emailText = findViewById(R.id.emailModify)
        passwordText = findViewById(R.id.passwordModifyLast)
        confirmedPass1 = findViewById(R.id.confirmedPass1)
        confirmedPass2 = findViewById(R.id.confirmedPass2)
        buttonSave = findViewById(R.id.buttonSaveChanges)

        firstNameText.setText(firstName?.toString())
        lastNameText.setText(lastName?.toString())
        phoneText.setText(phone?.toString())
        emailText.setText(email?.toString())

        buttonSave.setOnClickListener {
            saveData()
        }

        returnButton.setOnClickListener {
            newIntent(applicationContext, PersonalInfoActivity::class.java)
        }
    }

    override fun onStart() {
        super.onStart()
        mAuth = FirebaseAuth.getInstance()
        checkIfAuth(mAuth)
    }

    private fun saveData() {
        val firstName = firstNameText.text.toString()
        val lastName = lastNameText.text.toString()
        val phone = phoneText.text.toString()
        val email = emailText.text.toString()
        val password = passwordText.text.toString()
        val confirm1 = confirmedPass1.text.toString()
        val confirm2 = confirmedPass2.text.toString()
        if (firstName.isEmpty() && lastName.isEmpty() && phone.isEmpty() && email.isEmpty() && password.isEmpty() && confirm1.isEmpty() && confirm2.isEmpty()) {
            firstNameText.error = "Please enter your first name"
            lastNameText.error = "Please enter your last name"
            phoneText.error = "Please enter your phone number"
            emailText.error = "Please enter your email"
            passwordText.error = "Please enter your password"
            confirmedPass1.error = "Please enter your new password"
            confirmedPass2.error = "Please confirm your new password"
            return
        } else if (lastName.isEmpty()) {
            lastNameText.error = "Please enter your last name"
            return
        } else if (phone.isEmpty()) {
            phoneText.error = "Please enter your phone number"
            return
        } else if (email.isEmpty()) {
            emailText.error = "Please enter your email"
            return
        } else if (password.isEmpty()) {
            passwordText.error = "Please enter your password"
            return
        } else {

            if (confirm1.isNotEmpty() && confirm1.length < 12) {
                Toast.makeText(applicationContext, "Password should be longer than 12 characters ", Toast.LENGTH_LONG).show()
            }

            if (confirm1 != confirm2) {
                confirmedPass1.error = "Enter the same password"
                confirmedPass2.error = "Enter the same password"
                return
            }

            val map = mutableMapOf<String, Any>()
            map["firstname"] = SecuGerbis(firstName).encrypt()
            map["lastname"] = SecuGerbis(lastName).encrypt()
            map["phone"] = SecuGerbis(phone).encrypt()
            map["email"] = SecuGerbis(email).encrypt()
            map["password"] = SecuGerbis(confirm1).encrypt()


            val mAuth = FirebaseAuth.getInstance()

            mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user = mAuth.currentUser
                        if (confirm1.isNotEmpty()) {
                            user?.updatePassword(confirm1)
                            map["password"] = SecuGerbis(confirm1).encrypt()
                        } else {
                            user?.updatePassword(password)
                            map["password"] = SecuGerbis(password).decrypt()
                        }

                        val ref = FirebaseDatabase.getInstance().reference

                        val user1 = FirebaseAuth.getInstance().currentUser

                        ref.child("Nurse").child(user1!!.uid).updateChildren(map)
                            .addOnCompleteListener {
                                Toast.makeText(applicationContext, "Changes saved", Toast.LENGTH_LONG).show()
                            }
                        newIntent(applicationContext, PersonalInfoActivity::class.java)
                    } else {
                        Toast.makeText(applicationContext, "Last password is wrong", Toast.LENGTH_LONG).show()
                    }
                }
        }

    }

    private fun checkIfAuth(mAuth: FirebaseAuth) {
        if (mAuth.currentUser == null) {
            newIntent(applicationContext, LoginActivity::class.java)
        }
    }

    // Start new activity
    private fun newIntent(context: Context, clazz: Class<*>) {
        startActivity(Intent(context, clazz))
    }
}
