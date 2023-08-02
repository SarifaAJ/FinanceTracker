package com.example.finance.ui.password

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.finance.database.helper.DBHelper
import com.example.finance.databinding.ActivityChangePasswordBinding
import com.example.finance.ui.login.LoginActivity

class ChangePasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChangePasswordBinding
    private lateinit var db: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = DBHelper(this)

        // back button
        binding.btnBackArrow.setOnClickListener {
            finish()
        }

        binding.btnSave.setOnClickListener {
            val usernameText = binding.username.text.toString()
            val oldPasswordText = binding.oldPassword.text.toString()
            val newPasswordText = binding.newPassword.text.toString()
            val confirmNewPassText = binding.confirmPass.text.toString()

            if (TextUtils.isEmpty(usernameText) || TextUtils.isEmpty(oldPasswordText) ||
                TextUtils.isEmpty(newPasswordText) || TextUtils.isEmpty(confirmNewPassText)
            ) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            } else {
                val user = db.getUser(usernameText)
                if (user == null) {
                    Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show()
                } else {
                    if (user.password == oldPasswordText) {
                        if (newPasswordText == confirmNewPassText) {
                            val updated = db.updatePassword(usernameText, newPasswordText)
                            if (updated) {

                                Toast.makeText(this, "Password changed successfully", Toast.LENGTH_SHORT).show()

                                val intent = Intent(this@ChangePasswordActivity, LoginActivity::class.java)
                                startActivity(intent)
                                finish()
                            } else {
                                Toast.makeText(this, "Password change failed", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(this, "New Passwords do not match", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this, "Old password is incorrect", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}
