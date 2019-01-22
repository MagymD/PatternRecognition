package magym.patternrecognitionvegetables.data.datasourse

import magym.patternrecognitionvegetables.data.entity.PhotoItemEntity
import magym.patternrecognitionvegetables.data.service.PhotoService
import magym.patternrecognitionvegetables.domain.repository.PhotoRepository
import magym.patternrecognitionvegetables.domain.result.RequestResult
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class NetworkPhotoDataSource(private val service: PhotoService) : PhotoRepository {

    override fun uploadPhoto(file: File): RequestResult<List<PhotoItemEntity>> {
        val description = RequestBody.create(okhttp3.MultipartBody.FORM, file.name)

        val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
        val body = MultipartBody.Part.createFormData("picture", file.name, requestFile)

        return service.uploadPhoto(description, body)
                .map<RequestResult<List<PhotoItemEntity>>> { RequestResult.Result(it) }
                .onErrorReturn { RequestResult.Error(it) }
                .blockingGet()
    }

}