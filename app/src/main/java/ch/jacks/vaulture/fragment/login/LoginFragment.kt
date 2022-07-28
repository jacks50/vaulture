package ch.jacks.vaulture.fragment.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import ch.jacks.vaulture.R
import ch.jacks.vaulture.abs.AbsMainFragment
import ch.jacks.vaulture.app.VaultureApp
import ch.jacks.vaulture.db.JsonDbHelper
import ch.jacks.vaulture.util.MyTextUtil.setCustomFocusChangeListener
import ch.jacks.vaulture.util.SessionUtil
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.login_fragment.*
import kotlinx.coroutines.CoroutineExceptionHandler

class LoginFragment : AbsMainFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.login_fragment, container, false)
    }

    override fun setupUIComponents() {
        if (SessionUtil.rememberMe) {
            cbRememberMe.isChecked = true
            loginInput.setText(SessionUtil.lastLogin)
        }

        addToValidation(
            loginLayout,
            passwordLayout
        )
    }

    override fun setupListeners(view: View) {
        loginLayout.setCustomFocusChangeListener()
        passwordLayout.setCustomFocusChangeListener()

        cbRememberMe.setOnClickListener {
            SessionUtil.rememberMe = cbRememberMe.isChecked
        }

        btLogin.setOnClickListener { lView ->
            if (fieldsAreValid()) {
                showProgressBar(loginMainLayout, true)

                val currentLogin = loginInput.text.toString()
                val currentPassword = passwordInput.text.toString()

                JsonDbHelper.getLogin(currentLogin)?.let {
                    startBackgroundTask({
                        JsonDbHelper.loadFile(it, currentPassword)

                        SessionUtil.lastLogin = if (cbRememberMe.isChecked) currentLogin else ""

                        SessionUtil.currentLogin = currentLogin
                        VaultureApp.currentPwd = currentPassword

                        showProgressBar(loginMainLayout, false)

                        Snackbar.make(
                            lView, "Welcome back $currentLogin !", Snackbar.LENGTH_SHORT
                        ).show()

                        findNavController().navigate(
                            R.id.action_LoginFragment_to_PasswordListFragment
                        )
                    }, CoroutineExceptionHandler { _, _ ->
                        showProgressBar(loginMainLayout, false)
                        Snackbar
                            .make(lView, "Wrong username and/or password", Snackbar.LENGTH_SHORT)
                            .show()
                    })
                } ?: run {
                    Snackbar
                        .make(lView, "Wrong username and/or password", Snackbar.LENGTH_SHORT)
                        .show()

                    showProgressBar(loginMainLayout, false)
                }
            }
        }

        newAccountText.setOnClickListener {
            findNavController().navigate(R.id.action_LoginFragment_to_NewLoginFragment)
        }
    }
}