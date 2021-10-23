package ch.jacks.vaulture.util

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

object MyTextUtil {
    private const val EMPTY_INPUT_MSG = "Cannot be empty"

    private var fieldsMessages: Map<TextInputEditText, TextInputLayout> = HashMap()

    fun setupFieldValidation(viewFields: Map<TextInputEditText, TextInputLayout> = HashMap()) {
        fieldsMessages = viewFields
    }

    fun fieldsAreValid(): Boolean {
        var valid: Boolean = true

        fieldsMessages.forEach {
            var textLayout = it.value

            textLayout.isErrorEnabled = false

            if (TextUtils.isEmpty(it.key.text)) {
                textLayout.isErrorEnabled = true
                textLayout.error = EMPTY_INPUT_MSG
                valid = false
            }
        }

        return valid
    }

    fun colorizeText(text: String, colorizeDigits: Boolean = true, colorizeSpecial: Boolean = true): SpannableStringBuilder {
        var ssb = SpannableStringBuilder(text)

        for (i in text.indices) {
            if (colorizeDigits && text[i].isDigit()) {
                ssb.setSpan(ForegroundColorSpan(Color.RED), i, i + 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
            }

            if (colorizeSpecial && !text[i].isLetterOrDigit()) {
                ssb.setSpan(ForegroundColorSpan(Color.BLUE), i, i + 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
            }
        }

        return ssb
    }
}