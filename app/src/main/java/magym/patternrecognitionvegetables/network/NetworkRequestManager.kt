package magym.patternrecognitionvegetables.network

import magym.patternrecognitionvegetables.data.Item
import okhttp3.MultipartBody
import okhttp3.RequestBody

class NetworkRequestManager {

    internal fun uploadPhoto(description: RequestBody, file: MultipartBody.Part): RequestResult<List<Item>> {
        return NetworkManager.api
                .upload(description, file)
                .map<RequestResult<List<Item>>> { RequestResult.Result(it) }
                .onErrorReturn { t -> RequestResult.Error(t) }
                .blockingGet()
    }

}