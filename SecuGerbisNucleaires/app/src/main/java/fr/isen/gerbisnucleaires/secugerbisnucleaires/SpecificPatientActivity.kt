package fr.isen.gerbisnucleaires.secugerbisnucleaires

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_specific_patient.*

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class SpecificPatientActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_specific_patient)

        val title = intent.getStringExtra("title")
        val firstName = intent.getStringExtra("first_name")
        val lastName = intent.getStringExtra("last_name")
        val age = intent.getStringExtra("age")
        val disease = intent.getStringExtra("disease")

        specificPatientName.text = ("$title $firstName $lastName")
        specificPatientAge.text = ("$age years old")
        specificPatientDisease.text = disease

        updatePatientInfo(title, lastName, firstName, age, disease)
    }

    fun updatePatientInfo(title : String, lastName : String, firstName : String, age : String, disease : String) {
        SpecificPatientEditButton.setOnClickListener {

            val intent = Intent(this@SpecificPatientActivity, AddPatientActivity::class.java)
            intent.putExtra("title", title)
            intent.putExtra("first_name", lastName)
            intent.putExtra("last_name",firstName)
            intent.putExtra("age", age)
            intent.putExtra("disease", disease)
            startActivity(intent)
        }
    }

}
