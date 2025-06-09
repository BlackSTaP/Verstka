package com.example.storeapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val usernameEditText = findViewById<EditText>(R.id.usernameEditText)
        val passwordEditText = findViewById<EditText>(R.id.passwordEditText)
        val loginButton = findViewById<Button>(R.id.loginButton)

        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()
            saveCredentials(username, password)
            startActivity(Intent(this, StoreActivity::class.java))
        }
    }

    private fun saveCredentials(username: String, password: String) {
        val prefs = getSharedPreferences("auth", Context.MODE_PRIVATE)
        prefs.edit().putString("username", username).putString("password", password).apply()
    }
}
