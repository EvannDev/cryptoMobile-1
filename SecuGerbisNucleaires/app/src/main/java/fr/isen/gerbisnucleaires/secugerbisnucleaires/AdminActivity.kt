package fr.isen.gerbisnucleaires.secugerbisnucleaires

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import fr.isen.gerbisnucleaires.secugerbisnucleaires.dataclass.Nurse
import fr.isen.gerbisnucleaires.secugerbisnucleaires.dataclass.SecuGerbis
import fr.isen.gerbisnucleaires.secugerbisnucleaires.recyclerview.NurseAdapter
import kotlinx.android.synthetic.main.activity_admin.*
import kotlinx.android.synthetic.main.activity_nurse_item.*
import kotlinx.android.synthetic.main.activity_patients_info.*

class AdminActivity : AppCompatActivity() {

    fun onItemClick(nurse: Nurse) {
        /*val intent = Intent(this@AdminActivity, SpecificPatientActivity::class.java)
        intent.putExtra("uuid", patient.uuid)
        intent.putExtra("title", patient.name.title)
        intent.putExtra("first_name", patient.name.firstName)
        intent.putExtra("last_name", patient.name.name)
        intent.putExtra("age", patient.age)
        intent.putExtra("disease", patient.disease)
        startActivity(intent)*/
        confirmNurseButton.setOnClickListener {
            Log.d("TEST", "Confirm Button")
        }
        rejectNurseButton.setOnClickListener {
            Log.d("TEST", "Reject Button")
        }
    }

    val nurses: ArrayList<Nurse> = arrayListOf()
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("Nurse")

        val postListener = object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (childSnapshot in dataSnapshot.children) {
                    val nurse = childSnapshot.getValue(Nurse::class.java)!!
                    if(SecuGerbis(nurse.access).decrypt() == "Confirm"){
                        nurses.add(nurse)
                    }

                    newNurseRecycler.layoutManager = LinearLayoutManager(applicationContext)
                    newNurseRecycler.adapter = NurseAdapter(nurses, this@AdminActivity)
                }
            }

            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(applicationContext, "Can't read informations from Firebase", Toast.LENGTH_LONG)
                    .show()
            }
        }
        myRef.addValueEventListener(postListener)

        changeAdminCodeButtonClick()
    }

    fun changeAdminCodeButtonClick(){
        changeAdminCodeButton.setOnClickListener {
            FirebaseDatabase.getInstance().getReference("Code_Admin").child("key").setValue(adminCodeValue.text.toString()).addOnCompleteListener {
                Toast.makeText(applicationContext, "Code Admin have been changed", Toast.LENGTH_LONG).show()
                adminCodeValue.setText("")
            }
        }
    }
}
