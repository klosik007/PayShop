package com.pklos.payshop

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import org.w3c.dom.Text

class LoginActivity : AppCompatActivity() {
    private val TAG : String = "LoginActivity"
    private val REQUEST_SIGNUP : Int = 0

    private lateinit var _emailTxt : EditText
    private lateinit var _passwordTxt : EditText
    private lateinit var _loginBtn : Button
    private lateinit var _signup_link : TextView
    private lateinit var _forgotPass_link : TextView

    private val handler = Handler()

    private lateinit var mAuth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        _emailTxt = findViewById(R.id.email_txt)
        _passwordTxt =  findViewById(R.id.password_txt)
        _loginBtn = findViewById(R.id.login_btn)
        _signup_link = findViewById(R.id.signup_link)
        _forgotPass_link = findViewById(R.id.forgotPass_link)

        _loginBtn.setOnClickListener{
            login()
        }

        _signup_link.setOnClickListener {
            val intent = Intent(applicationContext, SignupActivity::class.java)
            startActivityForResult(intent, REQUEST_SIGNUP)
        }

        _forgotPass_link.setOnClickListener{
            val intent = Intent(applicationContext, ForgotPasswordActivity::class.java)
            startActivityForResult(intent, REQUEST_SIGNUP)
        }

        mAuth = FirebaseAuth.getInstance()
    }

    private fun login(){
        if (!validateLoginData()){
            onLoginFailed();
            return
        }

        _loginBtn.isEnabled = false

        val progressDialog = AppUtils.setProgressDialog(this, "Authentication in progress...")
        progressDialog.show()

        val email : String = _emailTxt.text.toString()
        val password : String = _passwordTxt.text.toString()

        //authentication logic here

        mAuth.signInWithEmailAndPassword(email, password)
             .addOnCompleteListener(this, OnCompleteListener {
                 task ->
                 if(task.isSuccessful){
                     Log.d(TAG, "signInWithEmailAndPassword: success")
                     Toast.makeText(this, "Successfully Logged", Toast.LENGTH_LONG).show()
                     handler.postDelayed({
                         onLoginSuccess()
                         progressDialog.dismiss()
                     }, 2000)
                 }else {
                     Log.d(TAG, "signInWithEmailAndPassword: failure")
                     Toast.makeText(this, "Login Failed", Toast.LENGTH_LONG).show()
                     handler.postDelayed({
                         onLoginFailed()
                         progressDialog.dismiss()
                     }, 2000)
                 }
             })
    }

    private fun onLoginSuccess(){
        _loginBtn.isEnabled = true
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun onLoginFailed(){
        _loginBtn.isEnabled = true
    }

    private fun validateLoginData(): Boolean {
        var valid = true

        val email : String = _emailTxt.text.toString()
        val password : String = _passwordTxt.text.toString()

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            _emailTxt.error = "Enter Valid Email Address"
            valid = false
        } else{
            _emailTxt.error = null
        }

        if (password.isEmpty() || password.length < 4){
            _passwordTxt.error = "Password Length Below 4 Characters"
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

    override fun onBackPressed() {
        moveTaskToBack(true)
    }
}