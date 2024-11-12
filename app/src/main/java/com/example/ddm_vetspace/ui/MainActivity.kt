package com.example.ddm_vetspace.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.ddm_vetspace.R
import com.example.ddm_vetspace.database.App
import com.example.ddm_vetspace.database.background_service_blog
import com.example.ddm_vetspace.database.background_service_pet

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicializa o banco de dados ao iniciar a MainActivity
        val dbHelper = (application as App).databaseHelper
        dbHelper.readableDatabase

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val loginButton = findViewById<Button>(R.id.loginButton)
        loginButton.setOnClickListener {
            val blogWorkRequest = OneTimeWorkRequestBuilder<background_service_blog>().build()
            WorkManager.getInstance(this).enqueue(blogWorkRequest)

            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        val cadastroText = findViewById<TextView>(R.id.registerText)
        cadastroText.setOnClickListener {
            val intent = Intent(this, Cadastro::class.java)
            startActivity(intent)
        }

    }
}