package ch.jacks.vaulture.dialog

import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import ch.jacks.vaulture.R
import ch.jacks.vaulture.util.MyTextUtil.colorize
import ch.jacks.vaulture.util.PasswordGeneratorUtil
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout

// TODO : Correct generation of password (size related)
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

    private lateinit var pwdGenerated: TextView
    private lateinit var pwdGeneratedLayout: TextInputLayout

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        var customDialogView = requireActivity()
            .layoutInflater
            .inflate(R.layout.password_generation_layout, null)

        // region Password size UI elements management
        ivPlusSize = customDialogView.findViewById(R.id.ivPlusSize)
        tvPwdSize = customDialogView.findViewById(R.id.tvPwdSize)
        ivMinusSize = customDialogView.findViewById(R.id.ivMinusSize)
        tvPwdSize.text = sizeCounter.toString()

        setupListener(
            ivPlusSize,
            ivMinusSize,
            tvPwdSize,
            { x -> sizeCounter += x; sizeCounter },
            0
        )
        // endregion

        // region Password digits UI elements management
        ivPlusNbDigits = customDialogView.findViewById(R.id.ivPlusNbDigits)
        tvPwdNbDigits = customDialogView.findViewById(R.id.tvPwdNbDigits)
        ivMinusNbDigits = customDialogView.findViewById(R.id.ivMinusNbDigits)
        tvPwdNbDigits.text = digitsCounter.toString()

        setupListener(
            ivPlusNbDigits,
            ivMinusNbDigits,
            tvPwdNbDigits,
            { x -> digitsCounter += x; digitsCounter },
            0
        )
        // endregion

        // region Password special chars UI elements management
        ivPlusNbSpecial = customDialogView.findViewById(R.id.ivPlusNbSpecial)
        tvPwdNbSpecial = customDialogView.findViewById(R.id.tvPwdNbSpecial)
        ivMinusNbSpecial = customDialogView.findViewById(R.id.ivMinusNbSpecial)
        tvPwdNbSpecial.text = specialCounter.toString()

        setupListener(
            ivPlusNbSpecial,
            ivMinusNbSpecial,
            tvPwdNbSpecial,
            { x -> specialCounter += x; specialCounter },
            0
        )
        // endregion

        // region Password size UI elements management
        pwdGenerated = customDialogView.findViewById(R.id.pwdGenerated)
        pwdGeneratedLayout = customDialogView.findViewById(R.id.pwdGeneratedLayout)

        pwdGenerated.text = PasswordGeneratorUtil
            .generateSecurePassword(sizeCounter, digitsCounter, specialCounter)
        pwdGeneratedLayout.colorize()
        // endregion

        return MaterialAlertDialogBuilder(requireActivity())
            .setView(customDialogView)
            .setNegativeButton("Close") { _, _ -> }
            .setPositiveButton("Copy") { _, _ ->
                var clipboard: ClipboardManager =
                    requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                var clipData: ClipData = ClipData.newPlainText("Password", pwdGenerated.text)
                clipboard.setPrimaryClip(clipData)
                Snackbar.make(rootView, "Generated password copied !", Snackbar.LENGTH_SHORT).show()
            }
            .create()
    }

    private fun setupListener(
        ivPlus: Button,
        ivMinus: Button,
        tvValue: TextView,
        counter: (Int) -> Int,
        counterMax: Int
    ) {
        ivPlus.setOnClickListener {
            tvValue.text = counter(+1).toString()

            // TODO : Manage max of the counter to avoid having too long passwords following case
            generatePassword()
        }

        ivMinus.setOnClickListener {
            if (counter(0) > 0) {
                tvValue.text = counter(-1).toString()

                generatePassword()
            }
        }
    }

    private fun generatePassword() {
        pwdGenerated.text = PasswordGeneratorUtil
            .generateSecurePassword(sizeCounter, digitsCounter, specialCounter)

        pwdGeneratedLayout.colorize()
    }
}