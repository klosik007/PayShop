package com.pklos.payshop.activities

import android.content.Intent
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.pklos.payshop.R


class SignupActivity : LoginRegisterActivity() {
    override val TAG : String = "SignupActivity"
    override val REQUEST_SIGNUP : Int = 0

    override lateinit var _emailTxt : EditText
    override lateinit var _passwordTxt : EditText
    override lateinit var _actionBtn : Button
    override lateinit var _link : TextView
    override lateinit var _forgotPass_link : TextView

    override var layout : Int = R.layout.activity_signup
    override var toastMsg : String = "Sign up failed"

    override var actionMsg = "Sign up in progress..."

    override var emailTxtId = R.id.email_txt
    override var passwordTxtId = R.id.password_txt
    override var actionBtnId = R.id.signup_btn
    override var forgotPassLinkId = R.id.empty_link
    override var loginOrSignUpLinkId = R.id.login_link

    override fun doNetworkStuff(email: String, password: String) {
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, OnCompleteListener {
                    task ->
                if(task.isSuccessful){
                    Toast.makeText(this, "Successfully Registered", Toast.LENGTH_LONG).show()
                    handler.postDelayed({
                        onSuccess()
                        progressDialog.dismiss()
                    }, 2000)
                }else {
                    Toast.makeText(this, "Registration Failed", Toast.LENGTH_LONG).show()
                    handler.postDelayed({
                        onFailure()
                        progressDialog.dismiss()
                    }, 2000)
                }
            })
    }

    override fun startActivityOnLinkView(){
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}