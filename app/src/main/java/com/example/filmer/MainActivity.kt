package com.example.filmer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val searchButton: Button = findViewById(R.id.search)
        val myListButton: Button = findViewById(R.id.my_list_button)
        searchButton.setOnClickListener {
            intent = Intent(this, SearchScreen::class.java)
            startActivity(intent)
        }
        myListButton.setOnClickListener {
            intent = Intent(this, MyListScreen::class.java)
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        // don't back to splash screen from main by press back button
    }
}