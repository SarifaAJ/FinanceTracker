package com.example.finance.ui.login

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.example.finance.database.helper.DBHelper
import com.example.finance.databinding.ActivityLoginBinding
import com.example.finance.ui.HomeActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var db : DBHelper

    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = DBHelper(this)
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        // button to home
        binding.btnUser.setOnClickListener {
            val moveIntent = Intent(this@LoginActivity, HomeActivity::class.java)
            startActivity(moveIntent)
        }
        // button to sign up
        binding.confirmUser.setOnClickListener {
            val moveIntent = Intent(this@LoginActivity, SignupActivity::class.java)
            startActivity(moveIntent)
        }

        // button to confirm your username and password before you go to home activity
        binding.btnUser.setOnClickListener {
            val usernameText = binding.username.text.toString()
            val passwordText = binding.password.text.toString()

            if (TextUtils.isEmpty(usernameText) || TextUtils.isEmpty(passwordText)) {
                Toast.makeText(this, "Add Username and Password", Toast.LENGTH_SHORT).show()
            } else {
                val checkUser = db.checkUserPass(usernameText, passwordText)
                if (checkUser) {
                    // Simpan username ke SharedPreferences
                    val editor = sharedPreferences.edit()
                    editor.putString("username", usernameText)
                    editor.apply()

                    Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                    val intent = Intent(applicationContext, HomeActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Wrong Username and Password", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}