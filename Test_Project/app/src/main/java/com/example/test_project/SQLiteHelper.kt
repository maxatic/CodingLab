package com.example.test_project

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.lang.Exception
import java.util.ArrayList


class SQLiteHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "users.db"
        private const val TBL_USERS = "tbl_users"
        private const val ID = "id"
        private const val USERNAME = "username"
        private const val EMAIL = "email"
        private const val PASSWORD = "password"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableUsers = ("CREATE TABLE " + TBL_USERS + "(" + ID + " INTEGER PRIMARY KEY,"
                + USERNAME + " TEXT," + PASSWORD + " TEXT," + EMAIL + " TEXT" + ")")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS TBL_USERS")

    }

    fun insertUser (user : UserModel) : Long{
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(ID, user.id)
        contentValues.put(EMAIL, user.email)
        contentValues.put(USERNAME, user.username)
        contentValues.put(PASSWORD, user.password)

        val success = db.insert(TBL_USERS, null, ContentValues())
        db.close()
        return success
    }

    @SuppressLint("Range")
    fun getAllUsers() : ArrayList<UserModel> {
        val usersList : ArrayList<UserModel> = ArrayList()
        val selectQuery = "SELECT * FROM $TBL_USERS"
        val db = this.readableDatabase

        val cursor : Cursor?

        try {
            cursor = db.rawQuery(selectQuery, null)

        }
        catch (e: Exception){
            e.printStackTrace()
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var id : Int
        var username : String
        var email : String
        var password : String

        if (cursor.moveToFirst()){
            do {
                id = cursor.getInt(cursor.getColumnIndex("id"))
                username = cursor.getString(cursor.getColumnIndex("username"))
                email = cursor.getString(cursor.getColumnIndex("email"))
                password = cursor.getString(cursor.getColumnIndex("password"))

                val user = UserModel(id=id, username=username, email=email, password=password)
                usersList.add(user)
            } while (cursor.moveToNext())
        }

        return usersList

    }
}