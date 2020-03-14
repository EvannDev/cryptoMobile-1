package fr.isen.gerbisnucleaires.secugerbisnucleaires

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.scottyab.rootbeer.RootBeer
import fr.isen.gerbisnucleaires.secugerbisnucleaires.dataclass.SecuGerbis
import fr.isen.gerbisnucleaires.secugerbisnucleaires.recyclerview.PatientAdapter
import fr.isen.gerbisnucleaires.secugerbisnucleaires.recyclerview.patient.Patient
import kotlinx.android.synthetic.main.activity_patients_info.*


class PatientsInfoActivity : AppCompatActivity(), PatientAdapter.OnItemClickListener {
    override fun onItemClick(patient: Patient) {
        val intent = Intent(this@PatientsInfoActivity, SpecificPatientActivity::class.java)
        intent.putExtra("uuid", patient.uuid)
        intent.putExtra("title", patient.name.title)
        intent.putExtra("first_name", patient.name.firstName)
        intent.putExtra("last_name", patient.name.name)
        intent.putExtra("age", patient.age)
        intent.putExtra("disease", patient.disease)
        startActivity(intent)
    }

    val patients: ArrayList<Patient> = arrayListOf()
    private lateinit var mAuth: FirebaseAuth

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_patients_info)

        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("Patients")
        myRef.keepSynced(true)

        val postListener = object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (childSnapshot in dataSnapshot.children) {
                    val patient = childSnapshot.getValue(Patient::class.java)!!
                    patients.add(patient)

                    patientsRecycler.layoutManager = LinearLayoutManager(applicationContext)
                    patientsRecycler.adapter = PatientAdapter(patients, this@PatientsInfoActivity)
                }
            }

            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(applicationContext, "Can't read informations from Firebase", Toast.LENGTH_LONG)
                    .show()
            }
        }
        myRef.addValueEventListener(postListener)

        addPatientButtonClick()
        returnButton()
    }

    override fun onStart() {
        super.onStart()
        mAuth = FirebaseAuth.getInstance()
        checkIfAuth(mAuth)
        checkRootMethod()
        checkEmulator()
    }

    private fun addPatientButtonClick() {
        addPatientButton.setOnClickListener {
            val intent = Intent(applicationContext, AddPatientActivity::class.java)
            intent.putExtra("uuid", "")
            intent.putExtra("title", SecuGerbis("").encrypt())
            intent.putExtra("first_name", SecuGerbis("").encrypt())
            intent.putExtra("last_name", SecuGerbis("").encrypt())
            intent.putExtra("age", SecuGerbis("0").encrypt())
            intent.putExtra("disease", SecuGerbis("").encrypt())
            startActivity(intent)
        }
    }

    private fun returnButton() {
        addPatientReturnButton.setOnClickListener {
            val intent = Intent(applicationContext, HomeActivity::class.java)
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

    private fun checkRootMethod() {
        val rootBeer = RootBeer(applicationContext)
        if (rootBeer.isRooted) {
            Log.d("ROOT", "Device is ROOT")
            //System.exit(0)
        }
    }

    private fun checkEmulator() {
        if(isProbablyAnEmulator()){
            Log.d("EMULATOR", "Device is an Emulator")
            //System.exit(0)
        }
    }

    // To see if the app is running on an emulator device
    private fun isProbablyAnEmulator() = Build.FINGERPRINT.startsWith("generic")
            || Build.FINGERPRINT.startsWith("unknown")
            || Build.MODEL.contains("google_sdk")
            || Build.MODEL.contains("Emulator")
            || Build.MODEL.contains("Android SDK built for x86")
            || Build.BOARD == "QC_Reference_Phone" //bluestacks
            || Build.MANUFACTURER.contains("Genymotion")
            || Build.HOST.startsWith("Build") //MSI App Player
            || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
            || "google_sdk" == Build.PRODUCT
}
