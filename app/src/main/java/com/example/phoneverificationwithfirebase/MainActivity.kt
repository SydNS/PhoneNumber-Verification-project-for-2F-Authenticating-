package com.example.phoneverificationwithfirebase

import com.example.phoneverificationwithfirebase.R
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private var spinner: Spinner? = null
    private var editText: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        spinner = findViewById(R.id.spinnerCountries)
        spinner?.adapter = ArrayAdapter(this, R.layout.simple_spinner_dropdown_item,R.id.countryID, CountryData.countryNames)
        editText = findViewById(R.id.editTextPhone)
        buttonContinue.setOnClickListener{


//                val code: String? = spinner?.selectedItemPosition?.let { it1 -> CountryData.countryAreaCodes[it1] }
                val code: String? = "256"
                val number = editText?.text.toString().trim { it <= ' ' }
                if (number.isEmpty() || number.length < 10) {
                    editText?.error = "Valid number is required"
                    editText?.requestFocus()
                    Toast.makeText(this,"error",Toast.LENGTH_SHORT).show()
                }
                val phoneNumber = "+$code$number"
                val intent = Intent(this, VerifyPhoneActivity::class.java)
                intent.putExtra("phonenumber", phoneNumber)
                startActivity(intent)
            Toast.makeText(this,"this far",Toast.LENGTH_SHORT).show()


        }
    }

//    override fun onStart() {
//        super.onStart()
//        if (FirebaseAuth.getInstance().currentUser != null) {
//            val intent = Intent(this, ProfileActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//            startActivity(intent)
//        }
//    }
}