package fr.isen.gerbisnucleaires.secugerbisnucleaires

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_personal_item.*

class PersonalInfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personal_item)

        FirebaseDatabase.getInstance().reference
            .child("Nurse")
            .child("Mettre_Un_Nurse_Id_en_lien_avec_Evann_a_la_connexion")
            .addListenerForSingleValueEvent(object : ValueEventListener {
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


        buttonEdit.setOnClickListener {
            newIntent(this, EditPersonalActivity::class.java)
        }
    }

    private fun newIntent(context: Context, clazz: Class<*>) {
        startActivity(Intent(context, clazz))
    }
}
