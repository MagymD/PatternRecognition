package magym.patternrecognitionvegetables.utils

import android.util.Log

fun String.log() = Log.d("myTag", this)

fun String.log(e: Exception) = Log.e("myTag", this, e)

fun String.log(e: Throwable) = Log.e("myTag", this, e)