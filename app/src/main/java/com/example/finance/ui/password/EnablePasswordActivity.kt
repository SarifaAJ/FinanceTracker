package com.example.finance.ui.password

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.example.finance.database.helper.DBHelper
import com.example.finance.databinding.ActivityEnablePasswordBinding
import com.example.finance.ui.SettingActivity
import com.example.finance.ui.login.LoginActivity

class EnablePasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEnablePasswordBinding
    private lateinit var db : DBHelper

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEnablePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = DBHelper(this)

        // Initialize SharedPreferences
        sharedPreferences = applicationContext.getSharedPreferences("pref", Context.MODE_PRIVATE)

        // button to home
        binding.btnSave.setOnClickListener {
            val moveIntent = Intent(this@EnablePasswordActivity, SettingActivity::class.java)
            startActivity(moveIntent)
        }

        // back button
        binding.btnBackArrow.setOnClickListener {
            // Set the switch state to OFF before navigating back to SettingActivity
            sharedPreferences.edit()
                .putBoolean("switchState", false)
                .apply()
            finish()
        }

        // button to login
        binding.confirmUser.setOnClickListener {
            val moveIntent = Intent(this@EnablePasswordActivity, LoginActivity::class.java)
            startActivity(moveIntent)
        }

        // button to input your data to database, so that you can use it for login to
        binding.btnSave.setOnClickListener {
            val usernameText = binding.username.text.toString()
            val passwordText = binding.password.text.toString()
            val confirmPassText = binding.confirmPass.text.toString()
            val savedData = db.insertData(usernameText, passwordText)

            if (TextUtils.isEmpty(usernameText) || TextUtils.isEmpty(passwordText) || TextUtils.isEmpty(confirmPassText)) {
                Toast.makeText(this, "Add Username, Email, Password & Confirm Password", Toast.LENGTH_SHORT).show()
            } else {
                if (passwordText == confirmPassText) {
                    if (savedData) {
                        Toast.makeText(this, "Password Enabled", Toast.LENGTH_SHORT).show()

                        // Sign up successful, set the switch state to true
                        sharedPreferences.edit()
                            .putBoolean("switchState", true)
                            .apply()

                        val intent = Intent(applicationContext, SettingActivity::class.java)
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
