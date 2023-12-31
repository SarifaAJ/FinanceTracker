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
import com.example.finance.ui.password.ForgetPasswordActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var db: DBHelper
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = DBHelper(this)
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        // button to sign up
        binding.forgetPass.setOnClickListener {
            val moveIntent = Intent(this@LoginActivity, ForgetPasswordActivity::class.java)
            startActivity(moveIntent)
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
