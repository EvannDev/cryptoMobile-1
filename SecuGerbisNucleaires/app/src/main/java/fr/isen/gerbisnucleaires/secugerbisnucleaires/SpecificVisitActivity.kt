package fr.isen.gerbisnucleaires.secugerbisnucleaires

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.scottyab.rootbeer.RootBeer
import fr.isen.gerbisnucleaires.secugerbisnucleaires.dataclass.SecuGerbis
import fr.isen.gerbisnucleaires.secugerbisnucleaires.recyclerview.patient.Patient
import kotlinx.android.synthetic.main.activity_specific_visit.*


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class SpecificVisitActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_specific_visit)

        val uuid = intent.getStringExtra("uuid")
        val patientId = intent.getStringExtra("patientId")
        val temperature = SecuGerbis(intent.getStringExtra("temperature")).decrypt()
        val treatment = SecuGerbis(intent.getStringExtra("treatment")).decrypt()
        val patientState = SecuGerbis(intent.getStringExtra("patientState")).decrypt()
        val dateOfVisit = SecuGerbis(intent.getStringExtra("dateOfVisit")).decrypt()

        var patientTitle: String
        var patientFirstName: String
        var patientLastName: String
        var patientAge: String
        var patientDisease: String

        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("Patients")
        myRef.keepSynced(true)

        val patientListener = object : ValueEventListener {

            @SuppressLint("SetTextI18n")
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (childSnapshot in dataSnapshot.children) {
                    val patient = childSnapshot.getValue(Patient::class.java)!!
                    if (patient.uuid == patientId) {
                        specificVisitTitle.text =
                            "Visit of ${SecuGerbis(patient.name.title).decrypt()} ${SecuGerbis(patient.name.firstName).decrypt()} ${SecuGerbis(patient.name.name).decrypt()}"
                        patientTitle = SecuGerbis(patient.name.title).decrypt()
                        patientFirstName = SecuGerbis(patient.name.firstName).decrypt()
                        patientLastName = SecuGerbis(patient.name.name).decrypt()
                        patientAge = SecuGerbis(patient.age).decrypt()
                        patientDisease = SecuGerbis(patient.disease).decrypt()

                        cancelButton(patientId, patientTitle, patientFirstName, patientLastName, patientAge, patientDisease)
                        editVisitButton(
                            patientId,
                            patientTitle,
                            patientFirstName,
                            patientLastName,
                            patientAge,
                            patientDisease,
                            uuid,
                            dateOfVisit,
                            temperature,
                            treatment,
                            patientState
                        )
                        deleteVisit(uuid, patientId, patientTitle, patientFirstName, patientLastName, patientAge, patientDisease)
                    }
                }
            }

            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(applicationContext, "Can't read information from Firebase", Toast.LENGTH_LONG).show()
            }
        }
        myRef.addValueEventListener(patientListener)

        specificVisitDateValue.text = dateOfVisit
        specificVisitTemperatureValue.text = temperature
        specificVisitTreatmentValue.text = treatment
        specificVisitTreatmentValue.movementMethod = ScrollingMovementMethod()
        specificVisitPatientStateValue.text = patientState
        specificVisitPatientStateValue.movementMethod = ScrollingMovementMethod()
    }

    override fun onStart() {
        super.onStart()
        mAuth = FirebaseAuth.getInstance()
        checkIfAuth(mAuth)
        checkRootMethod()
        checkEmulator()
    }

    fun cancelButton(
        patientId: String,
        patientTitle: String,
        patientFirstName: String,
        patientLastName: String,
        patientAge: String,
        patientDisease: String
    ) {
        specificVisitCancelButton.setOnClickListener {
            val intent = Intent(this@SpecificVisitActivity, SpecificPatientActivity::class.java)
            intent.putExtra("uuid", patientId)
            intent.putExtra("title", SecuGerbis(patientTitle).encrypt())
            intent.putExtra("first_name", SecuGerbis(patientFirstName).encrypt())
            intent.putExtra("last_name", SecuGerbis(patientLastName).encrypt())
            intent.putExtra("age", SecuGerbis(patientAge).encrypt())
            intent.putExtra("disease", SecuGerbis(patientDisease).encrypt())
            startActivity(intent)
        }
    }

    fun deleteVisit(
        uuid: String,
        patientId: String,
        patientTitle: String,
        patientFirstName: String,
        patientLastName: String,
        patientAge: String,
        patientDisease: String
    ) {
        specificVisitDeleteButton.setOnClickListener {
            FirebaseDatabase.getInstance().getReference("Visits").child(uuid).removeValue()
            val intent = Intent(this@SpecificVisitActivity, SpecificPatientActivity::class.java)
            intent.putExtra("uuid", patientId)
            intent.putExtra("title", SecuGerbis(patientTitle).encrypt())
            intent.putExtra("first_name", SecuGerbis(patientFirstName).encrypt())
            intent.putExtra("last_name", SecuGerbis(patientLastName).encrypt())
            intent.putExtra("age", SecuGerbis(patientAge).encrypt())
            intent.putExtra("disease", SecuGerbis(patientDisease).encrypt())
            startActivity(intent)
        }
    }

    fun editVisitButton(
        patientId: String,
        patientTitle: String,
        patientFirstName: String,
        patientLastName: String,
        patientAge: String,
        patientDisease: String,
        uuid: String,
        dateOfVisit: String,
        temperature: String,
        treatment: String,
        patientState: String
    ) {
        specificVisitEditButton.setOnClickListener {
            val intent = Intent(this@SpecificVisitActivity, AddVisitActivity::class.java)
            intent.putExtra("patientUuid", patientId)
            intent.putExtra("patientTitle", SecuGerbis(patientTitle).encrypt())
            intent.putExtra("patientLastname", SecuGerbis(patientFirstName).encrypt())
            intent.putExtra("patientFirstname", SecuGerbis(patientLastName).encrypt())
            intent.putExtra("patientAge", SecuGerbis(patientAge).encrypt())
            intent.putExtra("patientDisease", SecuGerbis(patientDisease).encrypt())
            intent.putExtra("uuid", uuid)
            intent.putExtra("dateOfVisit", SecuGerbis(dateOfVisit).encrypt())
            intent.putExtra("temperature", SecuGerbis(temperature).encrypt())
            intent.putExtra("treatment", SecuGerbis(treatment).encrypt())
            intent.putExtra("patientState", SecuGerbis(patientState).encrypt())
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
