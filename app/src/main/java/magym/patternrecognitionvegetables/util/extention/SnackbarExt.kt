package magym.patternrecognitionvegetables.util.extention

import android.support.design.widget.Snackbar
import android.view.View

fun View.showSnackbarExt(text: String) {
    Snackbar.make(this, text, Snackbar.LENGTH_LONG).show()
}