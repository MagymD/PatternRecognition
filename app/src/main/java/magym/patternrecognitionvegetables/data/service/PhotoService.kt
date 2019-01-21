package magym.patternrecognitionvegetables.data.service

import io.reactivex.Single
import magym.patternrecognitionvegetables.data.entity.PhotoItemEntity
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface PhotoService {

    @Multipart
    @POST("service/")
    fun uploadPhoto(
            @Part("description") description: RequestBody,
            @Part file: MultipartBody.Part
    ): Single<List<PhotoItemEntity>>

}