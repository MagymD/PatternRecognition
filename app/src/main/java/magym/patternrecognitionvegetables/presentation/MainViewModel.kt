package magym.patternrecognitionvegetables.presentation

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import kotlinx.coroutines.experimental.launch
import magym.patternrecognitionvegetables.network.DataRequestManager
import magym.patternrecognitionvegetables.utils.log
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class MainViewModel : ViewModel() {

    private val requestManager = DataRequestManager()

    internal val result: LiveData<String>
        get() = previewResult

    private val previewResult = MutableLiveData<String>()

    internal fun uploadPhoto(file: File, fileCallback: () -> Unit) {
        val description = RequestBody.create(okhttp3.MultipartBody.FORM, file.name)

        val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
        val body = MultipartBody.Part.createFormData("picture", file.name, requestFile)

        launch {
            requestManager.requestUploadPhoto(description, body, {
                previewResult.postValue("Ответ получен ($it)")
                fileCallback()
            }, {
                fileCallback()
                it.toString().log(it)
                previewResult.postValue("Ошибка (${it.message})")
            })
        }
    }

}