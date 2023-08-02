package com.example.finance.ui.login

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.example.finance.database.helper.DBHelper
import com.example.finance.databinding.ActivityEnterBinding
import com.example.finance.ui.HomeActivity

class EnterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEnterBinding
    private lateinit var db: DBHelper
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEnterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = DBHelper(this)
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        // button to confirm your username and password before you go to home activity
        binding.btnEnter.setOnClickListener {
            val usernameText = binding.username.text.toString()

            if (TextUtils.isEmpty(usernameText)) {
                Toast.makeText(this, "Add Username", Toast.LENGTH_SHORT).show()
            } else {
                if (saveUsernameToDatabase(usernameText)) {
                    // Save username to SharedPreferences
                    val editor = sharedPreferences.edit()
                    editor.putString("username", usernameText)
                    editor.apply()

                    val moveIntent = Intent(applicationContext, HomeActivity::class.java)
                    startActivity(moveIntent)
                } else {
                    Toast.makeText(this, "Failed to save username", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun saveUsernameToDatabase(username: String): Boolean {
        return db.insertUsername(username) != -1L
    }
}