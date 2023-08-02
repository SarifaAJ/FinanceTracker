package com.example.finance.ui.password

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.example.finance.database.helper.DBHelper
import com.example.finance.databinding.ActivityDisablePasswordBinding
import com.example.finance.ui.HomeActivity
import com.example.finance.ui.SettingActivity

class DisablePasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDisablePasswordBinding
    private lateinit var db: DBHelper
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDisablePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = DBHelper(this)
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        // button to home
        binding.btnLogin.setOnClickListener {
            val moveIntent = Intent(this@DisablePasswordActivity, SettingActivity::class.java)
            startActivity(moveIntent)
        }

        // back button
        binding.btnBackArrow.setOnClickListener {
            // Set the switch state to ON before navigating back to SettingActivity
            sharedPreferences.edit()
                .putBoolean("switchState", true)
                .apply()
            finish()
        }

        // button to confirm your username and password before you go to home activity
        binding.btnLogin.setOnClickListener {
            val usernameText = binding.username.text.toString()
            val passwordText = binding.password.text.toString()

            if (TextUtils.isEmpty(usernameText) || TextUtils.isEmpty(passwordText)) {
                Toast.makeText(this, "Add Username and Password", Toast.LENGTH_SHORT).show()
            } else {
                val checkUser = db.checkUserPass(usernameText, passwordText)
                if (checkUser) {
                    // save username in SharedPreferences
                    val editor = sharedPreferences.edit()
                    editor.putString("username", usernameText)
                    editor.apply()

                    Toast.makeText(this, "Password Disabled", Toast.LENGTH_SHORT).show()

                    // Sign up successful, set the switch state to false
                    sharedPreferences.edit()
                        .putBoolean("switchState", false)
                        .apply()

                    val intent = Intent(applicationContext, HomeActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Wrong Username and Password", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}