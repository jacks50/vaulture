package ch.jacks.vaulture.fragment.password

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ch.jacks.vaulture.R
import ch.jacks.vaulture.db.dao.PasswordDao
import ch.jacks.vaulture.db.entity.PasswordEntity
import kotlinx.android.synthetic.main.edit_password_fragment.*

class EditPasswordFragment : Fragment() {
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.edit_password_fragment, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_pwd, menu)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var pwd: PasswordEntity? = PasswordDao.getPassword(arguments?.get("pwd_id") as Long)

        if (pwd != null) {
            editPwdNameInput.setText(pwd.passwordName)
            editPwdUrlInput.setText(pwd.passwordURL)
            editPwdUsernameInput.setText(pwd.passwordUsername)
            editPwdPwdInput.setText(pwd.passwordValue)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.savePwd -> {
                savePassword()
                findNavController().popBackStack()
                true
            }

            R.id.cancelPwd -> {
                findNavController().popBackStack()
                true
            }

            else -> false
        }
    }

    private fun savePassword() {
        PasswordDao.editPassword(
                editPwdNameInput.text.toString(),
                editPwdUsernameInput.text.toString(),
                editPwdUrlInput.text.toString(),
                editPwdPwdInput.text.toString(),
                arguments?.get("pwd_id") as Long
        )
    }

    companion object {
        const val TAG = "EditPwdDialog"
    }
}