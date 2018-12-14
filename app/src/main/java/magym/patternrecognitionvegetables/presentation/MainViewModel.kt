package magym.patternrecognitionvegetables.presentation

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import kotlinx.coroutines.launch
import magym.patternrecognitionvegetables.data.Item
import magym.patternrecognitionvegetables.network.DataRequestManager
import magym.patternrecognitionvegetables.utils.deleteFile
import magym.patternrecognitionvegetables.utils.log
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class MainViewModel : ViewModel() {
    private val requestManager = DataRequestManager()

    internal val resultData: LiveData<List<Item>>
        get() = previewResultData
    private val previewResultData = MutableLiveData<List<Item>>()

    internal val resultError: LiveData<String>
        get() = previewResultError
    private val previewResultError = MutableLiveData<String>()

    internal fun uploadPhoto(file: File) {
        val description = RequestBody.create(okhttp3.MultipartBody.FORM, file.name)

        val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
        val body = MultipartBody.Part.createFormData("picture", file.name, requestFile)

        launch {
            requestManager.requestUploadPhoto(description, body, {
                previewResultData.postValue(it)
                file.deleteFile()
            }, {
                it.log()
                previewResultError.postValue("Ошибка: ${it.message}")
                file.deleteFile()
            })
        }
    }

}