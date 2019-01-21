package magym.patternrecognitionvegetables.util.extention

import android.annotation.SuppressLint
import android.content.Context
import android.os.Environment
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

fun Context.createImageFile(): File {
    @SuppressLint("SimpleDateFormat")
    val timeStamp = SimpleDateFormat("yyyy.MM.dd_HH:mm:ss").format(Date())
    val fileName = "JPEG_${timeStamp}_"
    val directory = getExternalFilesDir(Environment.DIRECTORY_PICTURES)

    return File.createTempFile(fileName, ".jpg", directory)
}

fun File.deleteFile() {
    if (this.exists()) this.delete()
}