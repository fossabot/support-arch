package io.wax911.support.ui.activity

import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import io.wax911.support.core.view.contract.CompatView
import io.wax911.support.ui.fragment.SupportFragment
import io.wax911.support.core.presenter.SupportPresenter
import io.wax911.support.core.util.SupportCoroutineUtil
import io.wax911.support.core.viewmodel.SupportViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.greenrobot.eventbus.EventBus

abstract class SupportActivity<M, P : SupportPresenter<*>>: AppCompatActivity(), CompatView<M, P>,
    SupportCoroutineUtil {

    private var isClosing: Boolean = false

    protected var supportFragment : SupportFragment<*, *, *>? = null

    protected val presenter: P by lazy { initPresenter() }
    protected val viewModel: SupportViewModel<M?, *>? by lazy { initViewModel() }

    /**
     * Can be used to configure custom theme styling as desired
     */
    protected fun configureActivity() {
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_NO) {
                window.navigationBarColor = getCompatColor(R.color.colorPrimary)
                window.decorView.systemUiVisibility += View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
            }
        }*/
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        configureActivity()
        super.onCreate(savedInstanceState)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        initializeComponents(savedInstanceState)
    }

    override fun setSupportActionBar(toolbar: Toolbar?) {
        super.setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    fun disableToolbarTitle() = actionBar?.setDisplayShowTitleEnabled(false)

    protected fun setTransparentStatusBar() {
        if (Build.VERSION.SDK_INT >= 21) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            window.statusBarColor = ContextCompat.getColor(this, android.R.color.transparent)
        }
    }

    protected fun setTransparentStatusBarWithColor(@ColorRes color: Int) {
        if (Build.VERSION.SDK_INT >= 21) {
            val colorInt = ContextCompat.getColor(this, color)
            window.statusBarColor = colorInt
            window.navigationBarColor = colorInt
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home)
            super.onBackPressed()
        return super.onOptionsItemSelected(item)
    }

    /**
     * Check if the current fragment activity has a permission granted to it.
     * If no permission is granted then this method will request a permission for you
     * @see ActivityCompat#requestPermissions
     */
    override fun requestPermissionIfMissing(manifestPermission: String): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return true
        else if (ContextCompat.checkSelfPermission(this, manifestPermission) == PackageManager.PERMISSION_GRANTED)
            return true
        else if (!ActivityCompat.shouldShowRequestPermissionRationale(this, manifestPermission))
            ActivityCompat.requestPermissions(this, arrayOf(manifestPermission), compatViewPermissionValue)
        return false
    }

    /**
     * Dispatch onResume() to fragments.  Note that for better inter-operation
     * with older versions of the platform, at the point of this call the
     * fragments attached to the activity are *not* resumed.  This means
     * that in some cases the previous state may still be saved, not allowing
     * fragment transactions that modify the state.  To correctly interact
     * with fragments in their proper state, you should instead override
     * [.onResumeFragments].
     */
    override fun onResume() {
        super.onResume()
        if (shouldSubscribe()) {
            if (!EventBus.getDefault().isRegistered(this))
                EventBus.getDefault().register(this)
        }
    }

    /**
     * Dispatch onPause() to fragments.
     */
    override fun onPause() {
        if (shouldSubscribe()) {
            if (EventBus.getDefault().isRegistered(this))
                EventBus.getDefault().unregister(this)
        }
        super.onPause()
    }

    override fun onDestroy() {
        cancelAllChildren()
        super.onDestroy()
    }

    /**
     * Take care of popping the fragment back stack or finishing the activity
     * as appropriate.
     */
    override fun onBackPressed() {
        if (supportFragment?.hasBackPressableAction() == true)
            return
        return super.onBackPressed()
    }

    /**
     * Coroutine dispatcher specification
     *
     * @return [kotlinx.coroutines.Dispatchers.IO] by default
     */
    override val coroutineDispatcher: CoroutineDispatcher
        get() = Dispatchers.Default

    /**
     * Called when the data is changed.
     * @param data The new data
     */
    override fun onChanged(data: M?) {
        Log.i(getViewName(), "onChanged() from view liveData has received data")
    }

    override fun onSharedPreferenceChanged(preference: SharedPreferences?, key: String?) {
        if (!key.isNullOrEmpty())
            Log.d(getViewName(), "onSharedPreferenceChanged -> $key | Changed value")
    }
}