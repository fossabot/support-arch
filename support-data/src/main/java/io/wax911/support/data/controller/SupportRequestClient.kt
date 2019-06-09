package io.wax911.support.data.controller

import io.wax911.support.data.controller.contract.ISupportRequestClient
import retrofit2.Call
import timber.log.Timber

abstract class SupportRequestClient: ISupportRequestClient {

    protected val moduleTag: String = javaClass.simpleName

    protected val callList: MutableList<Call<*>> = ArrayList()

    /**
     * Cancels all the call requests that were used in the executeUsing function
     *
     * @see [io.wax911.support.data.controller.SupportRequestClient.executeUsingAsync]
     */
    override fun cancel() {
        try {
            callList.forEach { it.cancel() }
            callList.clear()
            cancelAllChildren()
        } catch (e: Exception) {
            Timber.e(e)
        }
    }
}
