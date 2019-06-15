package io.wax911.support.core.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.Transformations.map
import androidx.lifecycle.ViewModel
import io.wax911.support.core.viewmodel.contract.ISupportViewModel
import io.wax911.support.data.model.NetworkState
import io.wax911.support.data.model.UiModel
import io.wax911.support.data.repository.SupportRepository
import io.wax911.support.data.repository.contract.ISupportRepository

abstract class SupportViewModel<M, P>(
    private val repository : ISupportRepository<M, P>
) : ViewModel(), ISupportViewModel<M, P> {

    private val requestBundleLiveData = MutableLiveData<P>()

    private val repositoryResult: LiveData<UiModel<M>> =
        map(requestBundleLiveData) { repository(it) }

    override val model: LiveData<M?> =
        Transformations.switchMap(repositoryResult) { it.model }


    override val networkState: LiveData<NetworkState>? =
        Transformations.switchMap(repositoryResult) { it.networkState }


    override val refreshState: LiveData<NetworkState>? =
        Transformations.switchMap(repositoryResult) { it.refreshState }

    /**
     * Forwards queries for the repository to handle
     *
     * @see [io.wax911.support.data.repository.SupportRepository.invoke]
     * @param parameter request data to be used by the repository
     */
    override fun invoke(parameter: P) {
        requestBundleLiveData.value = parameter
    }

    /**
     * This method will be called when this ViewModel is no longer used and will be destroyed.
     *
     * It is useful when ViewModel observes some data and you need to clear this subscription to
     * prevent a leak of this ViewModel.
     */
    override fun onCleared() {
        repository.onCleared()
        super.onCleared()
    }

    /**
     * Requests the repository to perform a retry operation
     */
    override fun retry() {
        val uiModel = repositoryResult.value
        uiModel?.retry?.invoke()
    }

    /**
     * Returns the current request bundle, this is nullable
     */
    override fun currentRequestParameter(): P? = requestBundleLiveData.value

    /**
     * Requests the repository to perform a refresh operation on the underlying database
     */
    override fun refresh() {
        val uiModel = repositoryResult.value
        uiModel?.refresh?.invoke()
    }
}
