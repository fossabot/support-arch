package io.wax911.support.data.source

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import androidx.paging.PagingRequestHelper
import androidx.paging.extension.createStatusLiveData
import androidx.room.RoomDatabase
import io.wax911.support.data.source.contract.ISupportDataSource
import io.wax911.support.data.util.SupportDataKeyStore
import io.wax911.support.data.util.pagination.SupportPagingHelper
import io.wax911.support.extension.util.SupportExtKeyStore
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

abstract class SupportDataSource<T>(
    protected val bundle: Bundle
) : PagedList.BoundaryCallback<T>(), ISupportDataSource {

    protected abstract val databaseHelper: RoomDatabase

    val pagingRequestHelper = PagingRequestHelper(IO_EXECUTOR)

    val networkState = pagingRequestHelper.createStatusLiveData()

    protected val supportPagingHelper: SupportPagingHelper?
        get() = bundle.getParcelable(SupportExtKeyStore.key_pagination)

    interface IDataObservable <O> {

        /**
         * Returns the appropriate observable which we will monitor for updates,
         * common implementation may include but not limited to returning
         * data source live data for a database
         *
         * @param bundle request params, implementation is up to the developer
         */
        fun observerOnLiveDataWith(bundle: Bundle): LiveData<O>
    }

    companion object {
        val IO_EXECUTOR: ExecutorService = Executors.newSingleThreadExecutor()
    }
}