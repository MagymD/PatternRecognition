package magym.patternrecognitionvegetables.util.extension

import android.view.View
import com.google.android.material.snackbar.Snackbar

fun View.showSnackbarExt(text: String) = Snackbar.make(this, text, Snackbar.LENGTH_LONG).show()