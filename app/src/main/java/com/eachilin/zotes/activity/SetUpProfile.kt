package com.eachilin.zotes.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.eachilin.zotes.databinding.ActivitySetUpProfileBinding
import com.google.firebase.auth.FirebaseAuth

private const val TAG = "SetUpProfile"
class SetUpProfile : AppCompatActivity() {
    private lateinit var binding:ActivitySetUpProfileBinding
    private lateinit var etEmail : EditText
    private lateinit var etPassword : EditText
    private lateinit var etConfirmPassword : EditText
    private lateinit var btnSubmit: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySetUpProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
    }

    private fun initView() {
        etEmail = binding.etEmail
        etPassword = binding.etPassword
        etConfirmPassword = binding.etConfirmPassword
        btnSubmit = binding.btnSignUp


        btnSubmit.setOnClickListener {

            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()
            val confirm = etConfirmPassword.text.toString().trim()

            if (email.isEmpty() || confirm.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Missing Infromation", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            btnSubmit.isEnabled = false

            if (password == confirm && email.contains("@")
            ) {
                checkForValidEmail(email,password)

//                if (isNewUser) {
//                    createUserAccount(email,password)
//                } else {
//                    Toast.makeText(
//                        this,
//                        "Email already exist, Please use a different email",
//                        Toast.LENGTH_LONG
//                    ).show()
//                }

            } else {
                btnSubmit.isEnabled = true
                Toast.makeText(this, "Invalid email or password do not match", Toast.LENGTH_LONG)
                    .show()
            }

        }
    }

    private fun goToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun createUserAccount(email: String, password: String) {


        val auth = FirebaseAuth.getInstance()
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                btnSubmit.isEnabled = true
                if (task.isSuccessful) {
                    Log.i(TAG, "created user Successfully")
                    goToMain()

                } else {
                    Log.e(TAG, "failed to create user", task.exception)
                    Toast.makeText(this, "Unable to create account", Toast.LENGTH_SHORT).show()
                }
            }


    }

    private fun  checkForValidEmail(email:String,password: String) :Boolean{
        val isValidUser = false
        val auth = FirebaseAuth.getInstance()
        auth.fetchSignInMethodsForEmail(email).addOnCompleteListener { task->
            val isNewUser:Boolean = task.result.signInMethods?.isEmpty() == true

            if(isNewUser){
                createUserAccount(email,password)
            }
        }

        return isValidUser

    }
}