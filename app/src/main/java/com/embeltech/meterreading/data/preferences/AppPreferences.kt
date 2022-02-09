package com.embeltech.meterreading.data.preferences

import android.content.Context
import android.content.SharedPreferences

/**
 * Singleton class for creating shared preferences.
 */

class AppPreferences
/**
 * Constructor
 * @param context
 */
private constructor(context: Context) {
    private val appSharedPrefs: SharedPreferences
    private val prefsEditor: SharedPreferences.Editor

    init {
        this.appSharedPrefs = context.getSharedPreferences(
            SDK_SHARED_PREFS,
            Context.MODE_PRIVATE
        )
        this.prefsEditor = appSharedPrefs.edit()
    }

    /**
     * Returns preferences
     * @param name
     * @return string value of key
     */
    fun getPreferences(name: String): String? {
        return appSharedPrefs.getString(name, "")
    }

    /**
     * Returns preferences
     * @param name
     * @param defualtValue
     * @return value of key if set or default value send in parameter
     */
    fun getPreferences(name: String, defualtValue: String): String? {
        return appSharedPrefs.getString(name, defualtValue)
    }

    fun getPreferences(name: String, defaultValue: Boolean): Boolean {
        return appSharedPrefs.getBoolean(name, defaultValue)
    }

    /**
     * Write in preference
     * @param name
     * @param text
     */
    fun savePreferences(name: String, text: String) {
        prefsEditor.putString(name, text)
        prefsEditor.commit()
    }

    fun savePreferences(name: String, flag: Boolean) {
        prefsEditor.putBoolean(name, flag)
        prefsEditor.commit()
    }

    fun saveToken(token: String) {
        prefsEditor.putString(PREFS_KEY_TOKEN, "Bearer $token")
        prefsEditor.commit()
    }

    fun getToken(): String? {
        return appSharedPrefs.getString(PREFS_KEY_TOKEN, "")
    }

    fun saveUserId(userId: Long) {
        prefsEditor.putLong(PREFS_KEY_USER_ID, userId)
        prefsEditor.commit()
    }

    fun getUserId(): Long {
        return appSharedPrefs.getLong(PREFS_KEY_USER_ID, 0)
    }

    fun saveUserRole(userRole: String) {
        prefsEditor.putString(PREFS_KEY_USER_ROLE, userRole)
        prefsEditor.commit()
    }

    fun getUserRole(): String? {
        return appSharedPrefs.getString(PREFS_KEY_USER_ROLE, "")
    }

    fun saveUserName(name: String) {
        prefsEditor.putString(PREFS_KEY_USER_NAME, name)
        prefsEditor.commit()
    }

    fun getUserName(): String? {
        return appSharedPrefs.getString(PREFS_KEY_USER_NAME, "")
    }

    companion object {
        private const val SDK_SHARED_PREFS = "your_app_package.shared_preferences"
        private var mInstance: AppPreferences? = null

        const val PREFS_KEY_USER_ROLE = "prefs_key_user_role"
        const val PREFS_KEY_USER_NAME = "user_name"
        const val PREFS_KEY_TOKEN = "Authorization"
        const val PREFS_KEY_USER_ID = "user_id"

        fun getInstance(context: Context): AppPreferences {
            if (mInstance == null) {
                mInstance = AppPreferences(context)
            }
            return mInstance!!
        }
    }
}
