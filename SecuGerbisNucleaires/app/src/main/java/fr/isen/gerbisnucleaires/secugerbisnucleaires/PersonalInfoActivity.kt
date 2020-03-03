package fr.isen.gerbisnucleaires.secugerbisnucleaires

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_personal_item.*

class PersonalInfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personal_item)

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
                    Toast.makeText(applicationContext, "Can not read data from database", Toast.LENGTH_LONG).show()
                }

                override fun onDataChange(p0: DataSnapshot) {
                    var map = p0.value as Map<String, Any>
                    firstnameNurse.text = map["firstname"].toString()
                    lastnameNurse.text = map["lastname"].toString()
                    phoneNurse.text = map["phone"].toString()
                    emailNurse.text = map["email"].toString()
                }
            })

        homeButton.setOnClickListener{
            newIntent(this, HomeActivity::class.java)
        }

        buttonEdit.setOnClickListener {
            newIntent(this, EditPersonalActivity::class.java)
        }
    }

    private fun newIntent(context: Context, clazz: Class<*>) {
        startActivity(Intent(context, clazz))
    }
}
