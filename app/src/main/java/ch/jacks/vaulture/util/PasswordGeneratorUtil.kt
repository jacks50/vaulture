package ch.jacks.vaulture.util

import java.security.SecureRandom

object PasswordGeneratorUtil {
    private var CHAR_LOWERCASE = "abcdefghijklmnopqrstuvwxyz"
    private var CHAR_UPPERCASE = CHAR_LOWERCASE.toUpperCase()
    private var DIGITS = "0123456789"
    private var SPECIAL = "+*%&=-!?$"

    private var random = SecureRandom()

    fun generateSecurePassword(size: Int, nbDigits: Int, nbSpecialChar: Int): String {
        println("Size ${size}, Digits ${nbDigits}, Special ${nbSpecialChar}")
        var sb = StringBuilder()

        var nbLowercaseChars = (size - nbDigits - nbSpecialChar) / 2
        var nbUppercaseChars = size - nbDigits - nbSpecialChar - nbLowercaseChars

        sb.append(generateString(DIGITS, nbDigits))
        sb.append(generateString(SPECIAL, nbSpecialChar))
        sb.append(generateString(CHAR_LOWERCASE, nbLowercaseChars))
        sb.append(generateString(CHAR_UPPERCASE, nbUppercaseChars))

        // TODO : Optimise this, can't be better than creating variables that way ??
        var result = CharArray(sb.length)

        sb.toCharArray(result)

        result.shuffle()

        var shuffledResult = ""

        result.forEach {
            shuffledResult += it
        }

        return shuffledResult
    }

    private fun generateString(charList: String, size: Int): String {
        var result = ""

        for (i in 0 until size) {
            result += charList[random.nextInt(charList.length)]
        }

        return result
    }

}