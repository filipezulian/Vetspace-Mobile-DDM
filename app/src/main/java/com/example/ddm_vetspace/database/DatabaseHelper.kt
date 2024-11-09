package com.example.ddm_vetspace.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DatabaseHelper(context : Context) : SQLiteOpenHelper(
    context, "vetspace.db", null, 1
) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE usuarios (user_id INTEGER PRIMARY KEY, nome TEXT, email TEXT, telefone TEXT, senha TEXT, permissao INTEGER)")
        db?.execSQL("CREATE TABLE pet (pet_id INTEGER PRIMARY KEY, tipo INTEGER, sexo INTEGER, nome TEXT, nascimento TEXT, user_id INTEGER)")
        db?.execSQL("CREATE TABLE blog (blog_id INTEGER PRIMARY KEY, descricao TEXT, user_id INTEGER, titulo TEXT, data TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        Log.i("info_db","executou onUpgrade")
        TODO("Not yet implemented")
    }

    companion object {

        // Instância única de DatabaseHelper
        @Volatile private var instance: DatabaseHelper? = null

        fun getInstance(context: Context): DatabaseHelper {
            return instance ?: synchronized(this) {
                instance ?: DatabaseHelper(context.applicationContext).also { instance = it }
            }
        }
    }
}