package ch.jacks.vaulture.abstract

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.Window
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class AbsMenuSheetDialog() : BottomSheetDialogFragment() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupMenuItems(view)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    abstract fun dismissWithResult(key: String)
    abstract fun setupMenuItems(view: View)
}