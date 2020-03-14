package fr.isen.gerbisnucleaires.secugerbisnucleaires

import android.content.Context
import android.content.Intent
import android.graphics.Paint
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
import fr.isen.gerbisnucleaires.secugerbisnucleaires.dataclass.Nurse
import fr.isen.gerbisnucleaires.secugerbisnucleaires.dataclass.SecuGerbis
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private var iTried = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mAuth = FirebaseAuth.getInstance()


        textbuttonsignin.paintFlags = textbuttonsignin.paintFlags or Paint.UNDERLINE_TEXT_FLAG

        buttonLogin.setOnClickListener {
            doLogin()
        }

        create.setOnClickListener {
            tryIt()
        }

        textbuttonsignin.setOnClickListener {
            newIntent(applicationContext, SignUpActivity::class.java)
        }
    }

    override fun onRestart() {
        super.onRestart()
        iTried = 0
    }

    override fun onStart() {
        super.onStart()
        checkRootMethod()
        checkEmulator()
    }

    override fun onResume() {
        super.onResume()
        iTried = 0
    }

    private fun tryIt() {
        val email = userEdit.text.toString()
        val password = passwordEdit.text.toString()

        if (email.isNotEmpty() || password.isNotEmpty()) {
            if (iTried > 8) {
                probablyNotEnough(email, password)
            } else
                iTried++
        }
    }

    private fun probablyNotEnough(email: String, password: String) {
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    if (mAuth.currentUser?.isEmailVerified!!) {
                        val dbr = FirebaseDatabase.getInstance().getReference("Nurse")
                        dbr.keepSynced(true)
                        val listener = object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                for (child in dataSnapshot.children) {
                                    val a = child.getValue(Nurse::class.java)!!
                                    if (a.id == mAuth.currentUser!!.uid) {
                                        if (SecuGerbis(a.access).decrypt() == resources.getString(R.string.isVerySuccessful)) {
                                            newIntent(applicationContext, AdminActivity::class.java)
                                        } else {
                                            iTried = 0
                                            break
                                        }
                                    }
                                }
                            }

                            override fun onCancelled(dataSnapshot: DatabaseError) {
                                iTried = 0
                            }
                        }

                        dbr.addListenerForSingleValueEvent(listener)
                    } else {
                        iTried = 0
                        mAuth.signOut()
                    }
                } else {
                    iTried = 0
                }
            }
        mAuth.signOut()
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

    private fun doLogout() {
        mAuth.signOut()
        Toast.makeText(applicationContext, "Disconnected", Toast.LENGTH_LONG).show()
    }

    private fun doLogin() {
        val email = userEdit.text.toString()
        val password = passwordEdit.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(applicationContext, "You should fill everything", Toast.LENGTH_SHORT).show()
        } else {
            mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        if (mAuth.currentUser?.isEmailVerified!!) {
                            Toast.makeText(applicationContext, "Welcome !", Toast.LENGTH_SHORT).show()
                            newIntent(applicationContext, HomeActivity::class.java)
                        } else {
                            Toast.makeText(applicationContext, "Email must be verified", Toast.LENGTH_SHORT).show()
                            doLogout()
                        }
                    } else {
                        Toast.makeText(applicationContext, "Authentication Failed", Toast.LENGTH_LONG).show()
                    }
                }
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
}