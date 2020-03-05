package fr.isen.gerbisnucleaires.secugerbisnucleaires

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import fr.isen.gerbisnucleaires.secugerbisnucleaires.dataclass.SecuGerbis
import fr.isen.gerbisnucleaires.secugerbisnucleaires.recyclerview.Visit
import fr.isen.gerbisnucleaires.secugerbisnucleaires.recyclerview.VisitAdapter
import kotlinx.android.synthetic.main.activity_specific_patient.*

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class SpecificPatientActivity : AppCompatActivity(), VisitAdapter.OnItemClickListener {

    val visits: ArrayList<Visit> = arrayListOf()
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_specific_patient)

        val uuid = intent.getStringExtra("uuid")
        val title = SecuGerbis(intent.getStringExtra("title")).decrypt()
        val firstName = SecuGerbis(intent.getStringExtra("first_name")).decrypt()
        val lastName = SecuGerbis(intent.getStringExtra("last_name")).decrypt()
        val age = SecuGerbis(intent.getStringExtra("age")).decrypt()
        val disease = SecuGerbis(intent.getStringExtra("disease")).decrypt()

        specificPatientName.text = ("$title $firstName $lastName")
        specificPatientAge.text = ("$age years old")
        specificPatientDisease.text = disease

        updatePatientInfo(uuid, title, lastName, firstName, age, disease)
        addVisit(uuid, title, lastName, firstName, age, disease)
        deletePatient(uuid)
        cancelButton()

        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("Visits")

        val visitListener = object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (childSnapshot in dataSnapshot.children) {
                    val visit = childSnapshot.getValue(Visit::class.java)!!
                    if (visit.patientId == uuid) {
                        visits.add(visit)
                    }

                    visitRecycler.layoutManager = LinearLayoutManager(applicationContext)
                    visitRecycler.adapter = VisitAdapter(visits, this@SpecificPatientActivity)
                }
            }

            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(applicationContext, "Can't read information from Firebase", Toast.LENGTH_LONG).show()
            }
        }
        myRef.addValueEventListener(visitListener)
    }

    override fun onStart() {
        super.onStart()
        mAuth = FirebaseAuth.getInstance()
        checkIfAuth(mAuth)
    }

    private fun cancelButton() {
        SpecificPatientCancelButton.setOnClickListener {
            val intent = Intent(this@SpecificPatientActivity, PatientsInfoActivity::class.java)
            startActivity(intent)
        }
    }

    private fun updatePatientInfo(uuid: String, title: String, lastName: String, firstName: String, age: String, disease: String) {
        SpecificPatientEditButton.setOnClickListener {

            val intent = Intent(this@SpecificPatientActivity, AddPatientActivity::class.java)
            intent.putExtra("uuid", uuid)
            intent.putExtra("title", SecuGerbis(title).encrypt())
            intent.putExtra("first_name", SecuGerbis(firstName).encrypt())
            intent.putExtra("last_name", SecuGerbis(lastName).encrypt())
            intent.putExtra("age", SecuGerbis(age).encrypt())
            intent.putExtra("disease", SecuGerbis(disease).encrypt())
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
    }

    private fun deletePatient(uuid: String) {
        SpecificPatientDeletePatientButton.setOnClickListener {
            val databaseVisit = FirebaseDatabase.getInstance()
            val myRefVisit = databaseVisit.getReference("Visits")

            val visitListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (childSnapshot in dataSnapshot.children) {
                        val visit = childSnapshot.getValue(Visit::class.java)!!
                        if (visit.patientId == uuid) {
                            myRefVisit.child(visit.uuid).removeValue()
                        }
                    }
                }

                override fun onCancelled(p0: DatabaseError) {
                    Toast.makeText(applicationContext, "Can't read information from Firebase", Toast.LENGTH_LONG).show()
                }
            }
            myRefVisit.addValueEventListener(visitListener)

            FirebaseDatabase.getInstance().getReference("Patients").child(uuid).removeValue()
            val intent = Intent(this@SpecificPatientActivity, PatientsInfoActivity::class.java)
            startActivity(intent)
        }
    }

    fun addVisit(patientUuid: String, title: String, lastName: String, firstName: String, age: String, disease: String) {
        SpecificPatientAddVisitButton.setOnClickListener {
            val intent = Intent(this@SpecificPatientActivity, AddVisitActivity::class.java)
            intent.putExtra("patientUuid", patientUuid)
            intent.putExtra("patientTitle", SecuGerbis(title).encrypt())
            intent.putExtra("patientLastname", SecuGerbis(lastName).encrypt())
            intent.putExtra("patientFirstname", SecuGerbis(firstName).encrypt())
            intent.putExtra("patientAge", SecuGerbis(age).encrypt())
            intent.putExtra("patientDisease", SecuGerbis(disease).encrypt())
            intent.putExtra("uuid", "")
            intent.putExtra("dateOfVisit", SecuGerbis("").encrypt())
            intent.putExtra("temperature", SecuGerbis("").encrypt())
            intent.putExtra("treatment", SecuGerbis("").encrypt())
            intent.putExtra("patientState", SecuGerbis("").encrypt())
            startActivity(intent)
        }
    }

    override fun onItemClick(visit: Visit) {
        val intent = Intent(this@SpecificPatientActivity, SpecificVisitActivity::class.java)
        intent.putExtra("uuid", visit.uuid)
        intent.putExtra("patientId", visit.patientId)
        intent.putExtra("temperature", visit.temperature)
        intent.putExtra("treatment", visit.treatment)
        intent.putExtra("patientState", visit.patientState)
        intent.putExtra("dateOfVisit", visit.dateOfVisit)

        startActivity(intent)
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
