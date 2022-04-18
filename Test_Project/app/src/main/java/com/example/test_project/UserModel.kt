package com.example.test_project

import java.util.*

data class UserModel(
    var id: Int = getAutoId(),
    var username: String = "",
    var email: String = "",
    val password: String = ""

) {

    companion object {
        fun getAutoId(): Int {
            val random = Random()
            return random.nextInt(100)
        }
    }


}