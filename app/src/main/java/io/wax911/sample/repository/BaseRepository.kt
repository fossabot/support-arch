package io.wax911.sample.repository

import android.content.Context
import android.os.Bundle
import io.wax911.sample.dao.BaseModelDao
import io.wax911.sample.dao.DatabaseHelper
import io.wax911.sample.model.BaseModel
import io.wax911.support.base.dao.SupportRepository
import io.wax911.support.util.InstanceUtil
import io.wax911.support.util.SupportStateUtil
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

class BaseRepository private constructor(): SupportRepository<Long, BaseModel?>() {

    override fun find(key: Long) = (modelDao as BaseModelDao).get(key)

    /**
     * Creates the network client for implementing class using the given parameters
     *
     * @param bundle bundle of parameters for the request
     */
    override fun createNetworkClientRequest(bundle: Bundle, context: Context): Deferred<Unit> = GlobalScope.async {
        when (bundle.getString(SupportStateUtil.arg_bundle)) {

        }
    }

    /**
     * When the application is not connected to the internet this method is called to resolve the
     * kind of content that needs to be fetched from the database
     */
    override fun requestFromCache(bundle: Bundle, context: Context) = GlobalScope.async {
        when (bundle.getString(SupportStateUtil.arg_bundle)) {

        }
    }

    companion object : InstanceUtil<BaseRepository, DatabaseHelper>({
       BaseRepository().also { repo -> repo.modelDao = it.baseModelDao() }
    })
}
