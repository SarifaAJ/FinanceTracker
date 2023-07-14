package com.example.finance.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.example.finance.database.helper.DBHelper
import com.example.finance.databinding.ActivitySignupBinding
import com.example.finance.ui.HomeActivity
import com.example.finance.ui.MainActivity

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private lateinit var db : DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = DBHelper(this)

        binding.btnUser.setOnClickListener {
            val moveIntent = Intent(this@SignupActivity, HomeActivity::class.java)
            startActivity(moveIntent)
        }
        binding.confirmUser.setOnClickListener {
            val moveIntent = Intent(this@SignupActivity, LoginActivity::class.java)
            startActivity(moveIntent)
        }
        binding.btnUser.setOnClickListener {
            val usernameText = binding.username.text.toString()
            val passwordText = binding.password.text.toString()
            val confirmPassText = binding.confirmPass.text.toString()
            val savedData = db.insertData(usernameText, passwordText)

            if (TextUtils.isEmpty(usernameText) || TextUtils.isEmpty(passwordText) || TextUtils.isEmpty(confirmPassText)) {
                Toast.makeText(this, "Add Username, Email, Password & Confirm Password", Toast.LENGTH_SHORT).show()
            } else {
                if (passwordText == confirmPassText) {
                    if (savedData) {
                        Toast.makeText(this, "Sign Up successful", Toast.LENGTH_SHORT).show()
                        val intent = Intent(applicationContext, HomeActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, "User exists", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Password not match", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}