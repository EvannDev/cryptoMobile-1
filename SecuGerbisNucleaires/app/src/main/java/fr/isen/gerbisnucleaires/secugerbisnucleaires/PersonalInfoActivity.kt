package fr.isen.gerbisnucleaires.secugerbisnucleaires

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_edit_personal.*
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_personal_item.*

class PersonalInfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personal_item)

        val user = FirebaseAuth.getInstance().currentUser

        FirebaseDatabase.getInstance().reference
            .child("Nurse")
            .child(user!!.uid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
        val ref = FirebaseDatabase.getInstance()
        val user = FirebaseAuth.getInstance().currentUser

        val dataOfflineRef = FirebaseDatabase.getInstance().getReference("Nurse/")
        dataOfflineRef.keepSynced(true)

        //Detecting Connection State
        val connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected")
        connectedRef.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(this@PersonalInfoActivity,"onCancelled",Toast.LENGTH_SHORT).show()
            }

            override fun onDataChange(p0: DataSnapshot) {
               val connected = p0.getValue(Boolean::class.java) ?: false
                if(connected){
                    Toast.makeText(this@PersonalInfoActivity,"Online",Toast.LENGTH_LONG).show()
                }
                else{
                    Toast.makeText(this@PersonalInfoActivity,"Offline",Toast.LENGTH_LONG).show()
                }
            }
        })


        ref.reference
            .child("Nurse")

            .child(user!!.uid)
            .addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {
                    Toast.makeText(applicationContext, "Can't read data from database", Toast.LENGTH_LONG).show()
                }

                override fun onDataChange(p0: DataSnapshot) {
                    val map = p0.value as Map<String, Any>
                    firstnameNurse.text = map["firstname"].toString()
                    lastnameNurse.text = map["lastname"].toString()
                    phoneNurse.text = map["phone"].toString()
                    emailNurse.text = map["email"].toString()
                    editButtonClick(map["firstname"].toString(), map["lastname"].toString(), map["phone"].toString(), map["email"].toString())
                }
            })

        homeButton.setOnClickListener{
            newIntent(this, HomeActivity::class.java)
            this.finish()
        }
    }

    fun editButtonClick(firstname: String, lastname: String, phone: String, email: String) {
        buttonEdit.setOnClickListener {
            val intent = Intent(this, EditPersonalActivity::class.java)
            intent.putExtra("firstname", firstname)
            intent.putExtra("lastname", lastname)
            intent.putExtra("phone", phone)
            intent.putExtra("email", email)
            startActivity(intent)
            this.finish()
        }
    }
}
