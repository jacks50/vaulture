package ch.jacks.vaulture.app

import android.app.Application
import android.content.Context

class VaultureApp : Application() {
    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
    }

    companion object {
        const val appKey = "VAULTURE_APP_CTX"

        lateinit var appContext: Context
        lateinit var currentPwd: String
    }
}