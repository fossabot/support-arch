package co.anitrend.arch.ui.extension

import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import co.anitrend.arch.extension.getColorFromAttr
import co.anitrend.arch.ui.R
import co.anitrend.arch.ui.recycler.SupportRecyclerView
import co.anitrend.arch.ui.recycler.adapter.SupportPagedListAdapter

/**
 * Sets up a recycler view by handling all the boilerplate code associated with it using
 * the given layout manager or the default.
 *
 * @param supportAdapter recycler view adapter which will be used
 * @param vertical if the layout adapter should be vertical or horizontal
 * @param recyclerLayoutManager optional layout manager if you do not wish to use the default
 *
 * @since v0.9.X
 */
fun SupportRecyclerView.setUpWith(supportAdapter: SupportPagedListAdapter<*>, vertical: Boolean = true,
                                  recyclerLayoutManager: RecyclerView.LayoutManager? = null) {
    setHasFixedSize(true)
    isNestedScrollingEnabled = true
    layoutManager = when (recyclerLayoutManager == null) {
        vertical ->
            StaggeredGridLayoutManager(
                context.resources.getInteger(R.integer.grid_list_x3),
                StaggeredGridLayoutManager.VERTICAL)
        !vertical ->
            StaggeredGridLayoutManager(
                context.resources.getInteger(R.integer.single_list_size),
                StaggeredGridLayoutManager.HORIZONTAL)
        else ->
            recyclerLayoutManager
    }
    adapter = supportAdapter
}

/**
 * This method applies the most common stateConfiguration for the widget, things like direction, colors, behavior etc.
 */
fun SwipeRefreshLayout.configureWidgetBehaviorWith(context: FragmentActivity?) = context?.also {
    setProgressBackgroundColorSchemeColor(it.getColorFromAttr(R.attr.rootColor))
    setColorSchemeColors(it.getColorFromAttr(R.attr.contentColor))
}

/**
 * Resets the refreshing or loading states when called, common use case would be after a network response
 */
fun SwipeRefreshLayout.onResponseResetStates() {
    if (isRefreshing) isRefreshing = false
}
