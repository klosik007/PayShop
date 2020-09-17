package com.pklos.payshop

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.PersistableBundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

abstract class LoginRegisterActivity : AppCompatActivity() {

    abstract val TAG : String
    abstract val REQUEST_SIGNUP : Int

    abstract var _emailTxt : EditText
    abstract var _passwordTxt : EditText
    abstract var _actionBtn : Button
    abstract var _link : TextView
    abstract var _forgotPass_link : TextView

    val handler = Handler()

    lateinit var mAuth : FirebaseAuth

    abstract var layout : Int

    abstract var toastMsg : String
    abstract var cls : Class<AppCompatActivity>
    abstract var actionMsg : String

    lateinit var progressDialog : AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout)

        _emailTxt = findViewById(R.id.email_txt)
        _passwordTxt = findViewById(R.id.password_txt)
        _actionBtn = findViewById(R.id.signup_btn)
        _link = findViewById(R.id.login_link)
        _forgotPass_link = findViewById(R.id.forgotPass_link)

        _actionBtn.setOnClickListener{
            performAction()
        }

        _link.setOnClickListener {
            //finish signup activity -> login activity
            finish()
        }

        mAuth = FirebaseAuth.getInstance()
    }

    fun performAction(){
        if (!validateData()){
            onFailure()
            return
        }

        _actionBtn.isEnabled = false

        progressDialog = AppUtils.setProgressDialog(this, actionMsg)
        progressDialog.show()

        val email : String = _emailTxt.text.toString()
        val password : String = _passwordTxt.text.toString()

        //signup or login logic here
        doNetworkStuff(email, password)
    }

    abstract fun doNetworkStuff(email : String, password : String) //signup or login logic

    fun onSuccess(){
        _actionBtn.isEnabled = true
        val intent = Intent(this, cls)
        startActivity(intent)
        finish()
    }

    fun onFailure(){
        Toast.makeText(baseContext, toastMsg, Toast.LENGTH_LONG).show()
        _actionBtn.isEnabled = true
    }

    fun validateData() : Boolean{
        var valid = true

        val email : String = _emailTxt.text.toString()
        val password : String = _passwordTxt.text.toString()

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