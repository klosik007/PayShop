package com.pklos.payshop

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : AppCompatActivity() {
    private val TAG : String = "ForgotPasswordActivity"
    private val REQUEST_SIGNUP : Int = 0

    private lateinit var _emailTxt : TextView
    private lateinit var _sendPassBtn : Button

    private lateinit var mAuth : FirebaseAuth

    private val handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        _emailTxt = findViewById(R.id.email_txt)
        _sendPassBtn = findViewById(R.id.sendpass_btn)

        _sendPassBtn.setOnClickListener{
            sendPassword()
        }

        mAuth = FirebaseAuth.getInstance()
    }

    private fun sendPassword(){
        if (!validateEmailData()){
            onSendPasswordFailed()
            return
        }

        _sendPassBtn.isEnabled = false

        val progressDialog = AppUtils.setProgressDialog(this, "Authentication in progress...")
        progressDialog.show()

        val email : String = _emailTxt.text.toString()

        //sending password logic

        mAuth.sendPasswordResetEmail(email)
            .addOnCompleteListener(this, OnCompleteListener {
                    task ->
                if(task.isSuccessful){
                    Log.d(TAG, "sendPasswordResetEmail: success")
                    Toast.makeText(this, "Reset link sent - check out your mailbox", Toast.LENGTH_LONG).show()
                    handler.postDelayed({
                        onSendPasswordSuccess()
                        progressDialog.dismiss()
                    }, 2000)
                }else {
                    Log.d(TAG, "sendPasswordResetEmail: failure")
                    Toast.makeText(this, "Reset link not sent: failure", Toast.LENGTH_LONG).show()
                    handler.postDelayed({
                        onSendPasswordFailed()
                        progressDialog.dismiss()
                    }, 2000)
                }
            })
    }

    private fun onSendPasswordSuccess(){
        _sendPassBtn.isEnabled = true
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun onSendPasswordFailed(){
        _sendPassBtn.isEnabled = true
    }

    private fun validateEmailData(): Boolean {
        var valid = true

        val email : String = _emailTxt.text.toString()

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            _emailTxt.error = "Enter Valid Email Address"
            valid = false
        } else{
            _emailTxt.error = null
        }

        return valid
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_SIGNUP){
            if (resultCode == RESULT_OK){
                finish()
            }
        }
    }
}