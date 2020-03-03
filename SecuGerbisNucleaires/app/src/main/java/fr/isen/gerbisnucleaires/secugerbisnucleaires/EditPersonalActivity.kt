package fr.isen.gerbisnucleaires.secugerbisnucleaires

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
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
        passwordText = findViewById(R.id.passwordModify)
        buttonSave = findViewById(R.id.buttonSaveChanges)

        buttonSave.setOnClickListener {
            saveData()
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

        ref.child("Nurse").child("Mettre_Un_Nurse_Id_en_lien_avec_Evann_a_la_connexion").updateChildren(map).addOnCompleteListener {
            Toast.makeText(applicationContext, "Changes saved", Toast.LENGTH_LONG).show()
        }
    }
}
