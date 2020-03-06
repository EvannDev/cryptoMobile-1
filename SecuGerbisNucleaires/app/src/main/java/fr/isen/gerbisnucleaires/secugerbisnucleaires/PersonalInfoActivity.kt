package fr.isen.gerbisnucleaires.secugerbisnucleaires

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import fr.isen.gerbisnucleaires.secugerbisnucleaires.dataclass.SecuGerbis
import kotlinx.android.synthetic.main.activity_personal_item.*

class PersonalInfoActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personal_item)

        val user = FirebaseAuth.getInstance().currentUser

        val ref = FirebaseDatabase.getInstance().reference
        ref.keepSynced(true)
        ref
            .child("Nurse")
            .child(user!!.uid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    Toast.makeText(applicationContext, "Cannot read data from database", Toast.LENGTH_LONG).show()
                }

                override fun onDataChange(p0: DataSnapshot) {
                    val map = p0.value as Map<*, *>
                    val firstNameNurseDecode = SecuGerbis(map["firstName"].toString()).decrypt()
                    val lastNameNurseDecode = SecuGerbis(map["lastName"].toString()).decrypt()
                    val phoneNurseDecode = SecuGerbis(map["phone"].toString()).decrypt()
                    val emailNurseDecode = SecuGerbis(map["email"].toString()).decrypt()

                    firstnameNurse.text = firstNameNurseDecode
                    lastnameNurse.text = lastNameNurseDecode
                    phoneNurse.text = phoneNurseDecode
                    emailNurse.text = emailNurseDecode

                    editButtonClick(firstNameNurseDecode, lastNameNurseDecode, phoneNurseDecode, emailNurseDecode)
                }
            })


        homeButton.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        mAuth = FirebaseAuth.getInstance()
        checkIfAuth(mAuth)
    }

    fun editButtonClick(firstName: String, lastName: String, phone: String, email: String) {
        buttonEdit.setOnClickListener {
            val intent = Intent(this, EditPersonalActivity::class.java)
            intent.putExtra("firstName", firstName)
            intent.putExtra("lastName", lastName)
            intent.putExtra("phone", phone)
            intent.putExtra("email", email)
            startActivity(intent)
            this.finish()
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

    override fun onBackPressed() {
    }
}
