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
                override fun onCancelled(p0: DatabaseError) {
                    Toast.makeText(applicationContext, "Can not read data from database", Toast.LENGTH_LONG).show()
                }

                override fun onDataChange(p0: DataSnapshot) {
                    val map = p0.value as Map<*, *>
                    firstnameNurse.text = map["firstname"].toString()
                    lastnameNurse.text = map["lastname"].toString()
                    phoneNurse.text = map["phone"].toString()
                    emailNurse.text = map["email"].toString()
                }
            })


        buttonEdit.setOnClickListener {
            newIntent(this, EditPersonalActivity::class.java)
        }

        buttonReturn.setOnClickListener {
            newIntent(this, HomeActivity::class.java)
        }
    }

    private fun newIntent(context: Context, clazz: Class<*>) {
        startActivity(Intent(context, clazz))
    }
}
