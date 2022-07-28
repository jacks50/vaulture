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
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout

class PasswordShowDialog(
    private var rootView: View,
    private var password: String,
) : DialogFragment() {
    private lateinit var pwdShow: TextView
    private lateinit var pwdShowLayout: TextInputLayout
    private lateinit var btCopy: Button
    private lateinit var btClose: Button

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        var customDialogView = requireActivity()
            .layoutInflater
            .inflate(R.layout.password_show_layout, null)

        pwdShow = customDialogView.findViewById(R.id.pwdShow)
        pwdShowLayout = customDialogView.findViewById(R.id.pwdShowLayout)
        btCopy = customDialogView.findViewById(R.id.btCopy)
        btClose = customDialogView.findViewById(R.id.btClose)

        pwdShow.text = password
        pwdShowLayout.colorize()

        btCopy.setOnClickListener {
            val clipboard: ClipboardManager =
                requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData: ClipData =
                ClipData.newPlainText("Password", password)
            clipboard.setPrimaryClip(clipData)

            Snackbar
                .make(rootView, "Password copied", Snackbar.LENGTH_SHORT)
                .show()

            dismiss()
        }

        btClose.setOnClickListener {
            dismiss()
        }

        return MaterialAlertDialogBuilder(requireActivity())
            .setView(customDialogView)
            .create()
    }
}