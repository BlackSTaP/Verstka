package com.example.storeapp

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.core.widget.addTextChangedListener
import androidx.appcompat.app.AppCompatActivity

class StoreActivity : AppCompatActivity() {
    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store)

        val listView = findViewById<ListView>(R.id.productListView)
        val searchEditText = findViewById<EditText>(R.id.searchEditText)
        val addProductButton = findViewById<Button>(R.id.addProductButton)

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1)
        listView.adapter = adapter
        refreshList()

        addProductButton.setOnClickListener {
            startActivity(Intent(this, AddProductActivity::class.java))
        }

        searchEditText.addTextChangedListener { text ->
            refreshList(text?.toString())
        }

        listView.setOnItemClickListener { _, _, position, _ ->
            val product = ProductRepository.getProducts()[position]
            if (product.stock > 0) {
                product.stock -= 1
                Toast.makeText(this, "Bought ${'$'}{product.name}", Toast.LENGTH_SHORT).show()
                refreshList(searchEditText.text.toString())
            } else {
                Toast.makeText(this, "Out of stock", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        refreshList(findViewById<EditText>(R.id.searchEditText).text.toString())
    }

    private fun refreshList(query: String? = null) {
        val names = ProductRepository.getProducts(query).map { p -> "${'$'}{p.name} - ${'$'}{p.price} (${ '$'}{p.stock})" }
        adapter.clear()
        adapter.addAll(names)
        adapter.notifyDataSetChanged()
    }
}
