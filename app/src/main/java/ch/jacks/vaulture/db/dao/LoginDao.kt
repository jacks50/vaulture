package ch.jacks.vaulture.db.dao

import android.content.ContentValues
import android.provider.BaseColumns
import ch.jacks.vaulture.app.VaultureApp
import ch.jacks.vaulture.db.DbHelper
import ch.jacks.vaulture.db.entity.LoginEntity

object LoginDao {
    fun createLogin(newLogin: String, newPassword: String): Boolean {
        var values = ContentValues().apply {
            put(DbHelper.VaultureContract.LoginEntity.COLUMN_LOGIN_USERNAME, newLogin)
            put(DbHelper.VaultureContract.LoginEntity.COLUMN_LOGIN_PASSWORD, newPassword)
        }

        var newLoginId = VaultureApp.getWritableDb().insert(
                DbHelper.VaultureContract.LoginEntity.TABLE_NAME,
                null,
                values
        )

        return newLoginId != -1L
    }

    fun updateLogin() {

    }

    fun deleteLogin() {

    }

    fun getLogin(login: String, password: String): LoginEntity? {
        var columnsGet = arrayOf(BaseColumns._ID, DbHelper.VaultureContract.LoginEntity.COLUMN_LOGIN_USERNAME)

        var whereClause =
                "${DbHelper.VaultureContract.LoginEntity.COLUMN_LOGIN_USERNAME} = ? AND " +
                        "${DbHelper.VaultureContract.LoginEntity.COLUMN_LOGIN_PASSWORD} = ?"

        var whereValues = arrayOf(login, password)

        var cr = VaultureApp.getReadableDb().query(
                DbHelper.VaultureContract.LoginEntity.TABLE_NAME,
                columnsGet,
                whereClause,
                whereValues,
                null,
                null,
                null
        )

        with(cr) {
            if (moveToFirst())
                return LoginEntity(
                        getLong(getColumnIndexOrThrow(BaseColumns._ID)),
                        getString(getColumnIndexOrThrow(DbHelper.VaultureContract.LoginEntity.COLUMN_LOGIN_USERNAME))
                )
        }

        return null
    }
}