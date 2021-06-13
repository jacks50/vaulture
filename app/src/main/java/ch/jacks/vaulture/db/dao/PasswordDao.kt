package ch.jacks.vaulture.db.dao

import android.content.ContentValues
import android.provider.BaseColumns
import ch.jacks.vaulture.app.VaultureApp
import ch.jacks.vaulture.db.DbHelper
import ch.jacks.vaulture.db.entity.PasswordEntity

object PasswordDao {
    fun createPassword(name: String, username: String, url: String, password: String, loginId: Long): Boolean {
        var values = ContentValues().apply {
            put(DbHelper.VaultureContract.PasswordEntity.COLUMN_PWD_NAME, name)
            put(DbHelper.VaultureContract.PasswordEntity.COLUMN_PWD_URL, url)
            put(DbHelper.VaultureContract.PasswordEntity.COLUMN_PWD_USERNAME, username)
            put(DbHelper.VaultureContract.PasswordEntity.COLUMN_PWD_PWD, password)
            put(DbHelper.VaultureContract.PasswordEntity.COLUMN_PWD_LOGIN_ID, loginId)
        }

        return -1L != VaultureApp.getWritableDb().insert(
                DbHelper.VaultureContract.PasswordEntity.TABLE_NAME,
                null,
                values
        )
    }

    fun editPassword(name: String, username: String, url: String, password: String, passwordId: Long): Boolean {
        var values = ContentValues().apply {
            put(DbHelper.VaultureContract.PasswordEntity.COLUMN_PWD_NAME, name)
            put(DbHelper.VaultureContract.PasswordEntity.COLUMN_PWD_URL, url)
            put(DbHelper.VaultureContract.PasswordEntity.COLUMN_PWD_USERNAME, username)
            put(DbHelper.VaultureContract.PasswordEntity.COLUMN_PWD_PWD, password)
        }

        return -1 != VaultureApp.getWritableDb().update(
                DbHelper.VaultureContract.PasswordEntity.TABLE_NAME,
                values,
                "_id = ?",
                arrayOf("$passwordId")
        )
    }

    fun deletePassword(passwordId: Long): Boolean {
        val selection = "${BaseColumns._ID} = ?"
        val selectionArgs = arrayOf("$passwordId")

        return -1 != VaultureApp.getWritableDb().delete(
                DbHelper.VaultureContract.PasswordEntity.TABLE_NAME,
                selection,
                selectionArgs)
    }

    fun getPassword(id: Long): PasswordEntity? {
        var cr = VaultureApp.getReadableDb().rawQuery(
                "SELECT * FROM ${DbHelper.VaultureContract.PasswordEntity.TABLE_NAME} " +
                        "WHERE _id = $id", null
        )

        with(cr) {
            if(moveToFirst())
                return PasswordEntity(
                        getLong(getColumnIndexOrThrow(BaseColumns._ID)),
                        getString(getColumnIndexOrThrow(DbHelper.VaultureContract.PasswordEntity.COLUMN_PWD_NAME)),
                        getString(getColumnIndexOrThrow(DbHelper.VaultureContract.PasswordEntity.COLUMN_PWD_USERNAME)),
                        getString(getColumnIndexOrThrow(DbHelper.VaultureContract.PasswordEntity.COLUMN_PWD_URL)),
                        getString(getColumnIndexOrThrow(DbHelper.VaultureContract.PasswordEntity.COLUMN_PWD_PWD)),
                        getLong(getColumnIndexOrThrow(DbHelper.VaultureContract.PasswordEntity.COLUMN_PWD_LOGIN_ID)),
                )
        }

        return null
    }

    fun getPasswords(loginId: Long): ArrayList<PasswordEntity> {
        var cr = VaultureApp.getReadableDb().rawQuery(
                "SELECT * FROM ${DbHelper.VaultureContract.PasswordEntity.TABLE_NAME} " +
                        "WHERE LOGIN_ID = $loginId", null
        )

        val passwordList = ArrayList<PasswordEntity>()

        with(cr) {
            while(moveToNext()) {
                val pwd = PasswordEntity(
                        getLong(getColumnIndexOrThrow(BaseColumns._ID)),
                        getString(getColumnIndexOrThrow(DbHelper.VaultureContract.PasswordEntity.COLUMN_PWD_NAME)),
                        getString(getColumnIndexOrThrow(DbHelper.VaultureContract.PasswordEntity.COLUMN_PWD_USERNAME)),
                        getString(getColumnIndexOrThrow(DbHelper.VaultureContract.PasswordEntity.COLUMN_PWD_URL)),
                        getString(getColumnIndexOrThrow(DbHelper.VaultureContract.PasswordEntity.COLUMN_PWD_PWD)),
                        getLong(getColumnIndexOrThrow(DbHelper.VaultureContract.PasswordEntity.COLUMN_PWD_LOGIN_ID)),
                )

                passwordList.add(pwd)
            }
        }

        return passwordList
    }
}