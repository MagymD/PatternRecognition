package magym.patternrecognitionvegetables.domain.repository

import magym.patternrecognitionvegetables.data.entity.PhotoItemEntity
import magym.patternrecognitionvegetables.domain.result.RequestResult
import java.io.File

interface PhotoRepository {

    fun uploadPhoto(file: File): RequestResult<List<PhotoItemEntity>>

}