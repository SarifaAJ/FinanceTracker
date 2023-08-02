package com.example.finance.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.finance.databinding.ActivitySettingBinding
import com.example.finance.ui.password.ChangePasswordActivity
import com.example.finance.ui.password.DisablePasswordActivity
import com.example.finance.ui.password.EnablePasswordActivity

class SettingActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // back button
        binding.btnBackArrow.setOnClickListener {
            finish()
        }

        // button to change password
        binding.changePassword.setOnClickListener {
            val moveIntent = Intent(this@SettingActivity, ChangePasswordActivity::class.java)
            startActivity(moveIntent)
        }

        // Initialize SharedPreferences
        sharedPreferences = applicationContext.getSharedPreferences("pref", Context.MODE_PRIVATE)

        // Check the initial state of the switch from SharedPreferences
        val switchState = sharedPreferences.getBoolean("switchState", false)
        binding.passwordSwitch.isChecked = switchState

        // Set a listener for the switch to update its state in SharedPreferences
        binding.passwordSwitch.setOnCheckedChangeListener { _, isChecked ->
            sharedPreferences.edit()
                .putBoolean("switchState", isChecked)
                .apply()

            if (isChecked) {
                // Switch is ON, go to LoginActivity
                val intent = Intent(this@SettingActivity, EnablePasswordActivity::class.java)
                startActivity(intent)
            } else {
                // Switch is OFF, go to SignupActivity
                val intent = Intent(this@SettingActivity, DisablePasswordActivity::class.java)
                startActivity(intent)
            }
        }
    }
}
