package magym.patternrecognitionvegetables.util.extension

import android.view.View
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import magym.patternrecognitionvegetables.R

/**
 * Удобная проперти для установки в качестве онклика — лямбды
 */
var View.onClick: () -> Unit
    get() = {}
    set(value) = setOnClickListener { value() }

fun SwipeRefreshLayout.init(listener: SwipeRefreshLayout.OnRefreshListener, enable: Boolean = true): SwipeRefreshLayout {
    this.setOnRefreshListener(listener)
    this.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark, R.color.colorAccent)
    if (!enable) this.isEnabled = enable
    return this
}