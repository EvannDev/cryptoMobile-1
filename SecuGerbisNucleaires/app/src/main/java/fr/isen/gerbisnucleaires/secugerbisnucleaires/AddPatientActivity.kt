package fr.isen.gerbisnucleaires.secugerbisnucleaires

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import fr.isen.gerbisnucleaires.secugerbisnucleaires.recyclerview.patient.Name
import fr.isen.gerbisnucleaires.secugerbisnucleaires.recyclerview.patient.Patient
import kotlinx.android.synthetic.main.activity_add_patient.*


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class AddPatientActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_patient)

        val uuid = intent.getStringExtra("uuid")
        val title = intent.getStringExtra("title")
        val firstName = intent.getStringExtra("first_name")
        val lastName = intent.getStringExtra("last_name")
        val age = intent.getStringExtra("age")
        val disease = intent.getStringExtra("disease")

        if (uuid == "" && firstName == "" && lastName == "" && age == "0" && disease == ""){
            addPatient()
        }
        else {
            addPatientAddButton.text = "Update Patient Information"
            updatePatientInfo(uuid, title, lastName, firstName, age, disease)
        }
        cancelClickButton()
    }

    override fun onStart() {
        super.onStart()
        mAuth = FirebaseAuth.getInstance()
        checkIfAuth(mAuth)
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
            val patientAge = addPatientAgeValue.text.toString().toInt()

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

    fun updatePatientInfo(uuid : String, title : String, lastName : String, firstName : String, age : String, disease : String) {
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
            val ref = FirebaseDatabase.getInstance().getReference("Patients")

            val patientName = addPatientLastnameFieldInput.text.toString()

            val patientFirstname = addPatientFirstnameFieldInput.text.toString()
            val patientTitle = addPatientTitleSpinner.selectedItem.toString()
            val patientDisease = addPatientDiseaseValue.text.toString()
            val patientAge = addPatientAgeValue.text.toString().toInt()



            if(patientName != "" && patientFirstname != "" && patientDisease != "" && (patientAge > 0 && patientAge <= 110)){
                val patient = Patient(uuid,Name(patientName,patientFirstname,patientTitle),patientDisease,patientAge)

                ref.child(uuid).setValue(patient).addOnCompleteListener {
                    Toast.makeText(applicationContext, "Changes saved", Toast.LENGTH_LONG).show()
                }

                val goToPatientsInfoActivity = Intent(this@AddPatientActivity, PatientsInfoActivity::class.java)
                startActivity(goToPatientsInfoActivity)

                Toast.makeText(this@AddPatientActivity, "The Patient Information have been Updated into Firebase", Toast.LENGTH_LONG).show()

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

    private fun checkIfAuth(mAuth : FirebaseAuth){
        if(mAuth.currentUser == null){
            newIntent(this@AddPatientActivity, LoginActivity::class.java)
        }
    }
    // Start new activity
    private fun newIntent(context: Context, clazz: Class<*>) {
        startActivity(Intent(context, clazz))
    }
}
