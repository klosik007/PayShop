package com.pklos.payshop

import android.app.ProgressDialog
import android.content.Context
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

class LoginActivity : LoginRegisterActivity() {
    override val TAG : String = "LoginActivity"
    override val REQUEST_SIGNUP : Int = 0

    override lateinit var _emailTxt : EditText
    override lateinit var _passwordTxt : EditText
    override lateinit var _actionBtn : Button
    override lateinit var _link : TextView
    override lateinit var _forgotPass_link : TextView

    override var layout : Int = R.layout.activity_login
    override var toastMsg : String = "Logged"

    override var actionMsg = "Login in progress..."

    override var emailTxtId = R.id.email_txt
    override var passwordTxtId = R.id.password_txt
    override var actionBtnId = R.id.login_btn
    override var forgotPassLinkId = R.id.forgotPass_link
    override var loginOrSignUpLinkId = R.id.signup_link

    override fun doNetworkStuff(email: String, password: String) {
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, OnCompleteListener {
                    task ->
                if(task.isSuccessful){
                    Log.d(TAG, "signInWithEmailAndPassword: success")
                    Toast.makeText(this, "Successfully Logged", Toast.LENGTH_LONG).show()
                    handler.postDelayed({
                        onSuccess()
                        progressDialog.dismiss()
                    }, 2000)
                }else {
                    Log.d(TAG, "signInWithEmailAndPassword: failure")
                    Toast.makeText(this, "Login Failed", Toast.LENGTH_LONG).show()
                    handler.postDelayed({
                        onFailure()
                        progressDialog.dismiss()
                    }, 2000)
                }
            })
    }

    override fun startActivityOnLinkView(){
        val intent = Intent(this, SignupActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onBackPressed() {
        moveTaskToBack(true)
    }
}