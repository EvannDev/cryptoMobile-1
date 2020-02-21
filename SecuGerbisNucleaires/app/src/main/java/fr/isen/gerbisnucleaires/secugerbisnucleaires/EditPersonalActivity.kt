package fr.isen.gerbisnucleaires.secugerbisnucleaires

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_edit_personal.*


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

        buttonSaveChanges.setOnClickListener {
            saveData()
        }
    }

    private fun saveData(){
        val firstname = firstNameText.text.toString()
        val lastname = lastNameText.text.toString()
        val phone = phoneText.text.toString()
        val email = emailText.text.toString()

        if(firstname.isEmpty() || lastname.isEmpty() || phone.isEmpty() || email.isEmpty()){
            firstNameText.error = "Please fill this blanks"
        }

        val nurse = Nurse(firstname, lastname, phone, email)
        val database = FirebaseDatabase.getInstance()
        val reference = database.getReference("database/secu-gerbis-nucleaires/data/user/K1QZEtEX7LTf9N96oDSc")
        reference.push().setValue(nurse)
        Toast.makeText(this, "Changes saved", Toast.LENGTH_LONG).show()
    }
}
