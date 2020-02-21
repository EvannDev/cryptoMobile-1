package fr.isen.gerbisnucleaires.secugerbisnucleaires

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.isen.gerbisnucleaires.secugerbisnucleaires.recyclerview.PatientAdapter
import fr.isen.gerbisnucleaires.secugerbisnucleaires.recyclerview.patient.Name
import fr.isen.gerbisnucleaires.secugerbisnucleaires.recyclerview.patient.Patient
import kotlinx.android.synthetic.main.activity_patients_info.*

class PatientsInfoActivity : AppCompatActivity(), PatientAdapter.OnItemClickListener {
    override fun onItemClick(patient: Patient) {
        val intent = Intent(this@PatientsInfoActivity, SpecificPatientActivity::class.java)
        intent.putExtra("title", patient.name.title)
        intent.putExtra("first_name", patient.name.firstName)
        intent.putExtra("last_name", patient.name.name)
        intent.putExtra("age", patient.age.toString())
        intent.putExtra("disease", patient.disease)
        startActivity(intent)
        //Toast.makeText(this, "Clicked: ${patient.name}", Toast.LENGTH_LONG).show()
    }

    private val patients: ArrayList<Patient> = arrayListOf(
        Patient(Name("Marsaut", "Mayeul", "Mr"), "Fou", 21),
        Patient(Name("Duee", "Allan", "Mr"), "Autorigole qsd hsd idsbcijds jsdijsddsij dsji ijs sijd jids jds ojds jd js ojdsodsosdjjds jds  sod dsj dsj sdoj dso ", 22),
        Patient(Name("Thomas", "Valentin", "Mme"), "Existe", 45),
        Patient(Name("Garcia Rota", "Lucas", "Mr"), "Monocouille et tri tétons",8) ,
        Patient(Name("Bilisari", "Elio", "Mr"), "Test",100),
        Patient(Name("De Bailliencourt", "Evann", "Mr"), "Test 1234 ",32)
    )

    private val lol = 0

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_patients_info)

        patientsRecycler.layoutManager = LinearLayoutManager(this)
        patientsRecycler.adapter = PatientAdapter(patients,this)
    }
}
