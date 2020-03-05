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
import fr.isen.gerbisnucleaires.secugerbisnucleaires.dataclass.SecuGerbis
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
        val patientTitle = SecuGerbis(intent.getStringExtra("patientTitle")).decrypt()
        val patientLastName = SecuGerbis(intent.getStringExtra("patientLastname")).decrypt()
        val patientFirstName = SecuGerbis(intent.getStringExtra("patientFirstname")).decrypt()
        val patientAge = SecuGerbis(intent.getStringExtra("patientAge")).decrypt()
        val patientDisease = SecuGerbis(intent.getStringExtra("patientDisease")).decrypt()
        val uuid = intent.getStringExtra("uuid")
        val dateOfVisit = SecuGerbis(intent.getStringExtra("dateOfVisit")).decrypt()
        val temperature = SecuGerbis(intent.getStringExtra("temperature")).decrypt()
        val treatment = SecuGerbis(intent.getStringExtra("treatment")).decrypt()
        val patientState = SecuGerbis(intent.getStringExtra("patientState")).decrypt()

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
            intent.putExtra("title", SecuGerbis(patientTitle).encrypt())
            intent.putExtra("first_name", SecuGerbis(patientFirstName).encrypt())
            intent.putExtra("last_name", SecuGerbis(patientLastName).encrypt())
            intent.putExtra("age", SecuGerbis(patientAge).encrypt())
            intent.putExtra("disease", SecuGerbis(patientDisease).encrypt())

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

            val newVisit = Visit(visitId, patientUuid, SecuGerbis(temperature).encrypt(),
                SecuGerbis(treatmentGiven).encrypt(),
                SecuGerbis(patientState).encrypt(),
                SecuGerbis(dateOfVisit).encrypt())

            if (dateOfVisit != "--/--/----" && treatmentGiven != "" && patientState != "" && temperature != "" && (temperature.toDouble() in 30.0..50.0)) {
                ref.child("Visits").child(visitId).setValue(newVisit)

                val intent = Intent(this@AddVisitActivity, SpecificPatientActivity::class.java)
                intent.putExtra("uuid", patientUuid)
                intent.putExtra("title", SecuGerbis(patientTitle).encrypt())
                intent.putExtra("first_name", SecuGerbis(patientFirstName).encrypt())
                intent.putExtra("last_name", SecuGerbis(patientLastName).encrypt())
                intent.putExtra("age", SecuGerbis(patientAge).encrypt())
                intent.putExtra("disease", SecuGerbis(patientDisease).encrypt())
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

            val newVisit = Visit(visitId, patientUuid,
                SecuGerbis(temperature).encrypt(),
                SecuGerbis(treatmentGiven).encrypt(),
                SecuGerbis(patientState).encrypt(),
                SecuGerbis(dateOfVisit).encrypt())

            if (dateOfVisit != "--/--/----" && treatmentGiven != "" && patientState != "" && temperature != "" && (temperature.toDouble() in 30.0..50.0)) {
                ref.child("Visits").child(visitId).setValue(newVisit)

                val intent = Intent(this@AddVisitActivity, SpecificPatientActivity::class.java)
                intent.putExtra("uuid", patientUuid)
                intent.putExtra("title", SecuGerbis(patientTitle).encrypt())
                intent.putExtra("first_name", SecuGerbis(patientFirstName).encrypt())
                intent.putExtra("last_name", SecuGerbis(patientLastName).encrypt())
                intent.putExtra("age", SecuGerbis(patientAge).encrypt())
                intent.putExtra("disease", SecuGerbis(patientDisease).encrypt())
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

    fun dateButtonClick() {
        val textview_date = this.addVisitDateValue

        textview_date?.setOnClickListener {
            DatePickerDialog(this@AddVisitActivity,
                setCalendar(),
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)).show()
        }
    }

    fun setCalendar () :  DatePickerDialog.OnDateSetListener {
        return DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDateInView()
        }
    }

    fun updateDateInView() {
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
