package ch.jacks.vaulture.dialog

import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import ch.jacks.vaulture.R
import ch.jacks.vaulture.util.MyTextUtil
import ch.jacks.vaulture.util.PasswordGeneratorUtil
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar

class PasswordGenerateDialog(private var rootView: View) : DialogFragment() {
    // region Password size
    private lateinit var ivPlusSize: ImageButton
    private lateinit var tvPwdSize: TextView
    private lateinit var ivMinusSize: ImageButton
    private var sizeCounter = 12 // default value
    // endregion

    // region Password nb digits
    private lateinit var ivPlusNbDigits: ImageButton
    private lateinit var tvPwdNbDigits: TextView
    private lateinit var ivMinusNbDigits: ImageButton
    private var digitsCounter = 4 // default value
    // endregion

    // region Password nb special chars
    private lateinit var ivPlusNbSpecial: ImageButton
    private lateinit var tvPwdNbSpecial: TextView
    private lateinit var ivMinusNbSpecial: ImageButton
    private var specialCounter = 4 // default value
    // endregion

    private lateinit var tvPwdGenerated: TextView

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
        tvPwdGenerated = customDialogView.findViewById(R.id.tvPwdGenerated)

        tvPwdGenerated.text = MyTextUtil.colorizeText(
            PasswordGeneratorUtil
                .generateSecurePassword(sizeCounter, digitsCounter, specialCounter)
        )
        // endregion

        return MaterialAlertDialogBuilder(requireActivity())
            .setView(customDialogView)
            .setNegativeButton("Close") { _, _ -> }
            .setPositiveButton("Copy") { _, _ ->
                var clipboard: ClipboardManager =
                    requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                var clipData: ClipData = ClipData.newPlainText("Password", tvPwdGenerated.text)
                clipboard.setPrimaryClip(clipData)
                Snackbar.make(rootView, "Generated password copied !", Snackbar.LENGTH_SHORT).show()
            }
            .create()
    }

    private fun setupListener(
        ivPlus: ImageButton,
        ivMinus: ImageButton,
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
        tvPwdGenerated.text = MyTextUtil.colorizeText(
            PasswordGeneratorUtil
                .generateSecurePassword(sizeCounter, digitsCounter, specialCounter)
        )
    }
}