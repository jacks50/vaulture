package ch.jacks.vaulture.util

import ch.jacks.vaulture.app.VaultureApp
import ch.jacks.vaulture.db.JsonDbHelper
import ch.jacks.vaulture.util.CryptUtil.charPool
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.io.InputStream

// TODO : Refactor this whole class and unify with import
object PasswordDbUtil {
    // CSV index keys for import from LastPass
    const val URL_INDEX = 0
    const val USERNAME_INDEX = 1
    const val PASSWORD_INDEX = 2
    const val TOTP_INDEX = 3
    const val EXTRA_INDEX = 4
    const val NAME_INDEX = 5
    const val GROUP_INDEX = 6
    const val FAVICON_INDEX = 7

    fun importJson() {

    }

    fun exportJson(login: String, pwd: String): Boolean {
        var saltStr = (1..16)
            .map { charPool.random() }
            .joinToString("")

        var ivStr = (1..16)
            .map { charPool.random() }
            .joinToString("")

        var salt = saltStr.toByteArray()
        var ivByte = ivStr.toByteArray()

        var key = CryptUtil.generateKey(pwd, salt)
        var iv = CryptUtil.generateIv(ivByte)

        var input = Json.encodeToString(JsonDbHelper.getPasswords())

        var encrypted =
            saltStr + ivStr + CryptUtil.encryptInput(input = input, key = key, ivParam = iv)

        val file = File(VaultureApp.appContext.filesDir, "${login}.vault")

        file.createNewFile()
        file.writeText(encrypted)

        return true
    }

    fun readPasswordFile(cacheDir: File, csvInputStream: InputStream) {
//        var csvFile = File(cacheDir, "csvpwd.tmp")
//
//        csvInputStream.use { input ->
//            val outputStream = FileOutputStream(csvFile)
//            outputStream.use { output ->
//                val buffer = ByteArray(4 * 1024) // buffer size
//                while (true) {
//                    val byteCount = input.read(buffer)
//                    if (byteCount < 0) break
//                    output.write(buffer, 0, byteCount)
//                }
//                output.flush()
//            }
//        }
//
//        csvFile.forEachLine {
//            var csvData = TextUtils.split(it, ",")
//            JsonDbHelper.createPassword(
//                PasswordEntity(
//                    -1L,
//                    csvData[NAME_INDEX],
//                    csvData[USERNAME_INDEX],
//                    csvData[URL_INDEX],
//                    csvData[PASSWORD_INDEX],
//                    SessionUtil.currentLoginId
//                )
//            )
//        }
//
//        csvFile.delete()
    }
}