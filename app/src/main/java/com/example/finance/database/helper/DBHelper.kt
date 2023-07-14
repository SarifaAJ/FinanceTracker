package com.example.finance.database.helper

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) : SQLiteOpenHelper(context, "Userdata", null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE Userdata (username TEXT PRIMARY KEY, password TEXT, email TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS Userdata")
        onCreate(db)
    }

    fun insertData(username: String, password: String): Boolean {
        val db = this.writableDatabase
        val cv = ContentValues()

        cv.put("username", username)
        cv.put("password", password)

        val result = db.insert("Userdata", null, cv)
        return result != -1L
    }

    fun checkUserPass(username: String, password: String): Boolean {
        val db = this.writableDatabase
        val query = "SELECT * FROM Userdata WHERE username = '$username' AND password = '$password'"
        val cursor = db.rawQuery(query, null)

        val exists = cursor.count > 0
        cursor.close()
        return exists
    }
}
