package magym.patternrecognitionvegetables.presentation

import android.Manifest
import android.arch.lifecycle.Observer
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.android.UI
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import magym.patternrecognitionvegetables.R
import magym.patternrecognitionvegetables.presentation.surface.SurfaceView
import magym.patternrecognitionvegetables.utils.deleteFile
import magym.patternrecognitionvegetables.utils.showSnackbarExt
import java.io.File


class MainActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    private val viewModel = MainViewModel()
    private val camera: CameraManager by lazy { CameraManager(this, surface_view) }

    private lateinit var surfaceView: SurfaceView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        swipeRefreshLayout = swipe_refresh_layout.init(this, false)

        requestPermissions()

        viewModel.resultData.observe(this@MainActivity, Observer {
            surfaceView.items = it
            setRefreshing(false)
        })
        viewModel.resultError.observe(this@MainActivity, Observer {
            showAnswer(it.toString())
            setRefreshing(false)
        })

        surfaceView = surface_view_map

        create_photo.setOnClickListener {
            setRefreshing(true)

            camera.photo { file ->
                file.showImage()
                "Фотография сделана".showSnackbar()
                viewModel.uploadPhoto(file) { file.deleteFile() }
            }
        }
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
        surfaceView.bitmap = BitmapFactory.decodeFile(this.absolutePath)
    }

    private fun setTitleToolbar(text: String) {
        supportActionBar?.title = text
    }

    private fun String.showToast() = Toast.makeText(this@MainActivity, this, Toast.LENGTH_SHORT).show()

    private fun String.showSnackbar() = layout.showSnackbarExt(this)

    private fun SwipeRefreshLayout.init(listener: SwipeRefreshLayout.OnRefreshListener, enable: Boolean = true): SwipeRefreshLayout {
        this.setOnRefreshListener(listener)
        this.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark, R.color.colorAccent)
        if (!enable) this.isEnabled = enable
        return this
    }

    companion object {
        private const val PERMISSIONS = 1
    }

}