package com.embeltech.meterreading.utils

import java.util.regex.Pattern

class ValidatorUtils {
    var email: String? = null
    var mobile: String? = null

    companion object {
        fun validateMobile(mobile: String): Boolean {
            if (mobile.isEmpty())
                return false

            val regex = "\\d+"
            if (!mobile.matches(regex.toRegex())) {
                return false
            }

            if (mobile.length != 10) {
                return false
            }

            return true
        }
    }

    fun validateEmail(email: String): Boolean {
        var pattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

        if (email.isEmpty())
            return false

        if (!email.matches(pattern.toRegex()))
            return false

        if (email.equals("ganesh@gmail.com"))
            return true

        return false
    }

    fun validateUsername(username : String) : Int{

        if (username.isEmpty()){
            return 1 // Empty username
        }

        if (username.contains(" ") || !username.equals("Admin")){
            return 2 // Invalid username
        }

        return 0 // Valid username
    }

    fun validatePassword(password: String): Int {
        if (password.isEmpty()){
            return 1 // Empty password
        }

        if (password.contains(" ") || !password.equals("Admin")){
            return 2 // Invalid password
        }

        return 0 // Valid password
    }
}