package magym.patternrecognitionvegetables.presentation.screen.photo

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlinx.android.synthetic.main.activity_photo.*
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import magym.patternrecognitionvegetables.R
import magym.patternrecognitionvegetables.presentation.common.CameraManager
import magym.patternrecognitionvegetables.util.extension.*
import org.koin.android.ext.android.inject
import java.io.File

class PhotoActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {

    private val viewModel: PhotoViewModel by inject()
    private val surface: PhotoSurfaceView by lazy { surface_view_photo }
    private val camera: CameraManager by lazy { CameraManager(this, surface_view_camera) }
    private val swipeRefreshLayout: SwipeRefreshLayout by lazy { swipe_refresh_layout.init(this, false) }

    private val isPermissionsGranted
        get() = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED

    private var titleToolbar: String
        get() = supportActionBar?.title.toString()
        set(value) {
            supportActionBar?.title = value
        }

    private var isRefreshing: Boolean
        get() = swipeRefreshLayout.isRefreshing
        set(value) {
            swipeRefreshLayout.isEnabled = value
            swipeRefreshLayout.isRefreshing = value
            allButtonsIsEnabled = !value
        }

    private var allButtonsIsEnabled
        get() = create_photo.isEnabled && open_gallery.isEnabled
        set(value) {
            create_photo.isEnabled = value
            open_gallery.isEnabled = value
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo)
        isRefreshing = false

        requestPermissions()

        create_photo.onClick = ::createPhoto
        open_gallery.onClick = ::openGallery

        viewModel.bitmap.observe(this, Observer {
            surface.bitmap = it
        })

        viewModel.result.observe(this, Observer {
            surface.items = it
            isRefreshing = false
        })

        viewModel.error.observe(this, Observer {
            showAnswer(it.toString())
            isRefreshing = false
        })
    }

    override fun onResume() {
        super.onResume()
        if (isPermissionsGranted) camera.start()
    }

    override fun onPause() {
        super.onPause()
        camera.finish()
    }

    override fun onRefresh() {}

    private fun requestPermissions() {
        if (isPermissionsGranted) return

        ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                PERMISSIONS)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSIONS -> {
                if (isPermissionsGranted) {
                    camera.start()
                } else {
                    GlobalScope.launch(Main) {
                        allButtonsIsEnabled = false
                        getString(R.string.permission_requirement).showToast()
                        delay(2200)
                        finish()
                    }
                }
            }
        }
    }

    private fun showAnswer(text: String) {
        titleToolbar = text
        text.showSnackbar()
    }

    private fun createPhoto() {
        isRefreshing = true

        // Некорректное отображение фотографии
        camera.photo { file ->
            viewModel.bitmap.value = file.toBitmap()
            getString(R.string.photo_was_taken).showSnackbar()
            viewModel.uploadPhoto(file) { file.deleteFile() }
        }
    }

    private fun openGallery() {
        startActivityForResult(Intent(Intent.ACTION_PICK).apply { type = "image/*" }, GALLERY_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when {
            requestCode == GALLERY_REQUEST && resultCode == Activity.RESULT_OK -> {
                val selectedImage = data?.data

                selectedImage?.let { uri ->
                    isRefreshing = true

                    val file = File(uri.getMyPath(contentResolver))

                    viewModel.bitmap.value = file.toBitmap()
                    viewModel.uploadPhoto(file) {}
                }
            }
        }
    }

    private fun File.toBitmap() = BitmapFactory.decodeFile(this.absolutePath)


    private fun String.showToast() = this@PhotoActivity.showToast(this)

    private fun String.showSnackbar() = layout.showSnackbarExt(this)

    private companion object {
        private const val PERMISSIONS = 1
        private const val GALLERY_REQUEST = 2
    }

}