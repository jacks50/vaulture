package ch.jacks.vaulture.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ch.jacks.vaulture.R
import ch.jacks.vaulture.abstract.AbsMenuSheetDialog
import ch.jacks.vaulture.listener.ISheetListener
import kotlinx.android.synthetic.main.menu_sheet_dialog.*

class MainMenuSheet(private val listener: ISheetListener): AbsMenuSheetDialog() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.menu_sheet_dialog, container, false)
    }

    override fun setupMenuItems(view: View) {
        sheetItemAddPwd.setOnClickListener{
            dismissWithResult(ADD_KEY)
        }

        sheetItemAddQR.setOnClickListener{
            dismissWithResult(ADD_QR_KEY)
        }

        sheetItemExport.setOnClickListener{
            dismissWithResult(EXPORT_KEY)
        }
    }

    override fun dismissWithResult(any: Any) {
        listener.callback(any)
        dismiss()
    }

    companion object {
        const val TAG = "MainMenuSheet"

        const val ADD_KEY = "ADD_PWD"
        const val ADD_QR_KEY = "ADD_QR_PWD"
        const val EXPORT_KEY = "EXPORT_PWD"
    }
}