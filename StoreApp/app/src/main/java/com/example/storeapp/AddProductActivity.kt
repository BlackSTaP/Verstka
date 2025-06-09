package com.example.storeapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AddProductActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)

        val nameEditText = findViewById<EditText>(R.id.nameEditText)
        val priceEditText = findViewById<EditText>(R.id.priceEditText)
        val stockEditText = findViewById<EditText>(R.id.stockEditText)
        val saveButton = findViewById<Button>(R.id.saveButton)

        saveButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val price = priceEditText.text.toString().toDoubleOrNull() ?: -1.0
            val stock = stockEditText.text.toString().toIntOrNull() ?: -1
            if (name.isBlank() || price <= 0 || stock < 0) {
                Toast.makeText(this, "Invalid data", Toast.LENGTH_SHORT).show()
            } else {
                ProductRepository.addProduct(Product(name, price, stock))
                finish()
            }
        }
    }
}
