package ch.jacks.vaulture.app

import android.app.Application
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import ch.jacks.vaulture.db.DbHelper

class VaultureApp : Application() {
    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
        dbHelper = DbHelper()
    }

    companion object {
        const val appKey = "VAULTURE_APP_CTX"

        lateinit var appContext: Context
        lateinit var dbHelper: DbHelper

        fun getReadableDb(): SQLiteDatabase {
            return dbHelper.readableDatabase
        }

        fun getWritableDb(): SQLiteDatabase {
            return dbHelper.writableDatabase
        }
    }
}