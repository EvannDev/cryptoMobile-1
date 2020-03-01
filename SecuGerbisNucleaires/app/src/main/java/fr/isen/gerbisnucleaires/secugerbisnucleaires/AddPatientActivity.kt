package fr.isen.gerbisnucleaires.secugerbisnucleaires

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import fr.isen.gerbisnucleaires.secugerbisnucleaires.recyclerview.patient.Name
import fr.isen.gerbisnucleaires.secugerbisnucleaires.recyclerview.patient.Patient
import kotlinx.android.synthetic.main.activity_add_patient.*


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class AddPatientActivity : AppCompatActivity() {

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_patient)

        val title = intent.getStringExtra("title")
        val firstName = intent.getStringExtra("first_name")
        val lastName = intent.getStringExtra("last_name")
        val age = intent.getStringExtra("age")
        val disease = intent.getStringExtra("disease")

        if (firstName == "" && lastName == "" && age == "0" && disease == ""){
            addPatient()
        }
        else {
            addPatientAddButton.text = "Update Patient Information"
            updatePatientInfo(title, lastName, firstName, age, disease)
        }
        cancelClickButton()
    }

    fun cancelClickButton() {
        addPatientCancelButton.setOnClickListener {
            this.finish()
        }
    }

    fun addPatient(){
        addPatientAddButton.setOnClickListener {
            val firebase = FirebaseDatabase.getInstance()
            val ref = firebase.reference

            val patientId = ref.child("Patients").push().key.toString()
            val patientName = addPatientLastnameFieldInput.text.toString()

            val patientFirstname = addPatientFirstnameFieldInput.text.toString()
            val patientTitle = addPatientTitleSpinner.selectedItem.toString()
            val patientDisease = addPatientDiseaseValue.text.toString()
            var patientAge = addPatientAgeValue.text.toString().toInt()

            val patient = Patient(patientId, Name(patientName,patientFirstname,patientTitle),patientDisease,patientAge)

            if(patientName != "" && patientFirstname != "" && patientDisease != "" && (patientAge > 0 && patientAge <= 110)){
                ref.child("Patients").child(patientId).setValue(patient)

                val goToPatientsInfoActivity = Intent(this@AddPatientActivity, PatientsInfoActivity::class.java)
                startActivity(goToPatientsInfoActivity)

                Toast.makeText(this@AddPatientActivity, "The Patient have just been added to Firebase", Toast.LENGTH_LONG).show()

                this.finish()

            }
            else {
                if(patientName == "" || patientFirstname == "" || patientDisease == "") {
                    Toast.makeText(this@AddPatientActivity, "ERREUR : All the field must be fill !!!", Toast.LENGTH_LONG).show()
                }
                else {
                    Toast.makeText(this@AddPatientActivity, "ERREUR : Age must be set between 1 and 110 years old !!! ", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    fun updatePatientInfo(title : String, lastName : String, firstName : String, age : String, disease : String) {
        when(title) {
            "Sir" -> addPatientTitleSpinner.setSelection(0)
            "Mr" -> addPatientTitleSpinner.setSelection(1)
            "Mrs" -> addPatientTitleSpinner.setSelection(2)
            "Madam" -> addPatientTitleSpinner.setSelection(3)
            "Ms" -> addPatientTitleSpinner.setSelection(4)
            "Miss" -> addPatientTitleSpinner.setSelection(5)
            "Dr" -> addPatientTitleSpinner.setSelection(6)
            "Professor" -> addPatientTitleSpinner.setSelection(7)
        }

        addPatientLastnameFieldInput.setText(lastName)
        addPatientFirstnameFieldInput.setText(firstName)
        addPatientAgeValue.setText(age)
        addPatientDiseaseValue.setText(disease)

        addPatientAddButton.setOnClickListener {

        }
    }
}