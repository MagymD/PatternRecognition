package magym.patternrecognitionvegetables.util.extention

import android.view.View

/**
 * Удобная проперти для установки в качестве онклика — лямбды
 */
var View.onClick: () -> Unit
    get() = {}
    set(value) = setOnClickListener { value() }