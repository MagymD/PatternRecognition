package magym.patternrecognitionvegetables.presentation.screen.photo

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import magym.patternrecognitionvegetables.data.entity.PhotoItemEntity
import magym.patternrecognitionvegetables.domain.interactor.PhotoInteractor
import magym.patternrecognitionvegetables.util.extention.log
import java.io.File

class PhotoViewModel(private val interactor: PhotoInteractor) : ViewModel() {

    val bitmap = MutableLiveData<Bitmap>()

    val result: LiveData<List<PhotoItemEntity>>
        get() = previewResult
    private val previewResult = MutableLiveData<List<PhotoItemEntity>>()

    val error: LiveData<String>
        get() = previewError
    private val previewError = MutableLiveData<String>()

    fun uploadPhoto(file: File, fileLoadedCallback: () -> Unit) {
        GlobalScope.launch {
            interactor.requestUploadPhoto(file, {
                previewResult.postValue(it)
                fileLoadedCallback()
            }, {
                it.log()
                previewError.postValue("Ошибка: ${it.message}")
                fileLoadedCallback()
            })
        }
    }

}