package magym.patternrecognitionvegetables.network

import okhttp3.MultipartBody
import okhttp3.RequestBody

class DataRequestManager {

    private val manager = NetworkRequestManager()

    internal fun requestUploadPhoto(description: RequestBody, file: MultipartBody.Part, callback: (String) -> Unit, fallback: (Throwable) -> Unit) {
        val data = manager.uploadPhoto(description, file)

        return when (data) {
            is RequestResult.Result -> callback(data.data)
            is RequestResult.Error -> fallback(data.error)
        }
    }

}