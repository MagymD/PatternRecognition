package magym.patternrecognitionvegetables.presentation.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel

open class BaseViewModel() : ViewModel() {

    private val parentJob = Job()
    private val coroutineContext get() = parentJob + Dispatchers.Default
    val scope = CoroutineScope(coroutineContext)

    override fun onCleared() {
        coroutineContext.cancel()
        super.onCleared()
    }

}