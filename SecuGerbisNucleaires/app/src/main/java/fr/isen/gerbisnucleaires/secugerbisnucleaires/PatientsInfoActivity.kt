package fr.isen.gerbisnucleaires.secugerbisnucleaires

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import fr.isen.gerbisnucleaires.secugerbisnucleaires.recyclerview.PatientAdapter
import fr.isen.gerbisnucleaires.secugerbisnucleaires.recyclerview.patient.Name
import fr.isen.gerbisnucleaires.secugerbisnucleaires.recyclerview.patient.Patient
import kotlinx.android.synthetic.main.activity_patients_info.*

class PatientsInfoActivity : AppCompatActivity() {

    private val patients: ArrayList<Patient> = arrayListOf(
        Patient(Name("Marsaut", "Mayeul", "Mr"), "Fou", 21),
        Patient(Name("Duee", "Allan", "Mr"), "Autorigole", 22),
        Patient(Name("Thomas", "Valentin", "Mme"), "Existe", 45),
        Patient(Name("Garcia Rota", "Lucas", "Mr"), "Monocouille et tri tétons",8)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_patients_info)

        patientsRecycler.layoutManager = LinearLayoutManager(this)
        patientsRecycler.adapter = PatientAdapter(patients)
    }
}
