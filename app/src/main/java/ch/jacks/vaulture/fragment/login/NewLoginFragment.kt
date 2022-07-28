package ch.jacks.vaulture.fragment.login

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import ch.jacks.vaulture.R
import ch.jacks.vaulture.abs.AbsMainFragment
import ch.jacks.vaulture.db.JsonDbHelper
import ch.jacks.vaulture.util.MyTextUtil.invalidate
import ch.jacks.vaulture.util.MyTextUtil.setCustomFocusChangeListener
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.new_login_fragment.*

class NewLoginFragment : AbsMainFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.new_login_fragment, container, false)
    }

    override fun setupUIComponents() {
        addToValidation(
            newLoginLayout,
            newPasswordLayout,
            newPasswordConfirmLayout
        )
    }

    override fun setupListeners(view: View) {
        newLoginLayout.setCustomFocusChangeListener()
        newPasswordLayout.setCustomFocusChangeListener()
        newPasswordConfirmLayout.setCustomFocusChangeListener()

        btCreateNewLogin.setOnClickListener {
            if (fieldsAreValid()) {
                if (TextUtils.equals(newPasswordInput.text, newPasswordConfirmInput.text)) {
                    val loginCreated = JsonDbHelper.createLogin(
                        newLoginInput.text.toString(),
                        newPasswordConfirmInput.text.toString()
                    )

                    if (loginCreated) {
                        Snackbar
                            .make(view, "New login created successfully", Snackbar.LENGTH_SHORT)
                            .show()
                        findNavController().popBackStack()
                    } else {
                        Snackbar
                            .make(view, "Error : Login already exists", Snackbar.LENGTH_SHORT)
                            .show()
                    }
                } else {
                    newPasswordLayout.invalidate("Passwords not matching")
                    newPasswordConfirmLayout.invalidate("Passwords not matching")
                }
            }
        }
    }
}