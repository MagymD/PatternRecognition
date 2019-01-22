package magym.patternrecognitionvegetables.presentation.screen.photo

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import magym.patternrecognitionvegetables.data.entity.PhotoItemEntity
import magym.patternrecognitionvegetables.presentation.base.surface.BaseSurfaceView

class PhotoSurfaceView(context: Context, attributeSet: AttributeSet) : BaseSurfaceView(context, attributeSet) {

    var bitmap: Bitmap? = null
        set(value) {
            items = null
            field = value
            setRatio()
        }
    var items: List<PhotoItemEntity>? = null

    private var readyToDraw = false

    private val screenX: Int by lazy { measuredWidth }
    private val screenY: Int by lazy { measuredHeight }
    private var bitmapX = 0f
    private var bitmapY = 0f
    private var ratioX = 0f
    private var ratioY = 0f

    private val paint = Paint()

    private val paintRed = Paint().apply {
        color = Color.RED
        style = Paint.Style.STROKE
    }
    private val paintText = Paint().apply {
        color = Color.RED
        style = Paint.Style.FILL
        textSize = 30f
    }

    @SuppressLint("DrawAllocation")
    public override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        readyToDraw = true
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)

        if (readyToDraw) {
            canvas.drawBitmap()
            canvas.drawFrames()
        }
    }

    private fun Canvas.drawBitmap() {
        bitmap?.let {
            this.drawBitmap(bitmap!!, null, Rect(0, 0, (bitmapX / ratioX).toInt(), (bitmapY / ratioY).toInt()), paint)
        }
    }

    private fun Canvas.drawFrames() {
        items?.let {
            for (i in 0 until items!!.size) {
                val item = items!![i]
                this.drawRectWithRatio(item.boxPoints[0], item.boxPoints[1], item.boxPoints[2], item.boxPoints[3], paintRed)
                this.drawTextWithRatio("${item.name}: ${(item.percentageProbability * 100).toInt()}%", item.boxPoints[0], item.boxPoints[1], paintText)
            }
        }
    }

    private fun setRatio() {
        bitmapX = (bitmap?.width ?: 0).toFloat()
        bitmapY = (bitmap?.height ?: 0).toFloat()

        val ratioBitmap = screenX / screenY.toFloat()
        val ratioScreen = bitmapX / bitmapY
        val oldRatioX = bitmapX / screenX
        val oldRatioY = bitmapY / screenY

        ratioX = if (ratioBitmap < ratioScreen) oldRatioX else oldRatioY
        ratioY = if (ratioBitmap > ratioScreen) oldRatioY else oldRatioX
    }

    private fun Canvas.drawRectWithRatio(left: Int, top: Int, right: Int, bottom: Int, paint: Paint) {
        this.drawRect(left.toFloat() / ratioX, top.toFloat() / ratioY, right.toFloat() / ratioX, bottom.toFloat() / ratioY, paint)
    }

    private fun Canvas.drawTextWithRatio(text: String, x: Int, y: Int, paint: Paint) {
        this.drawText(text, x.toFloat() / ratioX, y.toFloat() / ratioY, paint)
    }

}