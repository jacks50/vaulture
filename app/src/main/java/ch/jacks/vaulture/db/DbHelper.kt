package ch.jacks.vaulture.db

import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import ch.jacks.vaulture.app.VaultureApp

class DbHelper : SQLiteOpenHelper(
        VaultureApp.appContext,
        VaultureContract.DB_NAME,
        null,
        VaultureContract.DB_VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {
        db?.let { it.execSQL(VaultureContract.SQL_CREATE_LOGIN_ENTRIES) }
        db?.let { it.execSQL(VaultureContract.SQL_CREATE_VAULT_ENTRIES) }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }

    object VaultureContract {
        const val DB_NAME = "VaultureDB.db"
        const val DB_VERSION = 1

        const val SQL_CREATE_LOGIN_ENTRIES = "CREATE TABLE ${LoginEntity.TABLE_NAME} ( " +
                "${BaseColumns._ID} INTEGER PRIMARY KEY, " +
                "${LoginEntity.COLUMN_LOGIN_USERNAME} TEXT, " +
                "${LoginEntity.COLUMN_LOGIN_PASSWORD} TEXT " +
                ")"

        const val SQL_CREATE_VAULT_ENTRIES = "CREATE TABLE ${PasswordEntity.TABLE_NAME} ( " +
                "${BaseColumns._ID} INTEGER PRIMARY KEY, " +
                "${PasswordEntity.COLUMN_PWD_NAME} TEXT, " +
                "${PasswordEntity.COLUMN_PWD_USERNAME} TEXT, " +
                "${PasswordEntity.COLUMN_PWD_URL} TEXT, " +
                "${PasswordEntity.COLUMN_PWD_PWD} TEXT, " +
                "${PasswordEntity.COLUMN_PWD_LOGIN_ID} INT " +
                ")"

        object LoginEntity : BaseColumns {
            const val TABLE_NAME = "LOGIN"
            const val COLUMN_LOGIN_USERNAME = "USERNAME"
            const val COLUMN_LOGIN_PASSWORD = "PASSWORD"
        }

        object PasswordEntity : BaseColumns {
            const val TABLE_NAME = "VAULTPWD"
            const val COLUMN_PWD_NAME = "NAME"
            const val COLUMN_PWD_USERNAME = "USERNAME"
            const val COLUMN_PWD_URL = "URL"
            const val COLUMN_PWD_PWD = "PASSWORD"
            const val COLUMN_PWD_LOGIN_ID = "LOGIN_ID"
        }
    }
}