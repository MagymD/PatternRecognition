package magym.patternrecognitionvegetables.util.extention

import android.util.Log

fun String.log() = Log.d("myTag", this)

fun Any.log() = this.toString().log()

fun Exception.log() = Log.e("myTag", "", this)

fun Throwable.log() = Log.e("myTag", "", this)