package ch.jacks.vaulture.util

import android.content.res.ColorStateList
import android.graphics.Color
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import androidx.core.content.ContextCompat
import ch.jacks.vaulture.R
import com.google.android.material.textfield.TextInputLayout

object MyTextUtil {
    private const val EMPTY_INPUT_MSG = "Cannot be empty"

//    private var fieldsMessages: Map<TextInputEditText, TextInputLayout> = HashMap()
//
//    fun setupFieldValidation(viewFields: Map<TextInputEditText, TextInputLayout> = HashMap()) {
//        fieldsMessages = viewFields
//    }

    fun TextInputLayout.setCustomFocusChangeListener() {
        editText?.setOnFocusChangeListener { v, hasFocus ->
            val color = if (hasFocus)
                ContextCompat.getColor(context, R.color.onPrimary)
            else
                ContextCompat.getColor(context, R.color.primaryVariant)

            // (loginInput.parent as? TextInputLayout)?.setStartIconTintList(ColorStateList.valueOf(color))
            setStartIconTintList(ColorStateList.valueOf(color))
        }
    }

    fun TextInputLayout.validate(): Boolean {
        if (TextUtils.isEmpty(editText?.text)) {
            isErrorEnabled = true
            error = EMPTY_INPUT_MSG
            return false
        }

        isErrorEnabled = false

        return true
    }

    fun TextInputLayout.invalidate(errMsg: String) {
        isErrorEnabled = true
        error = errMsg
    }

    fun TextInputLayout.colorize(
        colorizeDigits: Boolean = true,
        colorizeSpecial: Boolean = true
    ) {
        editText?.let {
            it.text = colorizeText(it.text.toString(), colorizeDigits, colorizeSpecial)
        }
    }

    fun colorizeText(
        text: String,
        colorizeDigits: Boolean = true,
        colorizeSpecial: Boolean = true,
    ): SpannableStringBuilder {
        val ssb = SpannableStringBuilder(text)

        for (i in text.indices) {
            if (colorizeDigits && text[i].isDigit()) {
                ssb.setSpan(
                    ForegroundColorSpan(Color.MAGENTA),
                    i,
                    i + 1,
                    Spannable.SPAN_INCLUSIVE_INCLUSIVE
                )
            }

            if (colorizeSpecial && !text[i].isLetterOrDigit()) {
                ssb.setSpan(
                    ForegroundColorSpan(Color.CYAN),
                    i,
                    i + 1,
                    Spannable.SPAN_INCLUSIVE_INCLUSIVE
                )
            }
        }

        return ssb
    }

//    fun fieldsAreValid(): Boolean {
//        var valid: Boolean = true
//
//        fieldsMessages.forEach {
//            var textLayout = it.value
//
//            textLayout.isErrorEnabled = false
//
//            if (TextUtils.isEmpty(it.key.text)) {
//                textLayout.isErrorEnabled = true
//                textLayout.error = EMPTY_INPUT_MSG
//                valid = false
//            }
//        }
//
//        return valid
//    }

//    fun colorizeText(
//        text: String,
//        colorizeDigits: Boolean = true,
//        colorizeSpecial: Boolean = true
//    ): SpannableStringBuilder {
//        var ssb = SpannableStringBuilder(text)
//
//        for (i in text.indices) {
//            if (colorizeDigits && text[i].isDigit()) {
//                ssb.setSpan(
//                    ForegroundColorSpan(Color.RED),
//                    i,
//                    i + 1,
//                    Spannable.SPAN_INCLUSIVE_INCLUSIVE
//                )
//            }
//
//            if (colorizeSpecial && !text[i].isLetterOrDigit()) {
//                ssb.setSpan(
//                    ForegroundColorSpan(Color.BLUE),
//                    i,
//                    i + 1,
//                    Spannable.SPAN_INCLUSIVE_INCLUSIVE
//                )
//            }
//        }
//
//        return ssb
//    }
}