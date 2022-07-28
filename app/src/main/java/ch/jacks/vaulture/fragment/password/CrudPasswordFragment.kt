package ch.jacks.vaulture.fragment.password

import android.os.Bundle
import android.view.*
import androidx.navigation.fragment.findNavController
import ch.jacks.vaulture.R
import ch.jacks.vaulture.abs.AbsMainFragment
import ch.jacks.vaulture.db.JsonDbHelper
import ch.jacks.vaulture.db.entity.PasswordEntity
import ch.jacks.vaulture.dialog.PasswordGenerateDialog
import ch.jacks.vaulture.util.MyTextUtil.setCustomFocusChangeListener
import ch.jacks.vaulture.util.SessionUtil
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.crud_password_fragment.*
import kotlinx.coroutines.CoroutineExceptionHandler

class CrudPasswordFragment : AbsMainFragment() {
    private var selectedPwdId: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.crud_password_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            selectedPwdId = it.get("pwd_id") as String

            val pwd: PasswordEntity? = JsonDbHelper.getPassword(selectedPwdId)

            if (pwd != null) {
                pwdNameInput.setText(pwd.passwordName)
                pwdUrlInput.setText(pwd.passwordURL)
                pwdUsernameInput.setText(pwd.passwordUsername)
                pwdPwdInput.setText(pwd.passwordValue)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_pwd, menu)
    }

    override fun setupUIComponents() {
        addToValidation(
            pwdNameLayout,
            pwdUsernameLayout,
            pwdPwdLayout
        )
    }

    override fun setupListeners(view: View) {
        pwdNameLayout.setCustomFocusChangeListener()
        pwdUrlLayout.setCustomFocusChangeListener()
        pwdUsernameLayout.setCustomFocusChangeListener()
        pwdPwdLayout.setCustomFocusChangeListener()

        btConfirm.setOnClickListener {
            if (fieldsAreValid()) {
                showProgressBar(crudPasswordLayout, true)

                startBackgroundTask({
                    if (selectedPwdId.isNotEmpty())
                        editPassword()
                    else
                        savePassword()

                    showProgressBar(crudPasswordLayout, false)

                    findNavController().popBackStack()
                }, CoroutineExceptionHandler { _, _ ->
                    showProgressBar(crudPasswordLayout, false)
                    Snackbar
                        .make(
                            rootView,
                            "Something went wrong when saving password",
                            Snackbar.LENGTH_SHORT
                        )
                        .show()
                })
            }
        }

        btCancel.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.genPwd -> {
                PasswordGenerateDialog(rootView)
                    .show(requireActivity().supportFragmentManager, "PWD_GEN_DIALOG")
                true
            }

            else -> false
        }
    }

    private fun savePassword() {
        JsonDbHelper.createPassword(
            PasswordEntity(
                -1L,
                pwdNameInput.text.toString(),
                pwdUsernameInput.text.toString(),
                pwdUrlInput.text.toString(),
                pwdPwdInput.text.toString(),
                SessionUtil.currentLoginId
            )
        )

        Snackbar.make(rootView, "Password saved successfully", Snackbar.LENGTH_SHORT).show()
    }

    private fun editPassword() {
        JsonDbHelper.editPassword(
            PasswordEntity(
                -1L,
                pwdNameInput.text.toString(),
                pwdUsernameInput.text.toString(),
                pwdUrlInput.text.toString(),
                pwdPwdInput.text.toString(),
                SessionUtil.currentLoginId,
                selectedPwdId
            )
        )

        Snackbar.make(rootView, "Password edited successfully", Snackbar.LENGTH_SHORT).show()
    }

    companion object {
        const val TAG = "EditPwdDialog"
    }
}