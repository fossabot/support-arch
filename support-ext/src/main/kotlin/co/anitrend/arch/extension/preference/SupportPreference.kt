package co.anitrend.arch.extension.preference

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import co.anitrend.arch.extension.LAZY_MODE_UNSAFE
import co.anitrend.arch.extension.preference.contract.ISupportPreference

/**
 * Core abstract implementation for application preferences
 *
 * @since v0.9.X
 */
abstract class SupportPreference(protected val context: Context) : ISupportPreference {

    override val sharedPreferences: SharedPreferences by lazy(LAZY_MODE_UNSAFE) {
        PreferenceManager.getDefaultSharedPreferences(context)
    }

    override var isNewInstallation = true
        get() = sharedPreferences.getBoolean(ISupportPreference.IS_NEW_INSTALLATION, true)
        set(value) {
            field = value
            sharedPreferences.edit().putBoolean(ISupportPreference.IS_NEW_INSTALLATION, value).apply()
        }

    override var versionCode = 1
        get() = sharedPreferences.getInt(ISupportPreference.VERSION_CODE, 1)
        set(value) {
            field = value
            sharedPreferences.edit().putInt(ISupportPreference.VERSION_CODE, value).apply()
        }
}
