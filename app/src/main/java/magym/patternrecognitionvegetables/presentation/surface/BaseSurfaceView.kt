package magym.patternrecognitionvegetables.presentation.surface

import android.content.Context
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView
import magym.patternrecognitionvegetables.utils.log

open class BaseSurfaceView(context: Context, attributeSet: AttributeSet) : SurfaceView(context, attributeSet), SurfaceHolder.Callback {

    private var renderingThread: RenderingThread? = null

    init {
        isFocusable = true
        holder.addCallback(this)
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        renderingThread = RenderingThread(this, getHolder()).apply {
            threadRunning = true
            start()
        }
    }

    override fun surfaceChanged(arg0: SurfaceHolder, arg1: Int, arg2: Int, arg3: Int) {}

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        var retry = true
        renderingThread?.threadRunning = false

        while (retry) {
            try {
                renderingThread?.join()
                retry = false
            } catch (e: InterruptedException) {
                e.log()
            }
        }
    }

}