package fr.isen.gerbisnucleaires.secugerbisnucleaires

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase
import fr.isen.gerbisnucleaires.secugerbisnucleaires.R

class EditPersonalActivity : AppCompatActivity() {

    lateinit var firstNameText: EditText
    lateinit var lastNameText: EditText
    lateinit var phoneText: EditText
    lateinit var emailText: EditText
    lateinit var passwordText: EditText
    lateinit var buttonSave: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_personal)

        firstNameText = findViewById(R.id.firstnameModify)
        lastNameText = findViewById(R.id.lastnameModify)
        phoneText = findViewById(R.id.phoneModify)
        emailText = findViewById(R.id.emailModify)
        passwordText = findViewById(R.id.passwordModifyLast)
        buttonSave = findViewById(R.id.buttonSaveChanges)

        buttonSave.setOnClickListener {
            saveData()
            goToPersonnal()
        }
    }

    private fun saveData(){
        val firstname = firstNameText.text.toString()
        val lastname = lastNameText.text.toString()
        val phone = phoneText.text.toString()
        val email = emailText.text.toString()
        val password = passwordText.text.toString()
        if(firstname.isEmpty() || lastname.isEmpty() || phone.isEmpty() || email.isEmpty() || password.isEmpty()){
            firstNameText.error = "Please enter a firstname"
            lastNameText.error = "Please enter a lastname"
            phoneText.error = "Please enter a number"
            emailText.error = "Please enter an email"
            passwordText.error = "Please enter a password"
            return
        }

        var map = mutableMapOf<String, Any>()
        map["firstname"] = firstname
        map["lastname"] = lastname
        map["phone"] = phone
        map["email"] = email
        map["password"] = password

        val ref = FirebaseDatabase.getInstance().reference

        val user = FirebaseAuth.getInstance().currentUser

        user?.updateEmail(email)?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("TAG", "User email address updated.")
            }
        }

        user?.updatePassword(password)?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("TAG", "User password updated.")
            }
        }


        ref.child("Nurse").child(user!!.uid).updateChildren(map).addOnCompleteListener {
            Toast.makeText(applicationContext, "Changes saved", Toast.LENGTH_LONG).show()
        }

    }

    private fun goToPersonnal() {
        val personnalIntent = Intent(
            this,
            PersonalInfoActivity::class.java
        )
        startActivity(personnalIntent)
    }
}
