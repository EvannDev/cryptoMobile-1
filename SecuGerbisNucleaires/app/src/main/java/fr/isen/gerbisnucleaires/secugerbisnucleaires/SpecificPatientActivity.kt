package fr.isen.gerbisnucleaires.secugerbisnucleaires

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import fr.isen.gerbisnucleaires.secugerbisnucleaires.recyclerview.Visit
import fr.isen.gerbisnucleaires.secugerbisnucleaires.recyclerview.VisitAdapter
import kotlinx.android.synthetic.main.activity_specific_patient.*

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class SpecificPatientActivity : AppCompatActivity(), VisitAdapter.OnItemClickListener {

    val visits: ArrayList<Visit> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_specific_patient)

        val uuid = intent.getStringExtra("uuid")
        val title = intent.getStringExtra("title")
        val firstName = intent.getStringExtra("first_name")
        val lastName = intent.getStringExtra("last_name")
        val age = intent.getStringExtra("age")
        val disease = intent.getStringExtra("disease")

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
                    if(visit.patientId == uuid) {
                        visits.add(visit)
                    }

                    visitRecycler.layoutManager = LinearLayoutManager(this@SpecificPatientActivity)
                    visitRecycler.adapter = VisitAdapter(visits,this@SpecificPatientActivity)
                }
            }

            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(this@SpecificPatientActivity, "Can't read informations from Firebase", Toast.LENGTH_LONG).show()
            }
        }
        myRef.addValueEventListener(visitListener)
    }

    fun  cancelButton() {
        SpecificPatientCancelButton.setOnClickListener {
            val intent = Intent(this@SpecificPatientActivity, PatientsInfoActivity::class.java)
            startActivity(intent)
        }
    }

    fun updatePatientInfo(uuid : String, title : String, lastName : String, firstName : String, age : String, disease : String) {
        SpecificPatientEditButton.setOnClickListener {

            val intent = Intent(this@SpecificPatientActivity, AddPatientActivity::class.java)
            intent.putExtra("uuid", uuid)
            intent.putExtra("title", title)
            intent.putExtra("first_name", firstName)
            intent.putExtra("last_name",lastName)
            intent.putExtra("age", age)
            intent.putExtra("disease", disease)
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
    }

    private fun deletePatient(uuid : String){
        SpecificPatientDeletePatientButton.setOnClickListener {
            val databaseVisit = FirebaseDatabase.getInstance()
            val myRefVisit = databaseVisit.getReference("Visits")

            val visitListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (childSnapshot in dataSnapshot.children) {
                        val visit = childSnapshot.getValue(Visit::class.java)!!
                        if(visit.patientId == uuid) {
                            myRefVisit.child(visit.uuid).removeValue()
                        }
                    }
                }

                override fun onCancelled(p0: DatabaseError) {
                    Toast.makeText(this@SpecificPatientActivity, "Can't read informations from Firebase", Toast.LENGTH_LONG).show()
                }
            }
            myRefVisit.addValueEventListener(visitListener)

            FirebaseDatabase.getInstance().getReference("Patients").child(uuid).removeValue()
            val intent = Intent(this@SpecificPatientActivity, PatientsInfoActivity::class.java)
            startActivity(intent)
        }
    }

    fun addVisit(patientUuid : String, title : String, lastName : String, firstName : String, age : String, disease: String){
        SpecificPatientAddVisitButton.setOnClickListener {
            val intent = Intent(this@SpecificPatientActivity, AddVisitActivity::class.java)
            intent.putExtra("patientUuid", patientUuid)
            intent.putExtra("patientTitle", title)
            intent.putExtra("patientLastname", lastName)
            intent.putExtra("patientFirstname", firstName)
            intent.putExtra("patientAge", age)
            intent.putExtra("patientDisease", disease)
            intent.putExtra("uuid", "")
            intent.putExtra("dateOfVisit", "")
            intent.putExtra("temperature", "")
            intent.putExtra("treatment", "")
            intent.putExtra("patientState", "")
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

}