package fr.isen.gerbisnucleaires.secugerbisnucleaires

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
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
import kotlinx.android.synthetic.main.activity_personal_item.*

class PersonalInfoActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personal_item)

        val user = FirebaseAuth.getInstance().currentUser

        val ref = FirebaseDatabase.getInstance().reference
        ref.keepSynced(true)
        ref
            .child("Nurse")
            .child(user!!.uid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    Toast.makeText(applicationContext, "Cannot read data from database", Toast.LENGTH_LONG).show()
                }

                override fun onDataChange(p0: DataSnapshot) {
                    val map = p0.value as Map<*, *>
                    val firstNameNurseDecode = SecuGerbis(map["firstName"].toString()).decrypt()
                    val lastNameNurseDecode = SecuGerbis(map["lastName"].toString()).decrypt()
                    val phoneNurseDecode = SecuGerbis(map["phone"].toString()).decrypt()
                    val emailNurseDecode = SecuGerbis(map["email"].toString()).decrypt()

                    firstnameNurse.text = firstNameNurseDecode
                    lastnameNurse.text = lastNameNurseDecode
                    phoneNurse.text = phoneNurseDecode
                    emailNurse.text = emailNurseDecode

                    editButtonClick(firstNameNurseDecode, lastNameNurseDecode, phoneNurseDecode, emailNurseDecode)
                }
            })


        homeButton.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        mAuth = FirebaseAuth.getInstance()
        checkIfAuth(mAuth)
        checkRootMethod()
        checkEmulator()
    }

    fun editButtonClick(firstName: String, lastName: String, phone: String, email: String) {
        buttonEdit.setOnClickListener {
            val intent = Intent(this, EditPersonalActivity::class.java)
            intent.putExtra("firstName", firstName)
            intent.putExtra("lastName", lastName)
            intent.putExtra("phone", phone)
            intent.putExtra("email", email)
            startActivity(intent)
            this.finish()
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

    override fun onBackPressed() {
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
