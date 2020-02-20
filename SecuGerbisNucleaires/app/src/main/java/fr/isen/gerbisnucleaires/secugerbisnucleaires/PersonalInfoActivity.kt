package fr.isen.gerbisnucleaires.secugerbisnucleaires

import android.content.ClipData.newIntent
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_personal_item.*

class PersonalInfoActivity : AppCompatActivity() {

    private val nurses: ArrayList<Nurse> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personal_item)

        firstnameNurse.text = nurses[0].firstname
        lastnameNurse.text = nurses[0].lastname
        phoneNurse.text = nurses[0].phoneNumber
        emailNurse.text = nurses[0].email

        buttonEdit.setOnClickListener {
            newIntent(this, EditPersonalActivity::class.java)
        }
    }

    private fun newIntent(context: Context, clazz: Class<*>) {
        startActivity(Intent(context, clazz))
    }
}
