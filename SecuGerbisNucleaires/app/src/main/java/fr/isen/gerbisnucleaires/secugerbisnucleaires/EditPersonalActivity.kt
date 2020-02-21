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
    lateinit var buttonSave: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_personal)

        firstNameText = findViewById(R.id.firstnameModify)
        lastNameText = findViewById(R.id.lastnameModify)
        phoneText = findViewById(R.id.phoneModify)
        emailText = findViewById(R.id.emailModify)
        buttonSave = findViewById(R.id.buttonSaveChanges)

        buttonSave.setOnClickListener {
            saveData()
        }
    }

    private fun saveData(){
        val firstname = firstNameText.text.toString().trim()
        val lastname = lastNameText.text.toString().trim()
        val phone = phoneText.text.toString().trim()
        val email = emailText.text.toString().trim()
        if(firstname.isEmpty() || lastname.isEmpty() || phone.isEmpty() || email.isEmpty()){
            firstNameText.error = "Please enter a firstname"
            lastNameText.error = "Please enter a lastname"
            phoneText.error = "Please enter a number"
            emailText.error = "Please enter an email"
            return
        }

        val ref = FirebaseDatabase.getInstance().getReference("nurses")
        val nurseID = ref.push().key!!

        val nurse = Nurse(nurseID, firstname, lastname, phone, email)

        ref.child(nurseID).setValue(nurse).addOnCompleteListener {
            Toast.makeText(applicationContext, "Changes saved", Toast.LENGTH_LONG).show()
        }
    }
}
