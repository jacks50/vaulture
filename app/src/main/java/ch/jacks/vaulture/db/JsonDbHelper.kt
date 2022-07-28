package ch.jacks.vaulture.db

import ch.jacks.vaulture.app.VaultureApp
import ch.jacks.vaulture.db.entity.PasswordEntity
import ch.jacks.vaulture.util.CryptUtil
import ch.jacks.vaulture.util.PasswordDbUtil
import ch.jacks.vaulture.util.SessionUtil
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.File

object JsonDbHelper {
    private var passwordMap: MutableMap<String, PasswordEntity> = HashMap()

    fun createLogin(newLogin: String, newPassword: String): Boolean {
        val file = File(VaultureApp.appContext.filesDir, "$newLogin.vault")

        if (file.exists())
            return false

        return PasswordDbUtil.exportJson(newLogin, newPassword)
    }

    fun updateLogin() {
        throw Exception("Not implemented yet !!")
    }

    fun deleteLogin() {
        throw Exception("Not implemented yet !!")
    }

    fun getLogin(login: String): File? {
        val file = File(VaultureApp.appContext.filesDir, "$login.vault")

        if (!file.exists())
            return null

        return file
    }

    fun loadFile(file: File, password: String) {
        val decrypted = CryptUtil.decryptInput(
            cipherText = file.readText(),
            cipherPassword = password
        )

        passwordMap = Json.decodeFromString(decrypted)
    }

    fun reloadPasswords() {
        PasswordDbUtil.exportJson(SessionUtil.currentLogin, VaultureApp.currentPwd)

        val file = getLogin(SessionUtil.currentLogin)!!

        loadFile(file, VaultureApp.currentPwd)
    }

    fun createPassword(newPassword: PasswordEntity): Boolean {
        passwordMap[newPassword.passwordUID] = newPassword
        reloadPasswords()
        return true
    }

    fun editPassword(editedPassword: PasswordEntity): Boolean {
        passwordMap[editedPassword.passwordUID] = editedPassword
        reloadPasswords()
        return true
    }

    fun deletePassword(passwordId: String): Boolean {
        return false
    }

    fun getPassword(id: String): PasswordEntity? {
        return passwordMap[id]
    }

    fun getPasswords(): MutableMap<String, PasswordEntity> {
        return passwordMap
    }
}