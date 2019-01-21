package magym.patternrecognitionvegetables.data.repository

import magym.patternrecognitionvegetables.data.entity.PhotoItemEntity
import magym.patternrecognitionvegetables.data.service.PhotoService
import magym.patternrecognitionvegetables.domain.repository.PhotoRepository
import magym.patternrecognitionvegetables.domain.result.RequestResult
import okhttp3.MultipartBody
import okhttp3.RequestBody

class PhotoNetworkRepository(private val service: PhotoService) : PhotoRepository {

    override fun uploadPhoto(description: RequestBody, file: MultipartBody.Part): RequestResult<List<PhotoItemEntity>> {
        return service.uploadPhoto(description, file)
                .map<RequestResult<List<PhotoItemEntity>>> { RequestResult.Result(it) }
                .onErrorReturn { t -> RequestResult.Error(t) }
                .blockingGet()
    }

}