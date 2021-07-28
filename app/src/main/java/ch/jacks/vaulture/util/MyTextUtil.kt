package ch.jacks.vaulture.util

import android.text.TextUtils
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
}