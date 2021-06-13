package ch.jacks.vaulture.fragment.password

import android.app.Activity.RESULT_OK
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import ch.jacks.vaulture.AbstractMainFragment
import ch.jacks.vaulture.R
import ch.jacks.vaulture.adapter.PasswordAdapter
import ch.jacks.vaulture.app.VaultureApp
import ch.jacks.vaulture.db.dao.PasswordDao
import ch.jacks.vaulture.db.entity.PasswordEntity
import ch.jacks.vaulture.listener.ISheetListener
import ch.jacks.vaulture.menu.MainMenuSheet
import ch.jacks.vaulture.menu.PasswordMenuSheet
import ch.jacks.vaulture.util.PasswordImportUtil
import ch.jacks.vaulture.util.SessionUtil
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.password_list_fragment.*
import java.io.InputStream

class PasswordListFragment: ISheetListener, AbstractMainFragment() {
    // region Variables
    private var rootView: View? = null
    private var selectedPassword: PasswordEntity? = null
    private var passwordListAdapter: PasswordAdapter = PasswordAdapter(PasswordDao.getPasswords(SessionUtil.currentLoginId)) { onPasswordSelected(it) }
    private var mmSheet: MainMenuSheet = MainMenuSheet(this)
    private var pmSheet: PasswordMenuSheet = PasswordMenuSheet(this)
    // endregion

    // region Fragment lifecycle functions
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.password_list_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rootView = view
        setupListeners()
    }

    override fun onResume() {
        super.onResume()
        passwordListAdapter.updateDataSet()
    }
    // endregion

    // region Menu options creation
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
    }

    override fun setupListeners() {
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

    override fun callback(any: Any?) {
        when (any) {
            // region Main  menu controls
            MainMenuSheet.ADD_KEY -> {
                findNavController().navigate(R.id.action_ListFragment_to_NewPasswordFragment)
            }
            // endregion

            // region Password menu controls
            PasswordMenuSheet.EDIT_KEY -> {
                val bundle = bundleOf(Pair("pwd_id", selectedPassword!!.passwordId))
                findNavController().navigate(R.id.action_ListFragment_to_EditPasswordFragment, bundle)
            }
            PasswordMenuSheet.COPY_URL_KEY -> {
                var clipboard: ClipboardManager = requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                var clipData: ClipData = ClipData.newPlainText("URL", selectedPassword!!.passwordURL)
                clipboard.setPrimaryClip(clipData)
                Snackbar.make(rootView!!, "URL copied", Snackbar.LENGTH_SHORT).show()
            }
            PasswordMenuSheet.COPY_PWD_KEY -> {
                var clipboard: ClipboardManager = requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                var clipData: ClipData = ClipData.newPlainText("URL", selectedPassword!!.passwordValue)
                clipboard.setPrimaryClip(clipData)
                Snackbar.make(rootView!!, "Password copied", Snackbar.LENGTH_SHORT).show()
            }
            PasswordMenuSheet.SHOW_PWD_KEY -> {
                // TODO
            }
            PasswordMenuSheet.DELETE_KEY -> {
                MaterialAlertDialogBuilder(requireActivity())
                        .setMessage("Are you sure you want to delete this password ?")
                        .setNegativeButton("No") { _, _  -> }
                        .setPositiveButton("Yes") { _, _ ->
                            if(PasswordDao.deletePassword(selectedPassword!!.passwordId)) {
                                Snackbar.make(rootView!!, "Password deleted successfully", Snackbar.LENGTH_SHORT).show()
                                selectedPassword = null
                                passwordListAdapter.updateDataSet()
                            }
                        }
                        .show()
            }
            // endregion
            else -> {}
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.searchMenu -> {
                
            }
            R.id.logOutMenu -> {
                MaterialAlertDialogBuilder(requireActivity())
                        .setMessage("Are you sure you want to log out ?")
                        .setNegativeButton("No") { _, _  -> }
                        .setPositiveButton("Yes") { _, _ ->
                            findNavController().popBackStack()
                        }
                        .show()
            }
            R.id.generatePwdMenu -> {
                Snackbar.make(rootView!!, "Not implemented yet !", Snackbar.LENGTH_SHORT).show()
            }
            R.id.importPwdMenu -> {
                val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                    addCategory(Intent.CATEGORY_OPENABLE)
                    type = "*/*"
                }

                startActivityForResult(Intent.createChooser(intent, "Open CSV"), FILE_CHOOSER_REQ_CODE)
            }
            R.id.settingsMenu -> {
                Snackbar.make(rootView!!, "Not implemented yet !", Snackbar.LENGTH_SHORT).show()
            }
            R.id.aboutMenu -> {
                Snackbar.make(rootView!!, "Not implemented yet !", Snackbar.LENGTH_SHORT).show()
            }
            else -> {}
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when(requestCode) {
            FILE_CHOOSER_REQ_CODE -> {
                if (resultCode == RESULT_OK && data != null) {
                    var inputStream: InputStream? = VaultureApp.appContext.contentResolver.openInputStream(data.data!!)

                    PasswordImportUtil.readPasswordFile(VaultureApp.appContext.cacheDir, inputStream!!)
                    passwordListAdapter.updateDataSet()
                    Snackbar.make(rootView!!, "Passwords imported successfully", Snackbar.LENGTH_SHORT).show()
                }
            }
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

    companion object {
        const val FILE_CHOOSER_REQ_CODE = 100
    }
}