package fr.isen.gerbisnucleaires.secugerbisnucleaires

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import fr.isen.gerbisnucleaires.secugerbisnucleaires.recyclerview.patient.Patient
import kotlinx.android.synthetic.main.activity_specific_visit.*


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class SpecificVisitActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_specific_visit)

        val uuid = intent.getStringExtra("uuid")
        val patientId = intent.getStringExtra("patientId")
        val temperature = intent.getStringExtra("temperature")
        val treatment = intent.getStringExtra("treatment")
        val patientState = intent.getStringExtra("patientState")
        val dateOfVisit = intent.getStringExtra("dateOfVisit")

        var patientTitle: String
        var patientFirstname: String
        var patientLastname: String
        var patientAge: String
        var patientDisease: String

        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("Patients")

        val patientListener = object : ValueEventListener {

            @SuppressLint("SetTextI18n")
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (childSnapshot in dataSnapshot.children) {
                    val patient = childSnapshot.getValue(Patient::class.java)!!
                    if(patient.uuid == patientId) {
                        specificVisitTitle.text = "Visit of ${patient.name.title} ${patient.name.firstName} ${patient.name.name}"
                        patientTitle = patient.name.title
                        patientFirstname = patient.name.firstName
                        patientLastname = patient.name.name
                        patientAge = patient.age.toString()
                        patientDisease = patient.disease

                        cancelButton(patientId, patientTitle, patientFirstname, patientLastname, patientAge, patientDisease)
                        editVisitButton(patientId, patientTitle, patientFirstname, patientLastname, patientAge, patientDisease, uuid, dateOfVisit, temperature, treatment, patientState)
                        deleteVisit(uuid, patientId, patientTitle, patientFirstname, patientLastname, patientAge, patientDisease)
                    }
                }
            }
            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(this@SpecificVisitActivity, "Can't read informations from Firebase", Toast.LENGTH_LONG).show()
            }
        }
        myRef.addValueEventListener(patientListener)

        specificVisitDateValue.text = dateOfVisit
        specificVisitTemperatureValue.text =temperature
        specificVisitTreatmentValue.text = treatment
        specificVisitTreatmentValue.setMovementMethod(ScrollingMovementMethod())
        specificVisitPatientStateValue.text = patientState
        specificVisitPatientStateValue.setMovementMethod(ScrollingMovementMethod())
    }

    fun cancelButton(patientId : String, patientTitle : String, patientFirstname : String, patientLastname : String, patientAge : String, patientDisease : String) {
        specificVisitCancelButton.setOnClickListener {
            val intent = Intent(this@SpecificVisitActivity, SpecificPatientActivity::class.java)
            intent.putExtra("uuid", patientId)
            intent.putExtra("title", patientTitle)
            intent.putExtra("first_name", patientFirstname)
            intent.putExtra("last_name",patientLastname)
            intent.putExtra("age", patientAge)
            intent.putExtra("disease", patientDisease)
            startActivity(intent)
        }
    }

    fun deleteVisit(uuid : String, patientId : String, patientTitle : String, patientFirstname : String, patientLastname : String, patientAge : String, patientDisease : String){
        specificVisitDeleteButton.setOnClickListener {
            FirebaseDatabase.getInstance().getReference("Visits").child(uuid).removeValue()
            val intent = Intent(this@SpecificVisitActivity, SpecificPatientActivity::class.java)
            intent.putExtra("uuid", patientId)
            intent.putExtra("title", patientTitle)
            intent.putExtra("first_name", patientFirstname)
            intent.putExtra("last_name",patientLastname)
            intent.putExtra("age", patientAge)
            intent.putExtra("disease", patientDisease)
            startActivity(intent)
        }
    }

    fun editVisitButton(patientId : String, patientTitle : String, patientFirstname : String, patientLastname : String, patientAge : String, patientDisease : String, uuid : String, dateOfVisit : String, temperature : String, treatment : String, patientState : String) {
        specificVisitEditButton.setOnClickListener {
            val intent = Intent(this@SpecificVisitActivity, AddVisitActivity::class.java)
            intent.putExtra("patientUuid", patientId)
            intent.putExtra("patientTitle", patientTitle)
            intent.putExtra("patientLastname", patientFirstname)
            intent.putExtra("patientFirstname", patientLastname)
            intent.putExtra("patientAge", patientAge)
            intent.putExtra("patientDisease", patientDisease)
            intent.putExtra("uuid", uuid)
            intent.putExtra("dateOfVisit", dateOfVisit)
            intent.putExtra("temperature", temperature)
            intent.putExtra("treatment", treatment)
            intent.putExtra("patientState", patientState)
            startActivity(intent)
        }
    }
}
