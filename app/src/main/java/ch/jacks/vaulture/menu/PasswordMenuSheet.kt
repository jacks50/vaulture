package ch.jacks.vaulture.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ch.jacks.vaulture.R
import ch.jacks.vaulture.abs.AbsMenuSheetDialog
import kotlinx.android.synthetic.main.password_sheet_dialog.*

class PasswordMenuSheet(
    private val callback: (String) -> Unit
) : AbsMenuSheetDialog() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.password_sheet_dialog, container, false)
    }

    override fun setupMenuItems(view: View) {
        sheetItemCopyUsername.setOnClickListener {
            dismissWithResult(COPY_USERNAME_KEY)
        }

        sheetItemCopyPwd.setOnClickListener {
            dismissWithResult(COPY_PWD_KEY)
        }

        sheetItemShowPwd.setOnClickListener {
            dismissWithResult(SHOW_PWD_KEY)
        }

        sheetItemEditPwd.setOnClickListener {
            dismissWithResult(EDIT_KEY)
        }

        sheetItemDeletePwd.setOnClickListener {
            dismissWithResult(DELETE_KEY)
        }
    }

    override fun dismissWithResult(key: String) {
        callback(key)
        dismiss()
    }

    companion object {
        const val TAG = "PwdMenuSheet"

        const val EDIT_KEY = "EDIT_PWD"
        const val COPY_USERNAME_KEY = "COPY_USERNAME"
        const val COPY_PWD_KEY = "COPY_PWD"
        const val SHOW_PWD_KEY = "SHOW_PWD"
        const val DELETE_KEY = "DELETE_PWD"
    }
}