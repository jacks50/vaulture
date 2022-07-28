package ch.jacks.vaulture.dialog

import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import ch.jacks.vaulture.R
import ch.jacks.vaulture.util.MyTextUtil.colorize
import ch.jacks.vaulture.util.PasswordGeneratorUtil
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout

// ----------------- TODO : WHOLE REFACTORING REQUIRED HERE !! -------------------------
// ----------------- TODO : WHOLE REFACTORING REQUIRED HERE !! -------------------------
// ----------------- TODO : WHOLE REFACTORING REQUIRED HERE !! -------------------------
class PasswordGenerateDialog(private var rootView: View) : DialogFragment() {
    // region Password size
    private lateinit var ivPlusSize: Button
    private lateinit var tvPwdSize: TextView
    private lateinit var ivMinusSize: Button
    private var sizeCounter = 12 // default value
    // endregion

    // region Password nb digits
    private lateinit var ivPlusNbDigits: Button
    private lateinit var tvPwdNbDigits: TextView
    private lateinit var ivMinusNbDigits: Button
    private var digitsCounter = 4 // default value
    // endregion

    // region Password nb special chars
    private lateinit var ivPlusNbSpecial: Button
    private lateinit var tvPwdNbSpecial: TextView
    private lateinit var ivMinusNbSpecial: Button
    private var specialCounter = 4 // default value
    // endregion

    private lateinit var ivReload: ImageView

    private lateinit var btCopy: Button
    private lateinit var btClose: Button

    private var MAX_LENGTH = 24

    private lateinit var pwdGenerated: TextView
    private lateinit var pwdGeneratedLayout: TextInputLayout

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val customDialogView = requireActivity()
            .layoutInflater
            .inflate(R.layout.password_generation_layout, null)

        // region Password size UI elements management
        ivPlusSize = customDialogView.findViewById(R.id.ivPlusSize)
        tvPwdSize = customDialogView.findViewById(R.id.tvPwdSize)
        ivMinusSize = customDialogView.findViewById(R.id.ivMinusSize)
        tvPwdSize.text = sizeCounter.toString()

        setupButtonListener(
            ivPlusSize,
            ivMinusSize,
            tvPwdSize,
        ) {
            if ((it > 0 && sizeCounter < MAX_LENGTH) || (it < 0 && sizeCounter > (digitsCounter + specialCounter))) {
                sizeCounter += it
                tvPwdSize.text = sizeCounter.toString()
                generatePassword()
            }

            sizeCounter
        }
        // endregion

        // region Password digits UI elements management
        ivPlusNbDigits = customDialogView.findViewById(R.id.ivPlusNbDigits)
        tvPwdNbDigits = customDialogView.findViewById(R.id.tvPwdNbDigits)
        ivMinusNbDigits = customDialogView.findViewById(R.id.ivMinusNbDigits)
        tvPwdNbDigits.text = digitsCounter.toString()

        setupButtonListener(
            ivPlusNbDigits,
            ivMinusNbDigits,
            tvPwdNbDigits,
        ) {
            if ((it > 0 && digitsCounter < (sizeCounter - specialCounter)) || (it < 0 && digitsCounter > 0)) {
                digitsCounter += it
                tvPwdNbDigits.text = digitsCounter.toString()
                generatePassword()
            }

            digitsCounter
        }
        // endregion

        // region Password special chars UI elements management
        ivPlusNbSpecial = customDialogView.findViewById(R.id.ivPlusNbSpecial)
        tvPwdNbSpecial = customDialogView.findViewById(R.id.tvPwdNbSpecial)
        ivMinusNbSpecial = customDialogView.findViewById(R.id.ivMinusNbSpecial)
        tvPwdNbSpecial.text = specialCounter.toString()

        setupButtonListener(
            ivPlusNbSpecial,
            ivMinusNbSpecial,
            tvPwdNbSpecial,
        ) {
            if ((it > 0 && specialCounter < (sizeCounter - digitsCounter)) || (it < 0 && specialCounter > 0)) {
                specialCounter += it
                tvPwdNbSpecial.text = specialCounter.toString()
                generatePassword()
            }

            specialCounter
        }
        // endregion

        ivReload = customDialogView.findViewById(R.id.ivReload)
        ivReload.setOnClickListener {
            generatePassword()
        }

        // region Password size UI elements management
        pwdGenerated = customDialogView.findViewById(R.id.pwdGenerated)
        pwdGeneratedLayout = customDialogView.findViewById(R.id.pwdGeneratedLayout)

        pwdGenerated.text = PasswordGeneratorUtil
            .generateSecurePassword(sizeCounter, digitsCounter, specialCounter)
        pwdGeneratedLayout.colorize()
        // endregion

        btCopy = customDialogView.findViewById(R.id.btCopy)
        btClose = customDialogView.findViewById(R.id.btClose)

        btCopy.setOnClickListener {
            val clipboard: ClipboardManager =
                requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData: ClipData = ClipData.newPlainText("Password", pwdGenerated.text)
            clipboard.setPrimaryClip(clipData)
            Snackbar.make(rootView, "Generated password copied !", Snackbar.LENGTH_SHORT).show()
        }

        btClose.setOnClickListener {
            dismiss()
        }

        return MaterialAlertDialogBuilder(requireActivity())
            .setView(customDialogView)
            .create()
    }

    private fun setupButtonListener(
        ivPlus: Button,
        ivMinus: Button,
        tvValue: TextView,
        counter: (Int) -> Int,
    ) {
        ivPlus.setOnClickListener {
            counter(+1)
        }

        ivMinus.setOnClickListener {
            counter(-1)
        }
    }

    private fun generatePassword() {
        pwdGenerated.text = PasswordGeneratorUtil
            .generateSecurePassword(sizeCounter, digitsCounter, specialCounter)

        pwdGeneratedLayout.colorize()
    }
}