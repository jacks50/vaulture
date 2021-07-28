package ch.jacks.vaulture.fragment.password

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ch.jacks.vaulture.R
import ch.jacks.vaulture.db.dao.PasswordDao
import ch.jacks.vaulture.util.SessionUtil
import kotlinx.android.synthetic.main.new_password_fragment.*

class NewPasswordFragment : Fragment() {
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.new_password_fragment, container, false)
    }

    private fun savePassword() {
        PasswordDao.createPassword(
                newPwdNameInput.text.toString(),
                newPwdUsernameInput.text.toString(),
                newPwdUrlInput.text.toString(),
                newPwdPwdInput.text.toString(),
                SessionUtil.currentLoginId
        )
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_pwd, menu)
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

    companion object {
        const val TAG = "PwdDialog"
    }
}