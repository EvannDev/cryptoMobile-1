package fr.isen.gerbisnucleaires.secugerbisnucleaires

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import fr.isen.gerbisnucleaires.secugerbisnucleaires.recyclerview.patient.Patient
import kotlinx.android.synthetic.main.activity_specific_visit.*


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class SpecificVisitActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth

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
        var patientFirstName: String
        var patientLastName: String
        var patientAge: String
        var patientDisease: String

        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("Patients")

        val patientListener = object : ValueEventListener {

            @SuppressLint("SetTextI18n")
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (childSnapshot in dataSnapshot.children) {
                    val patient = childSnapshot.getValue(Patient::class.java)!!
                    if (patient.uuid == patientId) {
                        specificVisitTitle.text = "Visit of ${patient.name.title} ${patient.name.firstName} ${patient.name.name}"
                        patientTitle = patient.name.title
                        patientFirstName = patient.name.firstName
                        patientLastName = patient.name.name
                        patientAge = patient.age.toString()
                        patientDisease = patient.disease

                        cancelButton(patientId, patientTitle, patientFirstName, patientLastName, patientAge, patientDisease)
                        editVisitButton(
                            patientId,
                            patientTitle,
                            patientFirstName,
                            patientLastName,
                            patientAge,
                            patientDisease,
                            uuid,
                            dateOfVisit,
                            temperature,
                            treatment,
                            patientState
                        )
                        deleteVisit(uuid, patientId, patientTitle, patientFirstName, patientLastName, patientAge, patientDisease)
                    }
                }
            }

            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(applicationContext, "Can't read information from Firebase", Toast.LENGTH_LONG).show()
            }
        }
        myRef.addValueEventListener(patientListener)

        specificVisitDateValue.text = dateOfVisit
        specificVisitTemperatureValue.text = temperature
        specificVisitTreatmentValue.text = treatment
        specificVisitTreatmentValue.movementMethod = ScrollingMovementMethod()
        specificVisitPatientStateValue.text = patientState
        specificVisitPatientStateValue.movementMethod = ScrollingMovementMethod()
    }

    override fun onStart() {
        super.onStart()
        mAuth = FirebaseAuth.getInstance()
        checkIfAuth(mAuth)
    }

    fun cancelButton(
        patientId: String,
        patientTitle: String,
        patientFirstName: String,
        patientLastName: String,
        patientAge: String,
        patientDisease: String
    ) {
        specificVisitCancelButton.setOnClickListener {
            val intent = Intent(this@SpecificVisitActivity, SpecificPatientActivity::class.java)
            intent.putExtra("uuid", patientId)
            intent.putExtra("title", patientTitle)
            intent.putExtra("first_name", patientFirstName)
            intent.putExtra("last_name", patientLastName)
            intent.putExtra("age", patientAge)
            intent.putExtra("disease", patientDisease)
            startActivity(intent)
        }
    }

    fun deleteVisit(
        uuid: String,
        patientId: String,
        patientTitle: String,
        patientFirstName: String,
        patientLastName: String,
        patientAge: String,
        patientDisease: String
    ) {
        specificVisitDeleteButton.setOnClickListener {
            FirebaseDatabase.getInstance().getReference("Visits").child(uuid).removeValue()
            val intent = Intent(this@SpecificVisitActivity, SpecificPatientActivity::class.java)
            intent.putExtra("uuid", patientId)
            intent.putExtra("title", patientTitle)
            intent.putExtra("first_name", patientFirstName)
            intent.putExtra("last_name", patientLastName)
            intent.putExtra("age", patientAge)
            intent.putExtra("disease", patientDisease)
            startActivity(intent)
        }
    }

    fun editVisitButton(
        patientId: String,
        patientTitle: String,
        patientFirstName: String,
        patientLastName: String,
        patientAge: String,
        patientDisease: String,
        uuid: String,
        dateOfVisit: String,
        temperature: String,
        treatment: String,
        patientState: String
    ) {
        specificVisitEditButton.setOnClickListener {
            val intent = Intent(this@SpecificVisitActivity, AddVisitActivity::class.java)
            intent.putExtra("patientUuid", patientId)
            intent.putExtra("patientTitle", patientTitle)
            intent.putExtra("patientLastname", patientFirstName)
            intent.putExtra("patientFirstname", patientLastName)
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
