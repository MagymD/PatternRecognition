package magym.patternrecognitionvegetables.presentation

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.hardware.Camera
import android.hardware.Camera.PictureCallback
import android.media.MediaRecorder
import android.os.Build
import android.view.SurfaceHolder
import android.view.SurfaceView
import magym.patternrecognitionvegetables.utils.createImageFile
import java.io.File
import java.io.FileOutputStream


class CameraManager(private val context: Context,
                    surfaceView: SurfaceView) {

    private var parameters: Camera.Parameters? = null
    private var mediaRecorder: MediaRecorder? = null

    init {
        val holder = surfaceView.holder
        holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder) {
                camera?.setPreviewDisplay(holder)
            }

            override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {}

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                finish()
            }
        })
    }

    internal fun start() {
        if (camera == null) {
            camera = Camera.open()

            if (parameters == null) createParameters()
            camera?.parameters = parameters
        }
    }

    internal fun finish() {
        mediaRecorder?.let {
            it.reset()
            it.release()
            mediaRecorder = null
        }

        camera?.let {
            it.lock()
            it.release()
            camera = null
        }
    }

    internal fun photo(photoPath: (File) -> Unit) {
        camera?.startPreview()

        camera?.autoFocus { _, camera ->
            camera.setPreviewCallback(null)

            Companion.camera?.takePicture(null, null, PictureCallback { data, _ ->
                val photoFile = context.createImageFile()

                val outputStream = FileOutputStream(photoFile)
                outputStream.write(data)
                outputStream.close()

                photoPath(photoFile)
            })
        }
    }

    private fun createParameters() {
        parameters = camera?.parameters
        parameters?.let {
            if (context.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                it.setRotation(90)
            }

            val flashModes = it.supportedFlashModes
            if (flashModes != null && flashModes.contains(Camera.Parameters.FLASH_MODE_OFF)) {
                it.flashMode = Camera.Parameters.FLASH_MODE_OFF
            }

            val whiteBalance = it.supportedWhiteBalance
            if (whiteBalance != null && whiteBalance.contains(Camera.Parameters.WHITE_BALANCE_AUTO)) {
                it.whiteBalance = Camera.Parameters.WHITE_BALANCE_AUTO
            }

            val focusModes = it.supportedFocusModes
            if (focusModes != null && focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
                it.focusMode = Camera.Parameters.FOCUS_MODE_AUTO
            }
        }

        @SuppressLint("ObsoleteSdkInt")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            camera?.enableShutterSound(false)
        }
    }

    companion object {
        private var camera: Camera? = null
    }

}