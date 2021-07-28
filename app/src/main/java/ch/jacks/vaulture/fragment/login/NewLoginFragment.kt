package ch.jacks.vaulture.fragment.login

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import ch.jacks.vaulture.R
import ch.jacks.vaulture.db.dao.LoginDao
import ch.jacks.vaulture.util.MyTextUtil
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.new_login_fragment.*

class NewLoginFragment : DialogFragment() {
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.new_login_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        MyTextUtil.setupFieldValidation(mapOf(
                newLoginInput to newLoginLayout,
                newPasswordInput to newPasswordLayout,
                newPasswordConfirmInput to newPasswordConfirmLayout
        ))

        btCreateNewLogin.setOnClickListener {
            if (MyTextUtil.fieldsAreValid()) {
                if (TextUtils.equals(newPasswordInput.text, newPasswordConfirmInput.text)) {
                    LoginDao.createLogin(newLoginInput.text.toString(), newPasswordConfirmInput.text.toString())
                    Snackbar.make(view, "New login created successfully", Snackbar.LENGTH_SHORT)
                    findNavController().popBackStack()
                } else {
                    newPasswordConfirmLayout.isErrorEnabled = true
                    newPasswordConfirmLayout.error = "Passwords are not matching"
                }
            }
        }
    }
}