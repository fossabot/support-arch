package io.wax911.sample.core.repository.show

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.PagedList
import io.wax911.sample.core.api.endpoint.ShowEndpoint
import io.wax911.sample.core.data.source.ShowDataSource
import io.wax911.sample.core.model.show.Show
import io.wax911.support.core.factory.contract.IRetrofitFactory
import io.wax911.support.core.repository.SupportRepository
import io.wax911.support.core.view.model.NetworkState
import io.wax911.support.core.view.model.UiModel
import org.koin.core.inject

class ShowRepository(context: Context) : SupportRepository<PagedList<Show>>(context) {

    override val retroFactory: IRetrofitFactory by inject()

    /**
     * Handles dispatching of network requests to a background thread
     *
     * @param bundle bundle of parameters for the request
     */
    override fun invokeRequest(bundle: Bundle): UiModel<PagedList<Show>> {
        // create a boundary callback which will observe when the user reaches to the edges of
        // the list and update the database with extra data.
        val boundaryCallback = ShowDataSource(
            bundle = bundle,
            showEndpoint = retroFactory.createService(
                ShowEndpoint::class.java
            )
        )
        // we are using a mutable live data to trigger refresh requests which eventually calls
        // refresh method and gets a new live data. Each refresh request by the user becomes a newly
        // dispatched data in refreshTrigger
        val refreshTrigger = MutableLiveData<Unit>()
        val refreshState = Transformations.switchMap(refreshTrigger) {
            MutableLiveData<NetworkState>()
        }

        return UiModel(
            model = boundaryCallback.observerOnLiveDataWith(bundle),
            networkState = boundaryCallback.networkState,
            refresh = {
                boundaryCallback.refreshOrInvalidate()
                refreshTrigger.value = null
            },
            refreshState = refreshState,
            retry = {
                boundaryCallback.pagingRequestHelper.retryAllFailed()
            }
        )
    }
}