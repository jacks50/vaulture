package ch.jacks.vaulture.fragment.password

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ch.jacks.vaulture.R
import ch.jacks.vaulture.db.dao.PasswordDao
import ch.jacks.vaulture.db.entity.PasswordEntity
import ch.jacks.vaulture.dialog.PasswordGenerateDialog
import ch.jacks.vaulture.util.SessionUtil
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.crud_password_fragment.*

class CrudPasswordFragment : Fragment() {
    private lateinit var rootView: View
    private var selectedPwdId: Long = -1

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

        arguments?.let {
            selectedPwdId = it.get("pwd_id") as Long

            val pwd: PasswordEntity? = PasswordDao.getPassword(selectedPwdId)

            if (pwd != null) {
                pwdNameInput.setText(pwd.passwordName)
                pwdUrlInput.setText(pwd.passwordURL)
                pwdUsernameInput.setText(pwd.passwordUsername)
                pwdPwdInput.setText(pwd.passwordValue)
            }
        }

        fabAdd.setOnClickListener {
            if (selectedPwdId != -1L)
                editPassword()
            else
                savePassword()

            findNavController().popBackStack()
        }

        fabCancel.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.savePwd -> {
                if (selectedPwdId != -1L)
                    editPassword()
                else
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
        PasswordDao.createPassword(
            pwdNameInput.text.toString(),
            pwdUsernameInput.text.toString(),
            pwdUrlInput.text.toString(),
            pwdPwdInput.text.toString(),
            SessionUtil.currentLoginId
        )

        Snackbar.make(rootView, "Password saved successfully", Snackbar.LENGTH_SHORT).show()
    }

    private fun editPassword() {
        PasswordDao.editPassword(
            pwdNameInput.text.toString(),
            pwdUsernameInput.text.toString(),
            pwdUrlInput.text.toString(),
            pwdPwdInput.text.toString(),
            selectedPwdId
        )

        Snackbar.make(rootView, "Password edited successfully", Snackbar.LENGTH_SHORT).show()
    }

    companion object {
        const val TAG = "EditPwdDialog"
    }
}