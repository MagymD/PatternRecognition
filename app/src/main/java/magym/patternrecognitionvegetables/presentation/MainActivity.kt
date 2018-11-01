package magym.patternrecognitionvegetables.presentation

import android.Manifest
import android.arch.lifecycle.Observer
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import magym.patternrecognitionvegetables.R
import magym.patternrecognitionvegetables.utils.deleteFile
import magym.patternrecognitionvegetables.utils.showSnackbarExt
import java.io.File


class MainActivity : AppCompatActivity() {

    private val viewModel = MainViewModel()
    private val camera: CameraManager by lazy { CameraManager(this, surface_view) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        requestPermissions()

        viewModel.result.observe(this@MainActivity, Observer { showAnswer(it.toString()) })

        create_photo.setOnClickListener {
            create_photo.isEnabled = false

            camera.photo {
                it.showImage()
                "Фотография сделана".showSnackbar()
                viewModel.uploadPhoto(it) { it.deleteFile() }

                create_photo.isEnabled = true
            }
        }
    }

    private fun requestPermissions() {
        if (!permissions()) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    PERMISSIONS)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSIONS -> {
                if (!permissions()) {
                    launch(UI) {
                        create_photo.isEnabled = false
                        getString(R.string.permission_requirement).showToast()
                        delay(2200)
                        finish()
                    }
                } else {
                    camera.start()
                }
            }
        }
    }

    private fun permissions() = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED

    override fun onResume() {
        super.onResume()
        if (permissions()) camera.start()
    }

    override fun onPause() {
        super.onPause()
        camera.finish()
    }

    private fun showAnswer(text: String) {
        setTitleToolbar(text)
        text.showSnackbar()
    }

    private fun File.showImage() {
        image.setImageBitmap(BitmapFactory.decodeFile(this.absolutePath))
    }

    private fun setTitleToolbar(text: String) {
        supportActionBar?.title = text
    }

    private fun String.showToast() = Toast.makeText(this@MainActivity, this, Toast.LENGTH_SHORT).show()

    private fun String.showSnackbar() = create_photo.showSnackbarExt(this)

    companion object {
        private const val PERMISSIONS = 1
    }

}