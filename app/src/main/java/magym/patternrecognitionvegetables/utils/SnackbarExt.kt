package magym.patternrecognitionvegetables.utils

import android.support.design.widget.Snackbar
import android.view.View

internal fun View.showSnackbarExt(text: String) {
    Snackbar.make(this, text, Snackbar.LENGTH_LONG).show()
}