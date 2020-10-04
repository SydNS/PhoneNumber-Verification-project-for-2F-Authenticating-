package com.example.phoneverificationwithfirebase

import android.content.Intent
import android.icu.util.TimeUnit
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.example.phoneverificationwithfirebase.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.TaskExecutors
import com.google.firebase.FirebaseException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import javax.xml.datatype.DatatypeConstants.SECONDS


class VerifyPhoneActivity : AppCompatActivity() {

    private var verificationId: String? = null
    private var mAuth: FirebaseAuth? = null
    private var progressBar: ProgressBar? = null
    private var editText: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify_phone)
        mAuth = FirebaseAuth.getInstance()
        progressBar = findViewById(R.id.progressbar)
        editText = findViewById(R.id.editTextCode)
        val phonenumber = intent.getStringExtra("phonenumber")
        sendVerificationCode(phonenumber)
        findViewById<Button>(R.id.buttonSignIn).setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val code = editText?.getText().toString().trim { it <= ' ' }
                if (code.isEmpty() || code.length < 6) {
                    editText?.error = "Enter code..."
                    editText?.requestFocus()
                    return
                }
                verifyCode(code)
            }
        })
    }

    private fun verifyCode(code: String) {
        val credential: PhoneAuthCredential? =
            verificationId?.let { PhoneAuthProvider.getCredential(it, code) }
        if (credential != null) {
            signInWithCredential(credential)
        }
    }

    private fun signInWithCredential(credential: PhoneAuthCredential) {
        mAuth?.signInWithCredential(credential)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val intent = Intent(this@VerifyPhoneActivity, ProfileActivity::class.java)
                    intent.flags =
                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                } else {
                    Toast.makeText(
                        this@VerifyPhoneActivity,
                        task.exception?.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }

    private fun sendVerificationCode(number: String) {
        progressBar!!.visibility = View.VISIBLE
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            number,
            60,
            java.util.concurrent.TimeUnit.SECONDS,
            TaskExecutors.MAIN_THREAD,
            mCallBack
        )
    }

    private val mCallBack: PhoneAuthProvider.OnVerificationStateChangedCallbacks =
        object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
           override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                super.onCodeSent(p0, p1)
                verificationId = p0
            }

            override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                val code: String? = phoneAuthCredential.smsCode
                if (code != null) {
                    editText!!.setText(code)
                    verifyCode(code)
                }
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Toast.makeText(this@VerifyPhoneActivity, e.message, Toast.LENGTH_LONG).show()
            }
        }
}