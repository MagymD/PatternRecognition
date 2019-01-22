package magym.patternrecognitionvegetables.presentation.screen.photo

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.arch.lifecycle.Observer
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_photo.*
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import magym.patternrecognitionvegetables.R
import magym.patternrecognitionvegetables.presentation.common.CameraManager
import magym.patternrecognitionvegetables.util.extention.deleteFile
import magym.patternrecognitionvegetables.util.extention.onClick
import magym.patternrecognitionvegetables.util.extention.showSnackbarExt
import magym.patternrecognitionvegetables.util.extention.showToast
import org.koin.android.ext.android.inject
import java.io.File

class PhotoActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    private val viewModel: PhotoViewModel by inject()
    private val camera: CameraManager by lazy { CameraManager(this, surface_view_camera) }

    private lateinit var surface: PhotoSurfaceView

    private companion object {
        private const val PERMISSIONS = 1
        private const val GALLERY_REQUEST = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo)
        swipeRefreshLayout = swipe_refresh_layout.init(this, false)

        requestPermissions()

        surface = surface_view_map

        open_photo.onClick = ::openImage
        create_photo.onClick = ::createPhoto

        viewModel.result.observe(this, Observer {
            surface.items = it
            setRefreshing(false)
        })

        viewModel.error.observe(this, Observer {
            showAnswer(it.toString())
            setRefreshing(false)
        })
    }

    override fun onResume() {
        super.onResume()
        if (permissions()) camera.start()
    }

    override fun onPause() {
        super.onPause()
        camera.finish()
    }

    override fun onRefresh() {}

    private fun setRefreshing(refreshing: Boolean) {
        swipeRefreshLayout.isEnabled = refreshing
        swipeRefreshLayout.isRefreshing = refreshing

        create_photo.isEnabled = !refreshing
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
                if (permissions()) {
                    camera.start()
                } else {
                    GlobalScope.launch(Main) {
                        create_photo.isEnabled = false
                        getString(R.string.permission_requirement).showToast()
                        delay(2200)
                        finish()
                    }
                }
            }
        }
    }

    private fun permissions() = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED

    private fun showAnswer(text: String) {
        setTitleToolbar(text)
        text.showSnackbar()
    }

    private fun openImage() {
        startActivityForResult(Intent(Intent.ACTION_PICK).apply { type = "image/*" }, GALLERY_REQUEST)
    }

    private fun createPhoto() {
        setRefreshing(true)

        // Некорректное отображение фотографии
        camera.photo { file ->
            file.showImage()
            getString(R.string.photo_was_taken).showSnackbar()
            viewModel.uploadPhoto(file) {
                file.deleteFile()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            GALLERY_REQUEST -> if (resultCode == Activity.RESULT_OK) {
                val selectedImage = data?.data
                selectedImage?.let {
                    setRefreshing(true)
                    val file = File(it.myGetPath())
                    file.showImage()
                    viewModel.uploadPhoto(file) {}
                }
            }
        }
    }

    private fun setTitleToolbar(text: String) {
        supportActionBar?.title = text
    }

    private fun File.showImage() {
        surface.bitmap = BitmapFactory.decodeFile(this.absolutePath)
    }

    private fun String.showToast() = this@PhotoActivity.showToast(this)

    private fun String.showSnackbar() = layout.showSnackbarExt(this)

    private fun SwipeRefreshLayout.init(listener: SwipeRefreshLayout.OnRefreshListener, enable: Boolean = true): SwipeRefreshLayout {
        this.setOnRefreshListener(listener)
        this.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark, R.color.colorAccent)
        if (!enable) this.isEnabled = enable
        return this
    }

    @SuppressLint("Recycle")
    private fun Uri.myGetPath(): String? {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(this, projection, null, null, null) ?: return null
        val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        val result = cursor.getString(columnIndex)
        cursor.close()
        return result
    }

}