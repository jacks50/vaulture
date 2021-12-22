package ch.jacks.vaulture.fragment.password

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ch.jacks.vaulture.R
import ch.jacks.vaulture.db.dao.PasswordDao
import ch.jacks.vaulture.db.entity.PasswordEntity
import ch.jacks.vaulture.dialog.PasswordGenerateDialog
import kotlinx.android.synthetic.main.crud_password_fragment.*

class EditPasswordFragment : Fragment() {
    private lateinit var rootView: View

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.crud_password_fragment, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_pwd, menu)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rootView = view

        var pwd: PasswordEntity? = PasswordDao.getPassword(arguments?.get("pwd_id") as Long)

        if (pwd != null) {
            pwdNameInput.setText(pwd.passwordName)
            pwdUrlInput.setText(pwd.passwordURL)
            pwdUsernameInput.setText(pwd.passwordUsername)
            pwdPwdInput.setText(pwd.passwordValue)
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

            R.id.genPwd -> {
                PasswordGenerateDialog(rootView)
                    .show(requireActivity().supportFragmentManager, "PWD_GEN_DIALOG")
                true
            }

            else -> false
        }
    }

    private fun savePassword() {
        PasswordDao.editPassword(
                pwdNameInput.text.toString(),
                pwdUsernameInput.text.toString(),
                pwdUrlInput.text.toString(),
                pwdPwdInput.text.toString(),
                arguments?.get("pwd_id") as Long
        )
    }

    companion object {
        const val TAG = "EditPwdDialog"
    }
}