package co.anitrend.arch.extension

import android.content.res.Resources
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import androidx.annotation.StringRes
import co.anitrend.arch.extension.annotation.SupportExperimental
import com.google.android.material.snackbar.Snackbar
import kotlin.math.roundToInt


/**
 * Sets the current views visibility to GONE
 *
 * @see View.GONE
 */
fun View.gone() {
    visibility = View.GONE
}

/**
 * Sets the current views visibility to INVISIBLE
 *
 * @see View.INVISIBLE
 */
fun View.invisible() {
    visibility = View.INVISIBLE
}

/**
 * Sets the current views visibility to VISIBLE
 *
 * @see View.VISIBLE
 */
fun View.visible() {
    visibility = View.VISIBLE
}

@JvmOverloads
fun View.snackBar(
    @StringRes
    stringRes: Int,
    duration: Int,
    @StringRes
    actionRes: Int,
    action: ((Snackbar) -> Unit)? = null
): Snackbar {
    val snackBar = Snackbar.make(this, stringRes, duration)
    action?.run {
        snackBar.setAction(actionRes) { action.invoke(snackBar) }
    }
    snackBar.view.setBackgroundColor(context.getColorFromAttr(R.attr.colorPrimaryDark))
    val mainTextView = snackBar.view.findViewById<TextView>(R.id.snackbar_text)
    val actionTextView = snackBar.view.findViewById<TextView>(R.id.snackbar_action)
    mainTextView.setTextColor(context.getColorFromAttr(R.attr.titleTextColor))
    actionTextView.setTextColor(context.getColorFromAttr(R.attr.colorAccent))
    actionTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
    return snackBar
}

fun View.snackBar(@StringRes stringRes: Int, duration: Int): Snackbar =
    snackBar(stringRes, duration, 0)

fun Float.dipToPx() : Int {
    val scale = Resources.getSystem().displayMetrics.density
    return (this * scale + 0.5f).toInt()
}

fun Float.pxToDip() : Int {
    val scale = Resources.getSystem().displayMetrics.density
    return (this / scale + 0.5f).toInt()
}

fun Float.spToPx() : Int {
    val scaledDensity = Resources.getSystem().displayMetrics.scaledDensity
    return (this * scaledDensity).roundToInt()
}

fun Float.isSmallWidthScreen() : Boolean {
    val displayMetrics = Resources.getSystem().displayMetrics
    val widthDp = displayMetrics.widthPixels / displayMetrics.density
    val heightDp = displayMetrics.heightPixels / displayMetrics.density
    val screenSw = Math.min(widthDp, heightDp)
    return screenSw >= this
}

fun Float.isWideScreen() : Boolean {
    val displayMetrics = Resources.getSystem().displayMetrics
    val screenWidth = displayMetrics.widthPixels / displayMetrics.density
    return screenWidth >= this
}

/**
 * Credits
 * @author hamakn
 * https://gist.github.com/hamakn/8939eb68a920a6d7a498
 */
@SupportExperimental
fun Resources.getStatusBarHeight() : Int {
    var statusBarHeight = 0
    val resourceId = getIdentifier("status_bar_height", "dimen", "android")
    if (resourceId > 0)
        statusBarHeight = getDimensionPixelSize(resourceId)
    return statusBarHeight
}

/**
 * Credits
 * @author hamakn
 * https://gist.github.com/hamakn/8939eb68a920a6d7a498
 */
@SupportExperimental
fun Resources.getNavigationBarHeight() : Int {
    var navigationBarHeight = 0
    val resourceId = getIdentifier("navigation_bar_height", "dimen", "android")
    if (resourceId > 0)
        navigationBarHeight = getDimensionPixelSize(resourceId)
    return navigationBarHeight
}