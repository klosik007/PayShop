package com.pklos.payshop

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class SignupActivity : AppCompatActivity() {
    private val TAG : String = "SignupActivity"

    private var _nameTxt : EditText? = null
    private var _emailTxt : EditText? = null
    private var _passwordTxt : EditText? = null
    private var _signupBtn : Button? = null
    private var _login_link : TextView? = null

    private val handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        _nameTxt = findViewById(R.id.name_txt)
        _emailTxt = findViewById(R.id.email_txt)
        _passwordTxt = findViewById(R.id.password_txt)
        _signupBtn = findViewById(R.id.signup_btn)
        _login_link = findViewById(R.id.login_link)

        _signupBtn?.setOnClickListener{
            signUp()
        }

        _login_link?.setOnClickListener {
            //finish signup activity -> login activity
            finish()
        }
    }

    private fun signUp(){
        if (!validateSignupData()){
            onSignupFailed();
            return
        }

        _signupBtn?.isEnabled = false

        val progressDialog = AppUtils.setProgressDialog(this, "Creating account in progress...")
        progressDialog.show()

        val name : String = _nameTxt?.text.toString()
        val email : String = _emailTxt?.text.toString()
        val password : String = _passwordTxt?.text.toString()

        //signup logic here
        if (checkIfUserExists(name, email, password)){
            handler.postDelayed({
                onSignupFailed()
                progressDialog.dismiss()
            }, 3000)
        }
        else{
            handler.postDelayed({
                onSignupSuccess()
                progressDialog.dismiss()
            }, 3000)
        }
    }

    private fun onSignupSuccess(){
        _signupBtn?.isEnabled = true
        finish()
    }

    private fun onSignupFailed(){
        Toast.makeText(baseContext, "Signup Failed! User Already Exists", Toast.LENGTH_LONG).show()
        _signupBtn?.isEnabled = true
    }

    private fun validateSignupData(): Boolean {
        var valid = true

        val name : String = _nameTxt?.text.toString()
        val email : String = _emailTxt?.text.toString()
        val password : String = _passwordTxt?.text.toString()

        if (name.isEmpty() || name.length < 4){
            _nameTxt?.error = "Enter Valid Name"
            valid = false
        } else {
            _nameTxt?.error = null
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            _emailTxt?.error = "Enter Valid Email Address"
            valid = false
        } else {
            _emailTxt?.error = null
        }

        if (password.isEmpty() || password.length < 4){
            _passwordTxt?.error = "Password Length Below 4 Characters"
            valid = false
        } else{
            _passwordTxt?.error = null
        }

        return valid
    }

    private fun checkIfUserExists(name: String, email: String, password: String) : Boolean{
        //simple hardcoded authentication, for test
        if (name.equals("Przemo") && email.equals("xxx@xxx.com") && password.equals("xxx")){
            return true
        }

        return false
    }

    override fun onBackPressed() {
        moveTaskToBack(true)
    }
}