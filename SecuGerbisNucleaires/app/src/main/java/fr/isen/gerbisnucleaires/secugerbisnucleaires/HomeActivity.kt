package fr.isen.gerbisnucleaires.secugerbisnucleaires

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        mAuth = FirebaseAuth.getInstance()
        // Click on book icon
        personnalInfoButton.setOnClickListener {
            newIntent(this, PersonalInfoActivity::class.java)
        }

        // Click on patient icon
        patientsInfoButton.setOnClickListener {
            newIntent(this, PatientsInfoActivity::class.java)
        }

        textPatient.setOnClickListener {
            newIntent(this, PatientsInfoActivity::class.java)
        }

        textInfirmiers.setOnClickListener {
            newIntent(this, PersonalInfoActivity::class.java)
        }

        logoutHomeButton.setOnClickListener{
            mAuth.signOut()
            newIntent(this,LoginActivity::class.java)
        }
    }

    // Start new activity
    private fun newIntent(context: Context, clazz: Class<*>) {
        startActivity(Intent(context, clazz))
    }
}
