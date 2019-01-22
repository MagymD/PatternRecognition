package magym.patternrecognitionvegetables.domain.interactor

import magym.patternrecognitionvegetables.data.entity.PhotoItemEntity
import magym.patternrecognitionvegetables.domain.repository.PhotoRepository
import magym.patternrecognitionvegetables.domain.result.RequestResult
import java.io.File

class PhotoInteractor(private val repository: PhotoRepository) {

    fun requestUploadPhoto(file: File, callback: (List<PhotoItemEntity>) -> Unit, fallback: (Throwable) -> Unit) {
        val data = repository.uploadPhoto(file)

        return when (data) {
            is RequestResult.Result -> callback(data.data)
            is RequestResult.Error -> fallback(data.error)
        }
    }

}