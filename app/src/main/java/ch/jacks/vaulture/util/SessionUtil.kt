package ch.jacks.vaulture.util

import android.content.Context
import android.content.SharedPreferences
import ch.jacks.vaulture.app.VaultureApp

object SessionUtil {
    private var sp: SharedPreferences = VaultureApp.appContext.getSharedPreferences(VaultureApp.appKey, Context.MODE_PRIVATE)

    const val SP_LOGIN_ID_KEY = "LOGIN_ID"
    var currentLoginId: Long
        get() {
            return sp.getLong(SP_LOGIN_ID_KEY, -1L)
        }
        set(value) {
            sp.edit().putLong(SP_LOGIN_ID_KEY, value).commit()
        }

}