package io.wax911.support.data.util

import androidx.paging.PagedList
import io.wax911.support.extension.util.SupportExtKeyStore.pagingLimit

object SupportDataKeyStore {

    val PAGING_CONFIGURATION = PagedList.Config.Builder()
        .setEnablePlaceholders(false)
        .setPageSize(pagingLimit)
        .setPrefetchDistance(15)
        .setMaxSize(75)
        .build()
}