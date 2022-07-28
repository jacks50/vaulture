package ch.jacks.vaulture.util

import android.content.Context
import android.content.SharedPreferences
import ch.jacks.vaulture.app.VaultureApp

object SessionUtil {
    private var sp: SharedPreferences =
        VaultureApp.appContext.getSharedPreferences(VaultureApp.appKey, Context.MODE_PRIVATE)

    // region Login configuration parameters
    private const val SP_CFG_REMEMBER_ME_KEY = "CFG_REMEMBER_ME"
    // endregion

    // region Application status parameters
    private const val SP_LAST_LOGIN_KEY = "LAST_LOGIN"
    private const val SP_LOGIN_ID_KEY = "LOGIN_ID"
    private const val SP_LOGIN_KEY = "LOGIN"

    // endregion
    var rememberMe: Boolean
        get() {
            return sp.getBoolean(SP_CFG_REMEMBER_ME_KEY, false)
        }
        set(value) {
            sp.edit().putBoolean(SP_CFG_REMEMBER_ME_KEY, value).apply()
        }

    var lastLogin: String
        get() {
            return sp.getString(SP_LAST_LOGIN_KEY, "") ?: ""
        }
        set(value) {
            sp.edit().putString(SP_LAST_LOGIN_KEY, value).apply()
        }

    var currentLoginId: Long
        get() {
            return sp.getLong(SP_LOGIN_ID_KEY, -1L)
        }
        set(value) {
            sp.edit().putLong(SP_LOGIN_ID_KEY, value).apply()
        }

    var currentLogin: String
        get() {
            return sp.getString(SP_LOGIN_KEY, "") ?: ""
        }
        set(value) {
            sp.edit().putString(SP_LOGIN_KEY, value).apply()
        }
}