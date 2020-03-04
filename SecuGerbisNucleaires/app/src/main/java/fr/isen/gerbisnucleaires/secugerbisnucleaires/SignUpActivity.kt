package fr.isen.gerbisnucleaires.secugerbisnucleaires

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Patterns
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import fr.isen.gerbisnucleaires.secugerbisnucleaires.dataclass.Nurse
import kotlinx.android.synthetic.main.activity_sign_up.*
import java.nio.charset.Charset
import java.security.Key
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec


class SignUpActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES,"AndroidKeyStore")
        val kenGenParameterSpec = KeyGenParameterSpec.Builder("SecureGerbisKey",
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
            .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .build()

        keyGenerator.init(kenGenParameterSpec)
        keyGenerator.generateKey()

        val pair = chiffrement("ChiffreMoi!")
        val dechiffre = dechiffrement(pair.first,pair.second)
        Toast.makeText(this,dechiffre,Toast.LENGTH_LONG).show()

        mAuth = FirebaseAuth.getInstance()

        buttonsignup.setOnClickListener{
            registerUser()
        }
    }

    fun getKey() :SecretKey{
        val keystore = KeyStore.getInstance("AndroidKeyStore")
        keystore.load(null)

        val secretKeyEntry = keystore.getEntry("SecureGerbisKey", null) as KeyStore.SecretKeyEntry
        return secretKeyEntry.secretKey
    }

    fun chiffrement(data: String): Pair<ByteArray,ByteArray>{
        val cipher = Cipher.getInstance("AES/CBC/NoPadding")
        cipher.init(Cipher.ENCRYPT_MODE, getKey())
        val ivBytes = cipher.iv

        var tmp = data
        while(tmp.toByteArray().size % 16 != 0)
            tmp +="\u0020"

        val encryptedBytes = cipher.doFinal(tmp.toByteArray(Charsets.UTF_8))

        return Pair(ivBytes,encryptedBytes)
    }
    fun dechiffrement(ivBytes: ByteArray, data:ByteArray):String{
        val cipher = Cipher.getInstance("AES/CBC/NoPadding")
        val spec = IvParameterSpec(ivBytes)

        cipher.init(Cipher.DECRYPT_MODE, getKey())

        return cipher.doFinal(data).toString(Charsets.UTF_8).trim()
    }

    private fun registerUser() {
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("Code_Admin")

        val signUpListener = object : ValueEventListener {

            @RequiresApi(Build.VERSION_CODES.FROYO)
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (childSnapshot in dataSnapshot.children) {
                    checkField(childSnapshot.value.toString())
                }
            }

            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(this@SignUpActivity, "Can't read informations from Firebase", Toast.LENGTH_LONG).show()
            }
        }
        myRef.addValueEventListener(signUpListener)
    }

    @RequiresApi(Build.VERSION_CODES.FROYO)
    private fun checkField(adminCodeKey : String) {
        val email = emailSignUpEdit.text.toString()
        val password = passwordSignUpEdit.text.toString()
        val firstname = firstnameSignUpEdit.text.toString()
        val lastname = lastnameSignUpEdit.text.toString()
        val phone = phoneSignUpEdit.text.toString()
        val adminCode = codeAdminEdit.text.toString()

        if( email.isEmpty()     ||
            password.isEmpty()  ||
            firstname.isEmpty() ||
            lastname.isEmpty()  ||
            phone.isEmpty()     ||
            adminCode.isEmpty()){
            Toast.makeText(this,"Every Field must be fill ! ", Toast.LENGTH_LONG).show()
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(this,"Invalid Email ", Toast.LENGTH_LONG).show()
        }
        else if (password.length < 12){
            Toast.makeText(this,"Password should be longer than 12 characters ", Toast.LENGTH_LONG).show()
        }
        else if (!Patterns.PHONE.matcher(phone).matches()){
            Toast.makeText(this,"Invalid phone number ", Toast.LENGTH_LONG).show()
        }

        else if (!adminCode.equals(adminCodeKey)){
            Toast.makeText(this,"Invalid Admin Code ", Toast.LENGTH_LONG).show()
        }
        else{
            createAccount()
        }
    }

    private fun createAccount() {
        val email = emailSignUpEdit.text.toString()
        val password = passwordSignUpEdit.text.toString()
        val firstname = firstnameSignUpEdit.text.toString()
        val lastname = lastnameSignUpEdit.text.toString()
        val phone = phoneSignUpEdit.text.toString()

        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) {
                if (it.isSuccessful) {
                    Toast.makeText(this, "Registered", Toast.LENGTH_LONG).show()
                    sendEmailVerification()
                    fillRealTimeDatabase(firstname, lastname, email, phone,password)
                    goToLogin()
                    finish()
                } else {
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun fillRealTimeDatabase(firstname : String, lastname : String, email : String, phone : String, password: String){

        val nurseId =  mAuth.currentUser?.uid.toString()

        val nurse = Nurse(nurseId, firstname, lastname, phone, email,password)

        FirebaseDatabase.getInstance().getReference("Nurse").child(nurseId).setValue(nurse).addOnCompleteListener {
            Toast.makeText(this, "Registered", Toast.LENGTH_LONG).show()
        }
    }

    private fun sendEmailVerification(){
        val user = mAuth.currentUser
        user?.sendEmailVerification()
            ?.addOnCompleteListener(this) { task ->

                if (task.isSuccessful) {
                    Toast.makeText(baseContext,
                        "Verification email sent to ${user.email} ",
                        Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(baseContext,
                        "Failed to send verification email.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun goToLogin() {
        val homeIntent = Intent(
            this,
            LoginActivity::class.java
        )
        startActivity(homeIntent)
    }



}
