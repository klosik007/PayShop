package com.pklos.payshop

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import org.w3c.dom.Text

class LoginActivity : AppCompatActivity() {
    private val TAG : String = "LoginActivity"
    private val REQUEST_SIGNUP : Int = 0

    private var _emailTxt : EditText? = null
    private var _passwordTxt : EditText? = null
    private var _loginBtn : Button? = null
    private var _signup_link : TextView? = null

    private val handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        _emailTxt = findViewById(R.id.email_txt)
        _passwordTxt =  findViewById(R.id.password_txt)
        _loginBtn = findViewById(R.id.login_btn)
        _signup_link = findViewById(R.id.signup_link)

        _loginBtn?.setOnClickListener{
            login()
        }

        _signup_link?.setOnClickListener {
            //sign up activity start
            val intent = Intent(applicationContext, SignupActivity::class.java)
            startActivityForResult(intent, REQUEST_SIGNUP)
        }
    }

    private fun login(){
        if (!validateLoginData()){
            onLoginFailed();
            return
        }

        _loginBtn?.isEnabled = false

        val progressDialog = AppUtils.setProgressDialog(this, "Authentication in progress...")
        progressDialog.show()

        val email : String = _emailTxt?.text.toString()
        val password : String = _passwordTxt?.text.toString()

        //authentication logic here
        if (authenticate(email, password)){
            handler.postDelayed({
                onLoginSuccess()
                progressDialog.dismiss()
            }, 3000)
        }
        else{
            handler.postDelayed({
                onLoginFailed()
                progressDialog.dismiss()
            }, 3000)
        }
    }

    private fun onLoginSuccess(){
        _loginBtn?.isEnabled = true
        finish()
    }

    private fun onLoginFailed(){
        Toast.makeText(baseContext, "Login Failed!", Toast.LENGTH_LONG).show()
        _loginBtn?.isEnabled = true
    }

    private fun validateLoginData(): Boolean {
        var valid = true

        val email : String = _emailTxt?.text.toString()
        val password : String = _passwordTxt?.text.toString()

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            _emailTxt?.error = "Enter Valid Email Address"
            valid = false
        } else{
            _emailTxt?.error = null
        }

        if (password.isEmpty() || password.length < 4){
            _passwordTxt?.error = "Password Length Below 4 Characters"
            valid = false
        } else{
            _emailTxt?.error = null
        }

        return valid
    }

    private fun authenticate(email : String, password : String) : Boolean{
        //simple hardcoded authentication, for test
//        if (email.equals("xxx@xxx.com") && password.equals("xxx")){
//            return true
//        }
//
        return false
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