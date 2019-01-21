package magym.patternrecognitionvegetables.presentation.base.surface

import android.graphics.Canvas
import android.view.SurfaceHolder
import magym.patternrecognitionvegetables.util.extention.log

class RenderingThread(private val view: BaseSurfaceView,
                      private val holder: SurfaceHolder) : Thread() {

    var threadRunning: Boolean = false

    override fun run() {
        while (threadRunning) {
            var canvas: Canvas? = null

            try {
                canvas = holder.lockCanvas()
                synchronized(holder) { canvas?.let { view.draw(canvas) } }
            } catch (e: Exception) {
                e.log()
            } finally {
                canvas?.let { holder.unlockCanvasAndPost(canvas) }
            }

            sleepTime(15)
        }
    }

    private fun sleepTime(fps: Long) {
        val ticksPS = 1000 / fps
        val startTime: Long = System.currentTimeMillis()
        val sleepTime = ticksPS - (System.currentTimeMillis() - startTime)

        try {
            if (sleepTime > 0)
                Thread.sleep(sleepTime)
            else
                Thread.sleep(100)
        } catch (e: Exception) {
            e.log()
        }
    }

}