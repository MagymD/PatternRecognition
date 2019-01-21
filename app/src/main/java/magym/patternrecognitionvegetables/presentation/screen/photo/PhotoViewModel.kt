package magym.patternrecognitionvegetables.presentation.screen.photo

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import magym.patternrecognitionvegetables.data.entity.PhotoItemEntity
import magym.patternrecognitionvegetables.domain.interactor.PhotoInteractor
import magym.patternrecognitionvegetables.util.extention.deleteFile
import magym.patternrecognitionvegetables.util.extention.log
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class PhotoViewModel(private val interactor: PhotoInteractor) : ViewModel() {

    val result: LiveData<List<PhotoItemEntity>>
        get() = previewResult
    private val previewResult = MutableLiveData<List<PhotoItemEntity>>()

    val error: LiveData<String>
        get() = previewError
    private val previewError = MutableLiveData<String>()

    fun uploadPhoto(file: File) {
        val description = RequestBody.create(okhttp3.MultipartBody.FORM, file.name)

        val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
        val body = MultipartBody.Part.createFormData("picture", file.name, requestFile)

        GlobalScope.launch {
            interactor.requestUploadPhoto(description, body, {
                previewResult.postValue(it)
                file.deleteFile()
            }, {
                it.log()
                previewError.postValue("Ошибка: ${it.message}")
                file.deleteFile()
            })
        }
    }

}