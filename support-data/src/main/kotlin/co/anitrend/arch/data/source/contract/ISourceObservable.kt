package co.anitrend.arch.data.source.contract

import androidx.lifecycle.LiveData

/**
 *
 *
 * @since v1.1.0
 */
interface ISourceObservable<O, P> {

    /**
     * Returns the appropriate observable which we will monitor for updates,
     * common implementation may include but not limited to returning
     * data source live data for a database
     *
     * @param parameter to use when executing
     */
    operator fun invoke(parameter: P): LiveData<O>
}