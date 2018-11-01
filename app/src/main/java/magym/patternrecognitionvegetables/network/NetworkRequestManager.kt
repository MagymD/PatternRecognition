package magym.patternrecognitionvegetables.network

import okhttp3.MultipartBody
import okhttp3.RequestBody

class NetworkRequestManager {

    internal fun uploadPhoto(description: RequestBody, file: MultipartBody.Part): RequestResult<String> {
        return NetworkManager.api
                .upload(description, file)
                .map<RequestResult<String>> { RequestResult.Result(it) }
                .onErrorReturn { t -> RequestResult.Error(t) }
                .blockingGet()
    }

}