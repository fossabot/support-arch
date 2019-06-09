package io.wax911.sample.data.mapper.show

import androidx.paging.PagingRequestHelper
import io.wax911.sample.data.dao.query.ShowDao
import io.wax911.sample.data.model.container.Aniticipated
import io.wax911.sample.data.model.show.Show
import io.wax911.support.data.source.mapper.SupportPagingDataMapper
import retrofit2.Response
import timber.log.Timber

class AnticipatedShowMapper(
    private val showDao: ShowDao,
    pagingRequestHelper: PagingRequestHelper.Request.Callback
) : SupportPagingDataMapper<List<Aniticipated<Show>>, List<Show>>(pagingRequestHelper) {

    /**
     * Created mapped objects and handles the database operations which may be required to map various objects,
     * called in [retrofit2.Callback.onResponse] after assuring that the response was a success
     * @see [responseCallback]
     *
     * @param response retrofit response containing data
     * @return Mapped object that will be consumed by [onResponseDatabaseInsert]
     */
    override suspend fun onResponseMapFrom(response: Response<List<Aniticipated<Show>>?>): List<Show> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /**
     * Inserts the given object into the implemented room database,
     * called in [retrofit2.Callback.onResponse]
     * @see [responseCallback]
     *
     * @param mappedData mapped object from [onResponseMapFrom] to insert into the database
     */
    override suspend fun onResponseDatabaseInsert(mappedData: List<Show>) {
        if (mappedData.isNotEmpty())
            showDao.insert(*mappedData.toTypedArray())
        else
            Timber.tag(moduleTag).i("onResponseDatabaseInsert(mappedData: List<Show>) -> mappedData is empty")
    }
}