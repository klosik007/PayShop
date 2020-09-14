package com.pklos.payshop

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class SignupActivity : AppCompatActivity() {
    private val TAG : String = "SignupActivity"

    private lateinit var _nameTxt : EditText
    private lateinit var _emailTxt : EditText
    private lateinit var _passwordTxt : EditText
    private lateinit var _signupBtn : Button
    private lateinit var _login_link : TextView

    private val handler = Handler()

    private lateinit var mAuth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        _nameTxt = findViewById(R.id.name_txt)
        _emailTxt = findViewById(R.id.email_txt)
        _passwordTxt = findViewById(R.id.password_txt)
        _signupBtn = findViewById(R.id.signup_btn)
        _login_link = findViewById(R.id.login_link)

        _signupBtn.setOnClickListener{
            signUp()
        }

        _login_link.setOnClickListener {
            //finish signup activity -> login activity
            finish()
        }

        mAuth = FirebaseAuth.getInstance()
    }

    private fun signUp(){
        if (!validateSignupData()){
            onSignupFailed();
            return
        }

        _signupBtn.isEnabled = false

        val progressDialog = AppUtils.setProgressDialog(this, "Creating account in progress...")
        progressDialog.show()

        //val name : String = _nameTxt?.text.toString()
        val email : String = _emailTxt.text.toString()
        val password : String = _passwordTxt.text.toString()

        //signup logic here
        mAuth.createUserWithEmailAndPassword(email, password)
             .addOnCompleteListener(this, OnCompleteListener {
                 task ->
                 if(task.isSuccessful){
                     Toast.makeText(this, "Successfully Registered", Toast.LENGTH_LONG).show()
                     handler.postDelayed({
                         onSignupSuccess()
                         progressDialog.dismiss()
                     }, 2000)
                 }else {
                     Toast.makeText(this, "Registration Failed", Toast.LENGTH_LONG).show()
                     handler.postDelayed({
                         onSignupFailed()
                         progressDialog.dismiss()
                     }, 2000)
                 }
             })
    }

    private fun onSignupSuccess(){
        _signupBtn.isEnabled = false
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun onSignupFailed(){
        Toast.makeText(baseContext, "Signup Failed! User Already Exists", Toast.LENGTH_LONG).show()
        _signupBtn.isEnabled = true
    }

    private fun validateSignupData(): Boolean {
        var valid = true

        /*val name : String = _nameTxt?.text.toString()*/
        val email : String = _emailTxt.text.toString()
        val password : String = _passwordTxt.text.toString()

        /*if (name.isEmpty() || name.length < 4){
            _nameTxt?.error = "Enter Valid Name"
            valid = false
        } else {
            _nameTxt?.error = null
        }*/

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            _emailTxt.error = "Enter Valid Email Address"
            valid = false
        } else {
            _emailTxt.error = null
        }

        if (password.isEmpty() || password.length < 4){
            _passwordTxt.error = "Password Length Below 4 Characters"
            valid = false
        } else{
            _passwordTxt.error = null
        }

        return valid
    }

    override fun onBackPressed() {
        moveTaskToBack(true)
    }
}