package magym.patternrecognitionvegetables.utils

import android.annotation.SuppressLint
import android.content.Context
import android.os.Environment
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

internal fun Context.createImageFile(): File {
    @SuppressLint("SimpleDateFormat")
    val timeStamp = SimpleDateFormat("yyyy.MM.dd_HH:mm:ss").format(Date())
    val fileName = "JPEG_${timeStamp}_"
    val directory = getExternalFilesDir(Environment.DIRECTORY_PICTURES)

    return File.createTempFile(fileName, ".jpg", directory)
}

internal fun File.deleteFile() {
    if (this.exists()) this.delete()
}