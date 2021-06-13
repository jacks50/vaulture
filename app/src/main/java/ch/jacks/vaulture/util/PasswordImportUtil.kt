package ch.jacks.vaulture.util

import android.text.TextUtils
import ch.jacks.vaulture.db.dao.PasswordDao
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

object PasswordImportUtil {
    // region CSV index keys for import
    const val URL_INDEX = 0
    const val USERNAME_INDEX = 1
    const val PASSWORD_INDEX = 2
    const val TOTP_INDEX = 3
    const val EXTRA_INDEX = 4
    const val NAME_INDEX = 5
    const val GROUP_INDEX = 6
    const val FAVICON_INDEX = 7
    // endregion

    fun readPasswordFile(cacheDir: File, csvInputStream: InputStream) {
        var csvFile = File(cacheDir, "csvpwd.tmp")

        csvInputStream.use { input ->
            val outputStream = FileOutputStream(csvFile)
            outputStream.use { output ->
                val buffer = ByteArray(4 * 1024) // buffer size
                while (true) {
                    val byteCount = input.read(buffer)
                    if (byteCount < 0) break
                    output.write(buffer, 0, byteCount)
                }
                output.flush()
            }
        }

        csvFile.forEachLine {
            var csvData = TextUtils.split(it, ",")
            PasswordDao.createPassword(csvData[5], csvData[1], csvData[0], csvData[2], SessionUtil.currentLoginId)
        }

        csvFile.delete()
    }
}