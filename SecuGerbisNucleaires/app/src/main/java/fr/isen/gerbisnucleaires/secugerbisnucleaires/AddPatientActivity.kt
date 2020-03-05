package fr.isen.gerbisnucleaires.secugerbisnucleaires

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import fr.isen.gerbisnucleaires.secugerbisnucleaires.dataclass.SecuGerbis
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
        val title = SecuGerbis(intent.getStringExtra("title")).decrypt()
        val firstName = SecuGerbis(intent.getStringExtra("first_name")).decrypt()
        val lastName = SecuGerbis(intent.getStringExtra("last_name")).decrypt()
        val age = SecuGerbis(intent.getStringExtra("age")).decrypt()
        val disease = SecuGerbis(intent.getStringExtra("disease")).decrypt()

        if (uuid == "" && firstName == "" && lastName == "" && age == "0" && disease == "") {
            addPatient()
        } else {
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

    private fun cancelClickButton() {
        addPatientCancelButton.setOnClickListener {
            this.finish()
        }
    }

    private fun addPatient() {
        addPatientAddButton.setOnClickListener {
            val firebase = FirebaseDatabase.getInstance()
            val ref = firebase.reference

            val patientId = ref.child("Patients").push().key.toString()
            val patientName = addPatientLastnameFieldInput.text.toString()

            val patientFirstName = addPatientFirstnameFieldInput.text.toString()
            val patientTitle = addPatientTitleSpinner.selectedItem.toString()
            val patientDisease = addPatientDiseaseValue.text.toString()
            val patientAge = addPatientAgeValue.text.toString()

            val patient = Patient(patientId, Name(SecuGerbis(patientName).encrypt(),
                SecuGerbis(patientFirstName).encrypt(),
                SecuGerbis(patientTitle).encrypt()),
                SecuGerbis(patientDisease).encrypt(),
                SecuGerbis(patientAge).encrypt())

            if (patientName != "" && patientFirstName != "" && patientDisease != "" && (patientAge.toInt() in 1..110)) {
                ref.child("Patients").child(patientId).setValue(patient)

                val goToPatientsInfoActivity = Intent(this@AddPatientActivity, PatientsInfoActivity::class.java)
                startActivity(goToPatientsInfoActivity)

                Toast.makeText(applicationContext, "The Patient have just been added to Firebase", Toast.LENGTH_LONG).show()

                this.finish()

            } else {
                if (patientName == "" || patientFirstName == "" || patientDisease == "") {
                    Toast.makeText(applicationContext, "ERROR : All the field must be fill !!!", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(applicationContext, "ERROR : Age must be set between 1 and 110 years old !!! ", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun updatePatientInfo(uuid: String, title: String, lastName: String, firstName: String, age: String, disease: String) {
        when (title) {
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

            val patientFirstName = addPatientFirstnameFieldInput.text.toString()
            val patientTitle = addPatientTitleSpinner.selectedItem.toString()
            val patientDisease = addPatientDiseaseValue.text.toString()
            val patientAge = addPatientAgeValue.text.toString()



            if (patientName != "" && patientFirstName != "" && patientDisease != "" && (patientAge.toInt() in 1..110)) {
                val patient = Patient(uuid, Name(SecuGerbis(patientName).encrypt(),
                    SecuGerbis(patientFirstName).encrypt(),
                    SecuGerbis(patientTitle).encrypt()),
                    SecuGerbis(patientDisease).encrypt(),
                    SecuGerbis(patientAge).encrypt())

                ref.child(uuid).setValue(patient).addOnCompleteListener {
                    Toast.makeText(applicationContext, "Changes saved", Toast.LENGTH_LONG).show()
                }

                val goToPatientsInfoActivity = Intent(this@AddPatientActivity, PatientsInfoActivity::class.java)
                startActivity(goToPatientsInfoActivity)

                Toast.makeText(applicationContext, "The Patient Information have been Updated into Firebase", Toast.LENGTH_LONG).show()

                this.finish()

            } else {
                if (patientName == "" || patientFirstName == "" || patientDisease == "") {
                    Toast.makeText(applicationContext, "ERROR : All the field must be fill !!!", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(applicationContext, "ERROR : Age must be set between 1 and 110 years old !!! ", Toast.LENGTH_LONG).show()
                }
            }
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
