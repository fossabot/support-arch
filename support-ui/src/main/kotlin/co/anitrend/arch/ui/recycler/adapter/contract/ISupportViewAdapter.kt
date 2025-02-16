package co.anitrend.arch.ui.recycler.adapter.contract

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.annotation.LayoutRes
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import co.anitrend.arch.theme.animator.contract.ISupportAnimator
import co.anitrend.arch.domain.entities.NetworkState
import co.anitrend.arch.ui.action.contract.ISupportActionMode
import co.anitrend.arch.ui.recycler.common.SupportFooterErrorViewHolder
import co.anitrend.arch.ui.recycler.common.SupportFooterLoadingViewHolder
import co.anitrend.arch.ui.recycler.holder.SupportViewHolder
import co.anitrend.arch.ui.util.SupportStateLayoutConfiguration
import java.util.*

/**
 * Contract for view adapters
 */
interface ISupportViewAdapter<T> : Filterable {

     val moduleTag: String

    /**
     * Invokes a filter to search for data on the current adapter
     */
    val searchQuery: MutableLiveData<String>

    var lastAnimatedPosition: Int

    /**
     * Get currently set animation type for recycler view holder items
     *
     * @see [ISupportAnimator]
     */
    var customSupportAnimator: ISupportAnimator?

    /**
     * Retry click interceptor for recycler footer error
     */
    var retryFooterAction: View.OnClickListener
    var stateConfiguration: SupportStateLayoutConfiguration

    /**
     * Assigned if the current adapter supports needs to support [ISupportActionMode]
     */
    var supportAction: ISupportActionMode<T>?

    /**
     * Network state which will be used by [SupportFooterLoadingViewHolder] or [SupportFooterErrorViewHolder]
     */
    var networkState: NetworkState?

    /**
     * Used to get stable ids for [androidx.recyclerview.widget.RecyclerView.Adapter] but only if
     * [androidx.recyclerview.widget.RecyclerView.Adapter.setHasStableIds] is set to true.
     *
     * The identifiable id of each item should unique, and if non exists
     * then this function should return [androidx.recyclerview.widget.RecyclerView.NO_ID]
     */
    fun getStableIdFor(item: T?): Long

    /**
     * Should provide the required view holder, this function is a substitute for [onCreateViewHolder] which now
     * has extended functionality
     */
    fun createDefaultViewHolder(
        parent: ViewGroup, @LayoutRes viewType: Int, layoutInflater: LayoutInflater
    ): SupportViewHolder<T>

    /**
     * Returns a boolean indicating whether or not the adapter had data, and caters for [hasExtraRow]
     *
     * @return [Boolean]
     * @see hasExtraRow
     */
    fun isEmpty(): Boolean

    /**
     * Checks if current network state represents an additional row of data
     */
    fun hasExtraRow() = networkState != null &&
            (networkState is NetworkState.Loading || networkState is NetworkState.Error)

    /**
     * Returns a boolean to instruct the [GridLayoutManager] if an item at the position should
     * use a span size count of 1 otherwise defaults to the intended size
     *
     * @param position recycler position being rendered
     * @see setLayoutSpanSize
     */
    fun isFullSpanItem(position: Int): Boolean

    /**
     * Initial implementation is only specific for group types of recyclers,
     * in order to customize this an override is required.
     *
     * @param layoutManager grid layout manage for your recycler
     */
    fun setLayoutSpanSize(layoutManager: GridLayoutManager) {
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int = when {
                isFullSpanItem(position) -> 1
                else -> layoutManager.spanCount
            }
        }
    }


    fun animateViewHolder(holder: SupportViewHolder<T>?, position: Int) {
        holder?.apply {
            when (position > lastAnimatedPosition) {
                true -> customSupportAnimator?.also { supportAnimator ->
                    supportAnimator.getAnimators(itemView).forEach { animator ->
                        with(animator) {
                            duration = supportAnimator.getAnimationDuration().duration
                            interpolator = supportAnimator.interpolator
                            start()
                        }
                    }
                }
            }
            lastAnimatedPosition = position
        }
    }

    /**
     * Initial implementation is only specific for group types of recyclers,
     * in order to customize this an override is required.
     *
     * @param layoutParams StaggeredGridLayoutManager.LayoutParams for your recycler
     */
    fun setLayoutSpanSize(layoutParams: StaggeredGridLayoutManager.LayoutParams, position: Int) {
        if (isFullSpanItem(position))
            layoutParams.isFullSpan = true
    }

    /**
     * Informs view adapter of changes related to it's view holder
     */
    fun updateSelection()

    /**
     * Returns a filter that can be used to constrain data with a filtering
     * pattern.
     *
     * This method is usually implemented by [android.widget.Adapter]
     * classes.
     *
     * @return a filter used to constrain data
     */
    override fun getFilter(): Filter = object : Filter() {

        /**
         *
         * Invoked in a worker thread to filter the data according to the
         * constraint. Subclasses must implement this method to perform the
         * filtering operation. Results computed by the filtering operation
         * must be returned as a [android.widget.Filter.FilterResults] that
         * will then be published in the UI thread through
         * [.publishResult].
         *
         *
         * **Contract:** When the constraint is null, the original
         * data must be restored.
         *
         * @param constraint the constraint used to filter the data
         * @return the results of the filtering operation
         *
         * @see .filter
         * @see .publishResult
         * @see android.widget.Filter.FilterResults
         */
        override fun performFiltering(constraint: CharSequence?) = FilterResults().apply {
            values = Collections.EMPTY_LIST
        }

        /**
         * Invoked in the UI thread to publish the filtering results in the
         * user interface. Subclasses must implement this method to display the
         * results computed in [.performFiltering].
         *
         * @param constraint the constraint used to filter the data
         * @param results the results of the filtering operation
         *
         * @see .filter
         * @see .performFiltering
         * @see android.widget.Filter.FilterResults
         */
        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {

        }
    }

    companion object {

        /**
         * Provides default behaviour for item callback to compare objects
         */
        fun <T> getDefaultDiffItemCallback(): DiffUtil.ItemCallback<T> {
            return object : DiffUtil.ItemCallback<T>() {
                /**
                 * Called to check whether two objects represent the same item.
                 *
                 *
                 * For example, if your items have unique ids, this method should check their id equality.
                 *
                 *
                 * Note: `null` items in the list are assumed to be the same as another `null`
                 * item and are assumed to not be the same as a non-`null` item. This callback will
                 * not be invoked for either of those cases.
                 *
                 * @param oldItem The item in the old list.
                 * @param newItem The item in the new list.
                 * @return True if the two items represent the same object or false if they are different.
                 *
                 * @see Callback.areItemsTheSame
                 */
                override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
                    return oldItem?.equals(newItem) ?: false
                }

                /**
                 * Called to check whether two items have the same data.
                 *
                 *
                 * This information is used to detect if the contents of an item have changed.
                 *
                 *
                 * This method to check equality instead of [Object.equals] so that you can
                 * change its behavior depending on your UI.
                 *
                 *
                 * For example, if you are using DiffUtil with a
                 * [RecyclerView.Adapter], you should
                 * return whether the items' visual representations are the same.
                 *
                 *
                 * This method is called only if [.areItemsTheSame] returns `true` for
                 * these items.
                 *
                 *
                 * Note: Two `null` items are assumed to represent the same contents. This callback
                 * will not be invoked for this case.
                 *
                 * @param oldItem The item in the old list.
                 * @param newItem The item in the new list.
                 * @return True if the contents of the items are the same or false if they are different.
                 *
                 * @see Callback.areContentsTheSame
                 */
                override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
                    return oldItem.hashCode() == newItem.hashCode()
                }
            }
        }
    }
}