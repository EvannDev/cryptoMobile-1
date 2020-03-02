package fr.isen.gerbisnucleaires.secugerbisnucleaires

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.DatePicker
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase
import fr.isen.gerbisnucleaires.secugerbisnucleaires.recyclerview.Visit
import kotlinx.android.synthetic.main.activity_add_visit.*
import java.text.SimpleDateFormat
import java.util.*

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class AddVisitActivity : AppCompatActivity() {

    var cal = Calendar.getInstance()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_visit)

        val patientUuid = intent.getStringExtra("patientUuid")
        val patientTitle = intent.getStringExtra("patientTitle")
        val patientLastname = intent.getStringExtra("patientLastname")
        val patientFirstname = intent.getStringExtra("patientFirstname")
        val patientAge = intent.getStringExtra("patientAge")
        val patientDisease = intent.getStringExtra("patientDisease")

        addVisitTitle.text = "Add a Visit for $patientTitle $patientLastname $patientFirstname"

        dateButtonClick()
        cancelButtonClick(patientUuid, patientTitle, patientLastname, patientFirstname, patientAge, patientDisease)
        addVisitButtonClick(patientUuid, patientTitle, patientLastname, patientFirstname, patientAge, patientDisease)

    }
    fun cancelButtonClick(patientUuid : String, patientTitle : String, patientlastname : String, patientFirstname: String, patientAge : String, patientDisease : String) {
        addVisitCancelButton.setOnClickListener {
            val intent = Intent(this@AddVisitActivity, SpecificPatientActivity::class.java)
            intent.putExtra("uuid", patientUuid)
            intent.putExtra("title", patientTitle)
            intent.putExtra("first_name", patientFirstname)
            intent.putExtra("last_name", patientlastname)
            intent.putExtra("age", patientAge)
            intent.putExtra("disease", patientDisease)
            startActivity(intent)
            this.finish()
        }
    }

    fun addVisitButtonClick(patientUuid : String,  patientTitle : String, patientLastname : String, patientFirstname : String, patientAge: String, patientDisease: String){
       addVisitButton.setOnClickListener {
           val dateOfVisit = addVisitDateValue.text.toString()
           val temperature = addVisitTemperatureValue.text.toString()
           val treatmentGiven = addVisitTreatmentValue.text.toString()
           val patientState = addVisitPatientStateValue.text.toString()

           val firebase = FirebaseDatabase.getInstance()
           val ref = firebase.reference

           val visitId = ref.child("Visits").push().key.toString()

           val new_visit = Visit(visitId,patientUuid,temperature,treatmentGiven,patientState,dateOfVisit)

           Log.d("TEMPERATURE", "temp = " + temperature)

           if(dateOfVisit != "--/--/----" && treatmentGiven != "" && patientState != "" && temperature != "" && (temperature.toDouble() >= 30 && temperature.toDouble() <= 50) ){
               ref.child("Visits").child(visitId).setValue(new_visit)

               val intent = Intent(this@AddVisitActivity, SpecificPatientActivity::class.java)
               intent.putExtra("uuid", patientUuid)
               intent.putExtra("title", patientTitle)
               intent.putExtra("first_name", patientFirstname)
               intent.putExtra("last_name", patientLastname)
               intent.putExtra("age", patientAge)
               intent.putExtra("disease", patientDisease)
               startActivity(intent)

               Toast.makeText(this@AddVisitActivity, "The Visit for $patientTitle $patientFirstname $patientLastname have just been added to Firebase", Toast.LENGTH_LONG).show()

               this.finish()
           }
           else {
               if(dateOfVisit == "--/--/----" || treatmentGiven == "" || patientState == "") {
                   Toast.makeText(this@AddVisitActivity, "ERREUR : All field must be fill !!!", Toast.LENGTH_LONG).show()
               }
               else {
                   Toast.makeText(this@AddVisitActivity, "ERREUR : Temperature must be set between 30°C and 50°C !!! ", Toast.LENGTH_LONG).show()
               }
           }
        }
    }

    fun dateButtonClick() {
        val textview_date = this.addVisitDateValue

        textview_date?.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                DatePickerDialog(this@AddVisitActivity,
                    setCalendar(),
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)).show()
            }
        })
    }

    fun setCalendar () :  DatePickerDialog.OnDateSetListener {
        val dateSetListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int,
                                   dayOfMonth: Int) {
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateInView()
            }
        }
        return dateSetListener
    }

    fun updateDateInView() {
        val myFormat = "dd/MM/yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        addVisitDateValue?.text = sdf.format(cal.getTime())
    }
}
