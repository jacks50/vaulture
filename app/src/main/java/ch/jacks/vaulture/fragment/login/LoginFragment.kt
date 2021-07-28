package ch.jacks.vaulture.fragment.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ch.jacks.vaulture.R
import ch.jacks.vaulture.db.dao.LoginDao
import ch.jacks.vaulture.util.MyTextUtil
import ch.jacks.vaulture.util.SessionUtil
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.login_fragment.*

class LoginFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.login_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListeners(view)

        MyTextUtil.setupFieldValidation(mapOf(
                loginInput to loginLayout,
                passwordInput to passwordLayout
        ))
    }

    private fun setupListeners(view: View) {
        btLogin.setOnClickListener {
            if (MyTextUtil.fieldsAreValid()) {
                var currentLogin = LoginDao.getLogin(loginInput.text.toString(), passwordInput.text.toString())

                if (currentLogin != null) {
                    Snackbar.make(it, "Welcome back ${currentLogin.loginUsername} !", Snackbar.LENGTH_SHORT).show()
                    SessionUtil.currentLoginId = currentLogin.loginId
                    findNavController().navigate(R.id.action_LoginFragment_to_PasswordListFragment)
                } else {
                    Snackbar.make(it, "Wrong username and/or password", Snackbar.LENGTH_SHORT).show()
                }
            }
        }

        newAccountText.setOnClickListener {
            findNavController().navigate(R.id.action_LoginFragment_to_NewLoginFragment)
        }
    }
}