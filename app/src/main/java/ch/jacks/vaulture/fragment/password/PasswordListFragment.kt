package ch.jacks.vaulture.fragment.password

import android.app.Activity.RESULT_OK
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.SearchView
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import ch.jacks.vaulture.R
import ch.jacks.vaulture.abs.AbsMainFragment
import ch.jacks.vaulture.adapter.PasswordAdapter
import ch.jacks.vaulture.app.VaultureApp
import ch.jacks.vaulture.db.JsonDbHelper
import ch.jacks.vaulture.db.entity.PasswordEntity
import ch.jacks.vaulture.dialog.PasswordGenerateDialog
import ch.jacks.vaulture.menu.MainMenuSheet
import ch.jacks.vaulture.menu.PasswordMenuSheet
import ch.jacks.vaulture.util.MyTextUtil
import ch.jacks.vaulture.util.PasswordDbUtil
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.password_list_fragment.*
import java.io.InputStream

class PasswordListFragment : AbsMainFragment() {
    // region Variables
    private var selectedPassword: PasswordEntity? = null
    private var passwordListAdapter =
        PasswordAdapter(JsonDbHelper.getPasswords()) { onPasswordSelected(it) }
    private var mmSheet: MainMenuSheet = MainMenuSheet(::mainMenuCallback)
    private var pmSheet: PasswordMenuSheet = PasswordMenuSheet(::passwordMenuCallback)
    // endregion

    // region Fragment lifecycle functions
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.password_list_fragment, container, false)
    }

    override fun onResume() {
        super.onResume()
        passwordListAdapter.updateDataSet()
    }
    // endregion

    // region Menu options creation
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)

        val searchView = SearchView(requireActivity())
        menu.findItem(R.id.searchMenu).apply {
            setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW or MenuItem.SHOW_AS_ACTION_IF_ROOM)
            actionView = searchView
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                passwordListAdapter.filterDataSet(newText)
                return true
            }
        })
    }

    override fun setupListeners(view: View) {
        fabAdd.setOnClickListener {
            mmSheet.show(requireActivity().supportFragmentManager, MainMenuSheet.TAG)
        }

        passwordList.adapter = passwordListAdapter
    }
    // endregion

    private fun onPasswordSelected(selectedPassword: PasswordEntity) {
        this.selectedPassword = selectedPassword

        pmSheet.show(requireActivity().supportFragmentManager, MainMenuSheet.TAG)
    }

    private fun mainMenuCallback(key: String) {
        when (key) {
            // region Main menu controls
            MainMenuSheet.ADD_KEY -> {
                findNavController().navigate(R.id.action_ListFragment_to_CrudPasswordFragment)
            }
            // endregion
            else -> {
            }
        }
    }

    private fun passwordMenuCallback(key: String) {
        when (key) {
            // region Password menu controls
            PasswordMenuSheet.EDIT_KEY -> {
                val bundle = bundleOf(Pair("pwd_id", selectedPassword!!.passwordUID))
                findNavController().navigate(
                    R.id.action_ListFragment_to_CrudPasswordFragment,
                    bundle
                )
            }

            PasswordMenuSheet.COPY_USERNAME_KEY -> {
                val clipboard: ClipboardManager =
                    requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clipData: ClipData =
                    ClipData.newPlainText("Username", selectedPassword!!.passwordUsername)
                clipboard.setPrimaryClip(clipData)

                Snackbar
                    .make(rootView, "Username copied", Snackbar.LENGTH_SHORT)
                    .show()
            }

            PasswordMenuSheet.COPY_PWD_KEY -> {
                val clipboard: ClipboardManager =
                    requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clipData: ClipData =
                    ClipData.newPlainText("Password", selectedPassword!!.passwordValue)
                clipboard.setPrimaryClip(clipData)

                Snackbar
                    .make(rootView, "Password copied", Snackbar.LENGTH_SHORT)
                    .show()
            }

            PasswordMenuSheet.SHOW_PWD_KEY -> {
                val pwdValue: String = selectedPassword!!.passwordValue

                MaterialAlertDialogBuilder(
                    requireActivity(),
                    R.style.MaterialAlertDialog__Center
                )
                    .setMessage(MyTextUtil.colorizeText(pwdValue))
                    .setPositiveButton("Close") { _, _ -> }
                    .show()
            }

            PasswordMenuSheet.DELETE_KEY -> {
                MaterialAlertDialogBuilder(requireActivity())
                    .setMessage("Are you sure you want to delete this password ?")
                    .setNegativeButton("No") { _, _ -> }
                    .setPositiveButton("Yes") { _, _ ->
                        if (JsonDbHelper.deletePassword(selectedPassword!!.passwordUID)) {
                            Snackbar
                                .make(
                                    rootView,
                                    "Password deleted successfully",
                                    Snackbar.LENGTH_SHORT
                                )
                                .show()
                            selectedPassword = null
                            passwordListAdapter.updateDataSet()
                        }
                    }
                    .show()
            }
            // endregion
            else -> {
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.generatePwdMenu -> {
                PasswordGenerateDialog(rootView)
                    .show(requireActivity().supportFragmentManager, "PWD_GEN_DIALOG")
            }
            R.id.importPwdMenu -> {
//                val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
//                    addCategory(Intent.CATEGORY_OPENABLE)
//                    type = "*/*"
//                }
//
//                startActivityForResult(Intent.createChooser(intent, "Open CSV"), FILE_CHOOSER_REQ_CODE)
//                PasswordExportUtil.importJson()
                Snackbar.make(rootView, "Passwords imported !", Snackbar.LENGTH_SHORT).show()
            }
            R.id.exportPwdMenu -> {
//                PasswordExportUtil.exportJson(requireContext())
                Snackbar.make(rootView, "Passwords exported !", Snackbar.LENGTH_SHORT).show()
            }
            R.id.settingsMenu -> {
                Snackbar.make(rootView, "Not implemented yet !", Snackbar.LENGTH_SHORT).show()
            }
            R.id.aboutMenu -> {
                Snackbar.make(rootView, "Not implemented yet !", Snackbar.LENGTH_SHORT).show()
            }
            else -> {
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            FILE_CHOOSER_REQ_CODE -> {
                if (resultCode == RESULT_OK && data != null) {
                    var inputStream: InputStream? =
                        VaultureApp.appContext.contentResolver.openInputStream(data.data!!)

                    PasswordDbUtil.readPasswordFile(
                        VaultureApp.appContext.cacheDir,
                        inputStream!!
                    )
                    passwordListAdapter.updateDataSet()
                    Snackbar.make(
                        rootView,
                        "Passwords imported successfully",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

    companion object {
        const val FILE_CHOOSER_REQ_CODE = 100
    }
}