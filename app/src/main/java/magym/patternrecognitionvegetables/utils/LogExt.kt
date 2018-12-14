package magym.patternrecognitionvegetables.utils

import android.util.Log

internal fun String.log() = Log.d("myTag", this)

internal fun Any.log() = this.toString().log()

internal fun Exception.log() = Log.e("myTag", "", this)

internal fun Throwable.log() = Log.e("myTag", "", this)