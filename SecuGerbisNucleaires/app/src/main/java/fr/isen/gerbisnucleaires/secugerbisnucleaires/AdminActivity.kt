package fr.isen.gerbisnucleaires.secugerbisnucleaires

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
import fr.isen.gerbisnucleaires.secugerbisnucleaires.dataclass.Nurse
import fr.isen.gerbisnucleaires.secugerbisnucleaires.dataclass.SecuGerbis
import fr.isen.gerbisnucleaires.secugerbisnucleaires.recyclerview.NurseAdapter
import kotlinx.android.synthetic.main.activity_admin.*
import kotlinx.android.synthetic.main.activity_nurse_item.*

class AdminActivity : AppCompatActivity(), NurseAdapter.OnItemClickListener {
    private lateinit var mAuth: FirebaseAuth

    override fun onItemClick(nurse: Nurse) {
        /*val intent = Intent(this@AdminActivity, SpecificPatientActivity::class.java)
        intent.putExtra("uuid", patient.uuid)
        intent.putExtra("title", patient.name.title)
        intent.putExtra("first_name", patient.name.firstName)
        intent.putExtra("last_name", patient.name.name)
        intent.putExtra("age", patient.age)
        intent.putExtra("disease", patient.disease)
        startActivity(intent)*/
        confirmNurseButton.setOnClickListener {
            Log.d("TEST", "Confirm Button ${SecuGerbis(nurse.email).decrypt()}")
        }
        rejectNurseButton.setOnClickListener {
            Log.d("TEST", "Reject Button ${SecuGerbis(nurse.email).decrypt()}")
        }
    }


    val nurses: ArrayList<Nurse> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        changeAdminCodeButtonClick()

        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("Nurse")
        myRef.keepSynced(true)

        val nurseListener = object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (childSnapshot in dataSnapshot.children) {
                    val nurse = childSnapshot.getValue(Nurse::class.java)!!
                    if (SecuGerbis(nurse.access).decrypt() == resources.getString(R.string.justSignedUp)) {
                        nurses.add(nurse)
                    }

                    newNurseRecycler.layoutManager = LinearLayoutManager(applicationContext)
                    newNurseRecycler.adapter = NurseAdapter(nurses, this@AdminActivity)
                }
            }

            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(applicationContext, "Can't read informations from Firebase", Toast.LENGTH_LONG)
                    .show()
            }
        }
        myRef.addValueEventListener(nurseListener)
    }

    private fun changeAdminCodeButtonClick() {
        changeAdminCodeButton.setOnClickListener {
            if(adminCodeValue.text.toString().isNotEmpty()){
                val ref =FirebaseDatabase.getInstance().getReference("Code_Admin")
                    ref.keepSynced(true)
                    ref.child("key").setValue(SecuGerbis(adminCodeValue.text.toString()).encrypt()).addOnCompleteListener {
                    Toast.makeText(applicationContext, "Code Admin have been changed", Toast.LENGTH_LONG).show()
                    adminCodeValue.setText("")
                }
            }
            else {
                Toast.makeText(applicationContext, "You must fill Admin Code Field to change it !!!", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        mAuth = FirebaseAuth.getInstance()
        checkIfAuth(mAuth)
        checkRootMethod()
        checkEmulator()
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

