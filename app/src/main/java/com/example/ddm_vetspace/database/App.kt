package com.example.ddm_vetspace.database

import android.app.Application

class App : Application() {

    lateinit var databaseHelper: DatabaseHelper
        private set

    override fun onCreate() {
        super.onCreate()
        // Inicializa o DatabaseHelper quando o aplicativo é criado
        databaseHelper = DatabaseHelper(this)
    }
}