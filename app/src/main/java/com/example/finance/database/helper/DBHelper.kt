package com.example.finance.database.helper

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.finance.database.dataclass.User

class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "UserManager.db"
        private const val TABLE_USERS = "users"
        private const val COLUMN_ID = "id"
        private const val COLUMN_USERNAME = "username"
        private const val COLUMN_PASSWORD = "password"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_USERS_TABLE = ("CREATE TABLE $TABLE_USERS (" +
                "$COLUMN_ID INTEGER PRIMARY KEY," +
                "$COLUMN_USERNAME TEXT," +
                "$COLUMN_PASSWORD TEXT)")
        db.execSQL(CREATE_USERS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        onCreate(db)
    }

    fun insertData(username: String, password: String): Boolean {
        val values = ContentValues()
        values.put(COLUMN_USERNAME, username)
        values.put(COLUMN_PASSWORD, password)

        val db = this.writableDatabase
        val query = "SELECT * FROM $TABLE_USERS WHERE $COLUMN_USERNAME = ?"
        val cursor = db.rawQuery(query, arrayOf(username))
        if (cursor.count <= 0) {
            db.insert(TABLE_USERS, null, values)
            cursor.close()
            return true
        }
        cursor.close()
        return false
    }

    // Inside DBHelper class
    fun insertUsername(username: String): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COLUMN_USERNAME, username)
        return db.insert(TABLE_USERS, null, contentValues)
    }


    fun getUser(username: String): User? {
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_USERS WHERE $COLUMN_USERNAME = ?"
        val cursor: Cursor = db.rawQuery(query, arrayOf(username))

        if (cursor.moveToFirst()) {
            val idIndex = cursor.getColumnIndex(COLUMN_ID)
            val usernameIndex = cursor.getColumnIndex(COLUMN_USERNAME)
            val passwordIndex = cursor.getColumnIndex(COLUMN_PASSWORD)

            if (idIndex != -1 && usernameIndex != -1 && passwordIndex != -1) {
                val id = cursor.getInt(idIndex)
                val password = cursor.getString(passwordIndex)

                cursor.close()
                return User(id, username, password)
            }
        }

        cursor.close()
        return null
    }

    // Function to check if the username exists in the database
    fun checkUsername(username: String): Boolean {
        val db = this.readableDatabase
        val cursor: Cursor? = db.query(
            TABLE_USERS,
            arrayOf(COLUMN_ID),
            "$COLUMN_USERNAME = ?",
            arrayOf(username),
            null,
            null,
            null
        )
        val usernameExists = cursor != null && cursor.count > 0
        cursor?.close()
        return usernameExists
    }

    fun checkUserPass(username: String, password: String): Boolean {
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_USERS WHERE $COLUMN_USERNAME = ? AND $COLUMN_PASSWORD = ?"
        val cursor: Cursor = db.rawQuery(query, arrayOf(username, password))
        val count = cursor.count
        cursor.close()
        return count > 0
    }

    fun updatePassword(username: String, newPassword: String): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_PASSWORD, newPassword)

        val rowsAffected = db.update(TABLE_USERS, values, "$COLUMN_USERNAME = ?", arrayOf(username))
        return rowsAffected > 0
    }
}
