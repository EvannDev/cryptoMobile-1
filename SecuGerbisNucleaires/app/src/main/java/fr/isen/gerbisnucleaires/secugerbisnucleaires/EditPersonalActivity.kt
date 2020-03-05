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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.UserProfileChangeRequest
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

        val firstname = intent.getStringExtra("firstname")
        val lastname = intent.getStringExtra("lastname")
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

        firstNameText.setText(firstname?.toString())
        lastNameText.setText(lastname?.toString())
        phoneText.setText(phone?.toString())
        emailText.setText(email?.toString())

        buttonSave.setOnClickListener {
            saveData()
        }

        returnButton.setOnClickListener {
            newIntent(this@EditPersonalActivity, PersonalInfoActivity::class.java)
        }
    }

    override fun onStart() {
        super.onStart()
        mAuth = FirebaseAuth.getInstance()
        checkIfAuth(mAuth)
    }

    private fun saveData(){
        val firstname = firstNameText.text.toString()
        val lastname = lastNameText.text.toString()
        val phone = phoneText.text.toString()
        val email = emailText.text.toString()
        val password = passwordText.text.toString()
        val confirm1 = confirmedPass1.text.toString()
        val confirm2 = confirmedPass2.text.toString()
        if(firstname.isEmpty() && lastname.isEmpty() && phone.isEmpty() && email.isEmpty() && password.isEmpty() && confirm1.isEmpty() && confirm2.isEmpty()){
            firstNameText.error = "Please enter your firstname"
            lastNameText.error = "Please enter your lastname"
            phoneText.error = "Please enter your phone number"
            emailText.error = "Please enter your email"
            passwordText.error = "Please enter your password"
            confirmedPass1.error = "Please enter your new password"
            confirmedPass2.error = "Please confirm your new password"
            return
        }
        else if (lastname.isEmpty()){
            lastNameText.error = "Please enter your lastname"
            return
        }
        else if (phone.isEmpty()){
            phoneText.error = "Please enter your phone number"
            return
        }

        else if (email.isEmpty()){
            emailText.error = "Please enter your email"
            return
        }

        else if(password.isEmpty()){
            passwordText.error = "Please enter your password"
            return
        }

        else{

            if (!confirm1.isEmpty() && confirm1.length < 12){
                Toast.makeText(this,"Password should be longer than 12 characters ", Toast.LENGTH_LONG).show()
            }

            if(!confirm1.equals(confirm2)){
                confirmedPass1.error = "Enter the same password"
                confirmedPass2.error = "Enter the same password"
                return
            }

            var map = mutableMapOf<String, Any>()
            map["firstname"] = SecuGerbis(firstname).chiffrement()
            map["lastname"] = SecuGerbis(lastname).chiffrement()
            map["phone"] = SecuGerbis(phone).chiffrement()
            map["email"] = SecuGerbis(email).chiffrement()
            map["password"] = SecuGerbis(confirm1).chiffrement()


            val mAuth = FirebaseAuth.getInstance()

            mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user = mAuth.currentUser
                        if(!confirm1.isEmpty()){
                            user?.updatePassword(confirm1)
                            map["password"] = SecuGerbis(confirm1).chiffrement()
                        }
                        else{
                            user?.updatePassword(password)
                            map["password"] = password
                        }

                        val ref = FirebaseDatabase.getInstance().reference

                        val user1 = FirebaseAuth.getInstance().currentUser

                        ref.child("Nurse").child(user1!!.uid).updateChildren(map)
                            .addOnCompleteListener {
                                Toast.makeText(applicationContext, "Changes saved", Toast.LENGTH_LONG).show()
                            }
                            newIntent(this@EditPersonalActivity, PersonalInfoActivity::class.java)
                    } else {
                        Toast.makeText(this, "Last password is wrong", Toast.LENGTH_LONG).show()
                    }
                }
        }

    }

    private fun checkIfAuth(mAuth : FirebaseAuth){
        if(mAuth.currentUser == null){
            newIntent(this@EditPersonalActivity, LoginActivity::class.java)
        }
    }
    // Start new activity
    private fun newIntent(context: Context, clazz: Class<*>) {
        startActivity(Intent(context, clazz))
    }
}
