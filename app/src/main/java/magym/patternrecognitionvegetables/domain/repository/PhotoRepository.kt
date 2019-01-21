package magym.patternrecognitionvegetables.domain.repository

import magym.patternrecognitionvegetables.data.entity.PhotoItemEntity
import magym.patternrecognitionvegetables.domain.result.RequestResult
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface PhotoRepository {

    fun uploadPhoto(description: RequestBody, file: MultipartBody.Part): RequestResult<List<PhotoItemEntity>>

}