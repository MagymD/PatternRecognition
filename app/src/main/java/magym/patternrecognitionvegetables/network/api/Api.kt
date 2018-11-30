package magym.patternrecognitionvegetables.network.api

import io.reactivex.Single
import magym.patternrecognitionvegetables.data.Item
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface Api {

    @Multipart
    @POST("api/")
    fun upload(
            @Part("description") description: RequestBody,
            @Part file: MultipartBody.Part
    ): Single<List<Item>>

}