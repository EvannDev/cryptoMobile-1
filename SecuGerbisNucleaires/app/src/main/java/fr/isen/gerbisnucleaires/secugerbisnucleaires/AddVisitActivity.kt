package fr.isen.gerbisnucleaires.secugerbisnucleaires

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import fr.isen.gerbisnucleaires.secugerbisnucleaires.recyclerview.Visit
import kotlinx.android.synthetic.main.activity_add_visit.*
import java.text.SimpleDateFormat
import java.util.*

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class AddVisitActivity : AppCompatActivity() {

    var cal = Calendar.getInstance()
    private lateinit var mAuth: FirebaseAuth

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_visit)

        val patientUuid = intent.getStringExtra("patientUuid")
        val patientTitle = intent.getStringExtra("patientTitle")
        val patientLastName = intent.getStringExtra("patientLastname")
        val patientFirstName = intent.getStringExtra("patientFirstname")
        val patientAge = intent.getStringExtra("patientAge")
        val patientDisease = intent.getStringExtra("patientDisease")
        val uuid = intent.getStringExtra("uuid")
        val dateOfVisit = intent.getStringExtra("dateOfVisit")
        val temperature = intent.getStringExtra("temperature")
        val treatment = intent.getStringExtra("treatment")
        val patientState = intent.getStringExtra("patientState")

        if (uuid == "" && dateOfVisit == "" && temperature == "" && treatment == "" && patientState == "") {
            addVisitTitle.text = "Add a Visit for $patientTitle $patientLastName $patientFirstName"
            addVisitButtonClick(patientUuid, patientTitle, patientLastName, patientFirstName, patientAge, patientDisease)
        } else {
            addVisitTitle.text = "Edit Visit for $patientTitle $patientLastName $patientFirstName"
            addVisitButton.text = "Edit Information"
            addVisitDateValue.text = dateOfVisit
            addVisitTemperatureValue.setText(temperature)
            addVisitTreatmentValue.setText(treatment)
            addVisitPatientStateValue.setText(patientState)

            editVisitButtonClick(patientUuid, patientTitle, patientLastName, patientFirstName, patientAge, patientDisease, uuid)
        }

        dateButtonClick()
        cancelButtonClick(patientUuid, patientTitle, patientLastName, patientFirstName, patientAge, patientDisease)

    }

    override fun onStart() {
        super.onStart()
        mAuth = FirebaseAuth.getInstance()
        checkIfAuth(mAuth)
    }

    private fun cancelButtonClick(
        patientUuid: String,
        patientTitle: String,
        patientLastName: String,
        patientFirstName: String,
        patientAge: String,
        patientDisease: String
    ) {
        addVisitCancelButton.setOnClickListener {
            val intent = Intent(this@AddVisitActivity, SpecificPatientActivity::class.java)
            intent.putExtra("uuid", patientUuid)
            intent.putExtra("title", patientTitle)
            intent.putExtra("first_name", patientFirstName)
            intent.putExtra("last_name", patientLastName)
            intent.putExtra("age", patientAge)
            intent.putExtra("disease", patientDisease)

            startActivity(intent)
            this.finish()
        }
    }

    private fun addVisitButtonClick(
        patientUuid: String,
        patientTitle: String,
        patientLastName: String,
        patientFirstName: String,
        patientAge: String,
        patientDisease: String
    ) {
        addVisitButton.setOnClickListener {
            val dateOfVisit = addVisitDateValue.text.toString()
            val temperature = addVisitTemperatureValue.text.toString()
            val treatmentGiven = addVisitTreatmentValue.text.toString()
            val patientState = addVisitPatientStateValue.text.toString()

            val firebase = FirebaseDatabase.getInstance()
            val ref = firebase.reference

            val visitId = ref.child("Visits").push().key.toString()

            val newVisit = Visit(visitId, patientUuid, temperature, treatmentGiven, patientState, dateOfVisit)

            if (dateOfVisit != "--/--/----" && treatmentGiven != "" && patientState != "" && temperature != "" && (temperature.toDouble() in 30.0..50.0)) {
                ref.child("Visits").child(visitId).setValue(newVisit)

                val intent = Intent(this@AddVisitActivity, SpecificPatientActivity::class.java)
                intent.putExtra("uuid", patientUuid)
                intent.putExtra("title", patientTitle)
                intent.putExtra("first_name", patientFirstName)
                intent.putExtra("last_name", patientLastName)
                intent.putExtra("age", patientAge)
                intent.putExtra("disease", patientDisease)
                startActivity(intent)

                Toast.makeText(
                    applicationContext,
                    "The Visit for $patientTitle $patientFirstName $patientLastName have just been added to Firebase",
                    Toast.LENGTH_LONG
                ).show()

                this.finish()
            } else {
                if (dateOfVisit == "--/--/----" || treatmentGiven == "" || patientState == "") {
                    Toast.makeText(applicationContext, "ERROR : All field must be fill !!!", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(applicationContext, "ERROR : Temperature must be set between 30°C and 50°C !!! ", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun editVisitButtonClick(
        patientUuid: String,
        patientTitle: String,
        patientLastName: String,
        patientFirstName: String,
        patientAge: String,
        patientDisease: String,
        visitId: String
    ) {
        addVisitButton.setOnClickListener {
            val dateOfVisit = addVisitDateValue.text.toString()
            val temperature = addVisitTemperatureValue.text.toString()
            val treatmentGiven = addVisitTreatmentValue.text.toString()
            val patientState = addVisitPatientStateValue.text.toString()

            val firebase = FirebaseDatabase.getInstance()
            val ref = firebase.reference

            val newVisit = Visit(visitId, patientUuid, temperature, treatmentGiven, patientState, dateOfVisit)

            if (dateOfVisit != "--/--/----" && treatmentGiven != "" && patientState != "" && temperature != "" && (temperature.toDouble() in 30.0..50.0)) {
                ref.child("Visits").child(visitId).setValue(newVisit)

                val intent = Intent(this@AddVisitActivity, SpecificPatientActivity::class.java)
                intent.putExtra("uuid", patientUuid)
                intent.putExtra("title", patientTitle)
                intent.putExtra("first_name", patientFirstName)
                intent.putExtra("last_name", patientLastName)
                intent.putExtra("age", patientAge)
                intent.putExtra("disease", patientDisease)
                startActivity(intent)

                Toast.makeText(
                    applicationContext,
                    "The Visit for $patientTitle $patientFirstName $patientLastName have just been added to Firebase",
                    Toast.LENGTH_LONG
                ).show()

                this.finish()
            } else {
                if (dateOfVisit == "--/--/----" || treatmentGiven == "" || patientState == "") {
                    Toast.makeText(applicationContext, "ERROR : All field must be fill !!!", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(applicationContext, "ERROR : Temperature must be set between 30°C and 50°C !!! ", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun dateButtonClick() {
        val textViewDate = this.addVisitDateValue

        textViewDate?.setOnClickListener {
            DatePickerDialog(
                applicationContext,
                setCalendar(),
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    private fun setCalendar(): DatePickerDialog.OnDateSetListener {
        return DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDateInView()
        }
    }

    private fun updateDateInView() {
        val myFormat = "dd/MM/yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        addVisitDateValue?.text = sdf.format(cal.time)
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
