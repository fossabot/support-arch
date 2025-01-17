package io.wax911.sample.data.util

import android.content.Context
import androidx.core.content.edit
import co.anitrend.arch.extension.preference.SupportPreference

class Settings(context: Context) : SupportPreference(context) {

    var authenticatedUserId: Long = INVALID_USER_ID
        get() = sharedPreferences.getLong(AUTHENTICATED_USER, -1)
        set(value) {
            field = value
            sharedPreferences.edit {
                putLong(AUTHENTICATED_USER, value)
                apply()
            }
        }

    companion object  {
        const val INVALID_USER_ID: Long = -1
        private const val AUTHENTICATED_USER = "_authenticatedUser"
    }
}
