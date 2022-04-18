package com.example.test_project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Button
import android.widget.Toast

class Authorization : AppCompatActivity() {
    private lateinit var edUsername : EditText
    private lateinit var edPassword : EditText
    private lateinit var btnAddUser : Button
    private lateinit var btnEnterAuth : Button

    private lateinit var sqLiteHelper: SQLiteHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        initView()
        sqLiteHelper = SQLiteHelper(this)

        btnAddUser.setOnClickListener{addUser()}
        btnEnterAuth.setOnClickListener{getUsers()}

    }

    private fun getUsers() {
        val usersList = sqLiteHelper.getAllUsers()
        Log.e("pppp" , "${usersList.size}")
    }

    private fun addUser(){
        val username = edUsername.text.toString()
        val password = edPassword.text.toString()

        if(username.isEmpty() || password.isEmpty()){
            Toast.makeText(this, "Please, enter a required field", Toast.LENGTH_LONG).show()
        }
        else {
            val user = UserModel(username = username, password = password, email = "")
            val status = sqLiteHelper.insertUser(user)

            if (status > -1){
                Toast.makeText(this, "User added...", Toast.LENGTH_LONG).show()
                clearEditText()

            }
            else{
                Toast.makeText(this, "Record not saved", Toast.LENGTH_LONG).show()

            }
        }
    }

    private fun clearEditText() {
        edUsername.setText("")
        edPassword.setText("")
        edUsername.requestFocus()
    }


    private fun initView () {
        edUsername = findViewById(R.id.username_input)
        edPassword = findViewById(R.id.password_input)
        btnAddUser = findViewById(R.id.btn_enter_user)
        btnEnterAuth = findViewById(R.id.btn_google)
    }
}