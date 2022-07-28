package ch.jacks.vaulture.abs

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import ch.jacks.vaulture.R
import ch.jacks.vaulture.util.MyTextUtil.validate
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

abstract class AbsMainFragment : Fragment() {
    // mutable list used to keep reference to fields that need to be validated
    private var validationFields: MutableList<TextInputLayout> = ArrayList()

    // circular progress indicator to be shown when starting heavy tasks
    private lateinit var indicator: LinearProgressIndicator
    private val indicatorLayout = ConstraintLayout.LayoutParams(
        ConstraintLayout.LayoutParams.MATCH_PARENT,
        ConstraintLayout.LayoutParams.WRAP_CONTENT
    )

    // reference to root view (for snackbars and other components
    protected lateinit var rootView: View

    /**
     * Override of the Fragment onViewCreated lifecycle function
     * Calls abstract functions to start the setup of the Fragment
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rootView = view

        validationFields.clear()

        indicator = LinearProgressIndicator(requireContext())
        indicator.isIndeterminate = true
        indicator.trackColor = ContextCompat.getColor(requireContext(), R.color.primary)
        indicator.setIndicatorColor(ContextCompat.getColor(requireContext(), R.color.secondary))

        indicatorLayout.topToTop = ConstraintSet.PARENT_ID
        indicatorLayout.leftToLeft = ConstraintSet.PARENT_ID
        indicatorLayout.rightToRight = ConstraintSet.PARENT_ID

        indicator.layoutParams = indicatorLayout

        setupUIComponents()
        setupListeners(view)
    }

    /**
     * Used to setup UI components such as fields, layouts and others
     * Called by onViewCreated of the Fragment automatically
     * Optional
     */
    open fun setupUIComponents() {}

    /**
     * Used to setup listeners on view components
     * Called by onViewCreated of the Fragment automatically
     * Optional
     */
    open fun setupListeners(view: View) {}

    /**
     * Adds a field to a list that can be later checked for validation
     */
    fun addToValidation(vararg fields: TextInputLayout) {
        fields.forEach {
            if (it !in validationFields)
                validationFields.add(it)
        }
    }

    /**
     * Removes a field to the validation list
     */
    fun removeFromValidation(vararg fields: TextInputLayout) {
        fields.forEach {
            validationFields.remove(it)
        }
    }

    /**
     * Checks if the list of fields provided are valid (= not empty)
     */
    fun fieldsAreValid(): Boolean {
        var valid = true

        validationFields.forEach {
            valid = it.validate() && valid
        }

        return valid
    }

    fun startBackgroundTask(
        work: () -> Unit,
        exceptionCallback: CoroutineExceptionHandler
    ): Boolean {
        GlobalScope.launch(exceptionCallback) {
            work()
        }

        return true
    }

    fun showProgressBar(mainLayout: ConstraintLayout, enable: Boolean) {
        requireActivity().runOnUiThread {
            if (enable) {
                mainLayout.addView(indicator)

                //loadingBar.visibility = View.VISIBLE
                requireActivity().window.setFlags(
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                )
            } else {
                mainLayout.removeView(indicator)

                //loadingBar.visibility = View.GONE
                requireActivity().window.clearFlags(
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                )
            }
        }
    }
}