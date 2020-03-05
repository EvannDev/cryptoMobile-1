package fr.isen.gerbisnucleaires.secugerbisnucleaires

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import fr.isen.gerbisnucleaires.secugerbisnucleaires.dataclass.SecuGerbis
import fr.isen.gerbisnucleaires.secugerbisnucleaires.recyclerview.PatientAdapter
import fr.isen.gerbisnucleaires.secugerbisnucleaires.recyclerview.patient.Patient
import kotlinx.android.synthetic.main.activity_patients_info.*


class PatientsInfoActivity : AppCompatActivity(), PatientAdapter.OnItemClickListener {
    override fun onItemClick(patient: Patient) {
        val intent = Intent(this@PatientsInfoActivity, SpecificPatientActivity::class.java)
        intent.putExtra("uuid", patient.uuid)
        intent.putExtra("title", patient.name.title)
        intent.putExtra("first_name", patient.name.firstName)
        intent.putExtra("last_name", patient.name.name)
        intent.putExtra("age", patient.age)
        intent.putExtra("disease", patient.disease)
        startActivity(intent)
    }

    val patients: ArrayList<Patient> = arrayListOf()
    private lateinit var mAuth: FirebaseAuth

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_patients_info)

        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("Patients")

        val postListener = object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (childSnapshot in dataSnapshot.children) {
                    val patient = childSnapshot.getValue(Patient::class.java)!!
                    patients.add(patient)

                    patientsRecycler.layoutManager = LinearLayoutManager(applicationContext)
                    patientsRecycler.adapter = PatientAdapter(patients, this@PatientsInfoActivity)
                }
            }

            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(applicationContext, "Can't read informations from Firebase", Toast.LENGTH_LONG)
                    .show()
            }
        }
        myRef.addValueEventListener(postListener)

        addPatientButtonClick()
        returnButton()
    }

    override fun onStart() {
        super.onStart()
        mAuth = FirebaseAuth.getInstance()
        checkIfAuth(mAuth)
    }

    private fun addPatientButtonClick() {
        addPatientButton.setOnClickListener {
            val intent = Intent(applicationContext, AddPatientActivity::class.java)
            intent.putExtra("uuid", "")
            intent.putExtra("title", SecuGerbis("").encrypt())
            intent.putExtra("first_name", SecuGerbis("").encrypt())
            intent.putExtra("last_name", SecuGerbis("").encrypt())
            intent.putExtra("age", SecuGerbis("0").encrypt())
            intent.putExtra("disease", SecuGerbis("").encrypt())
            startActivity(intent)
        }
    }

    private fun returnButton() {
        addPatientReturnButton.setOnClickListener {
            val intent = Intent(applicationContext, HomeActivity::class.java)
            startActivity(intent)
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
}
