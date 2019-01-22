package magym.patternrecognitionvegetables.util.extention

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("Recycle")
fun Uri.getMyPath(contentResolver: ContentResolver): String? {
    val projection = arrayOf(MediaStore.Images.Media.DATA)
    val cursor = contentResolver.query(this, projection, null, null, null) ?: return null
    val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
    cursor.moveToFirst()
    val result = cursor.getString(columnIndex)
    cursor.close()
    return result
}

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