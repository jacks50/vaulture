package ch.jacks.vaulture.util

import android.os.Build
import java.util.*
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

object CryptUtil {
    val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')

    fun generateKey(password: String, salt: ByteArray): SecretKey {
        val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
        val spec = PBEKeySpec(password.toCharArray(), salt, 65536, 256)
        return SecretKeySpec(factory.generateSecret(spec).encoded, "AES")
    }

    fun generateIv(iv: ByteArray): IvParameterSpec {
        return IvParameterSpec(iv)
    }

    fun encryptInput(
        algo: String = "AES/CBC/PKCS5Padding",
        input: String,
        key: SecretKey,
        ivParam: IvParameterSpec
    ): String {
        val cipher = Cipher.getInstance(algo)
        cipher.init(Cipher.ENCRYPT_MODE, key, ivParam)

        val cipherText = cipher.doFinal(input.toByteArray())

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Base64.getEncoder().encodeToString(cipherText)
        } else {
            TODO("VERSION.SDK_INT < O")
        }
    }

    fun decryptInput(
        algo: String = "AES/CBC/PKCS5Padding",
        cipherText: String,
        cipherPassword: String
    ): String {
        val salt = cipherText.take(16).toByteArray()
        val ivByte = cipherText.substring(16, 32).toByteArray()

        val key = generateKey(cipherPassword, salt)
        val iv = generateIv(ivByte)

        val cipher = Cipher.getInstance(algo)
        cipher.init(Cipher.DECRYPT_MODE, key, iv)

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String(cipher.doFinal(Base64.getDecoder().decode(cipherText.drop(32))))
        } else {
            TODO("VERSION.SDK_INT < O")
        }
    }
}