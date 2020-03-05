package fr.isen.gerbisnucleaires.secugerbisnucleaires

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import fr.isen.gerbisnucleaires.secugerbisnucleaires.dataclass.SecuGerbis
import kotlinx.android.synthetic.main.activity_personal_item.*
import java.security.KeyStore
import java.util.*
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

class PersonalInfoActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personal_item)

        isConnect(this)

        val user = FirebaseAuth.getInstance().currentUser

        FirebaseDatabase.getInstance().reference
            .child("Nurse")
            .child(user!!.uid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    Toast.makeText(applicationContext, "Can not read data from database", Toast.LENGTH_LONG).show()
                }

                override fun onDataChange(p0: DataSnapshot) {
                    var map = p0.value as Map<String, Any>
                    val firstnameNurseDecode = SecuGerbis(map["firstname"].toString()).dechiffrement()
                    val lastnameNurseDecode = SecuGerbis(map["lastname"].toString()).dechiffrement()
                    val phoneNurseDecode = SecuGerbis(map["phone"].toString()).dechiffrement()
                    val emailNurseDecode = SecuGerbis(map["email"].toString()).dechiffrement()

                    firstnameNurse.text = firstnameNurseDecode
                    lastnameNurse.text = lastnameNurseDecode
                    phoneNurse.text = phoneNurseDecode
                    emailNurse.text= emailNurseDecode

                    editButtonClick(firstnameNurseDecode,lastnameNurseDecode,phoneNurseDecode,emailNurseDecode)
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

    private fun isConnect(activity: Context){
        //Detecting Connection State
        val connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected")
        connectedRef.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(activity,"onCancelled",Toast.LENGTH_SHORT).show()
            }
            override fun onDataChange(p0: DataSnapshot) {
                val connected = p0.getValue(Boolean::class.java) ?: false
                if(connected){
                    Toast.makeText(activity,"Online",Toast.LENGTH_LONG).show()
                }
                else{
                    Toast.makeText(activity,"Offline",Toast.LENGTH_LONG).show()
                }
            }
        })
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

    private fun checkIfAuth(mAuth : FirebaseAuth){
        if(mAuth.currentUser == null){
            newIntent(this@PersonalInfoActivity, LoginActivity::class.java)
        }
    }
    // Start new activity
    private fun newIntent(context: Context, clazz: Class<*>) {
        startActivity(Intent(context, clazz))
    }
}
