package magym.patternrecognitionvegetables.domain.interactor

import magym.patternrecognitionvegetables.data.entity.PhotoItemEntity
import magym.patternrecognitionvegetables.domain.repository.PhotoRepository
import magym.patternrecognitionvegetables.domain.result.RequestResult
import okhttp3.MultipartBody
import okhttp3.RequestBody

class PhotoInteractor(private val pero: PhotoRepository) {

    fun requestUploadPhoto(description: RequestBody, file: MultipartBody.Part, callback: (List<PhotoItemEntity>) -> Unit, fallback: (Throwable) -> Unit) {
        val data = pero.uploadPhoto(description, file)

        return when (data) {
            is RequestResult.Result -> callback(data.data)
            is RequestResult.Error -> fallback(data.error)
        }
    }

}