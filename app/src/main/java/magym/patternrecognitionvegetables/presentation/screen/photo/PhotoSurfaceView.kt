package magym.patternrecognitionvegetables.presentation.screen.photo

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

    private val paint = Paint()
    private val paintRect = Paint().apply {
        color = Color.RED
        style = Paint.Style.STROKE
    }
    private val paintText = Paint().apply {
        color = Color.RED
        style = Paint.Style.FILL
        textSize = 30f
    }

    private var bitmapX = 0f
    private var bitmapY = 0f
    private var ratioX = 0f
    private var ratioY = 0f

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        setRatio()
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        canvas.drawColor(Color.WHITE)
        canvas.drawBitmap()
        canvas.drawFrames()
    }

    private fun Canvas.drawBitmap() {
        bitmap?.let {
            this.drawBitmapWithRatio(it, right = bitmapX, bottom = bitmapY, paint = paint)
        }
    }

    private fun Canvas.drawFrames() {
        items?.let {
            for (i in 0 until it.size) {
                val item = it[i]
                this.drawRectWithRatio(item.boxPoints[0], item.boxPoints[1], item.boxPoints[2], item.boxPoints[3], paintRect)
                this.drawTextWithRatio("${item.name}: ${(item.percentageProbability * 100).toInt()}%", item.boxPoints[0], item.boxPoints[1], paintText)
            }
        }
    }

    private fun setRatio() {
        bitmapX = (bitmap?.width ?: 0).toFloat()
        bitmapY = (bitmap?.height ?: 0).toFloat()

        val ratioBitmap = measuredWidth / measuredHeight.toFloat()
        val ratioScreen = bitmapX / bitmapY
        val oldRatioX = bitmapX / measuredWidth
        val oldRatioY = bitmapY / measuredHeight

        ratioX = if (ratioBitmap < ratioScreen) oldRatioX else oldRatioY
        ratioY = if (ratioBitmap > ratioScreen) oldRatioY else oldRatioX
    }

    private fun Canvas.drawBitmapWithRatio(bitmap: Bitmap, left: Float = 0f, top: Float = 0f, right: Float, bottom: Float, paint: Paint) =
            this.drawBitmap(bitmap, null, Rect((left / ratioX).toInt(), (top / ratioX).toInt(), (right / ratioX).toInt(), (bottom / ratioY).toInt()), paint)

    private fun Canvas.drawRectWithRatio(left: Int, top: Int, right: Int, bottom: Int, paint: Paint) =
            this.drawRect(left.toFloat() / ratioX, top.toFloat() / ratioY, right.toFloat() / ratioX, bottom.toFloat() / ratioY, paint)

    private fun Canvas.drawTextWithRatio(text: String, x: Int, y: Int, paint: Paint) =
            this.drawText(text, x.toFloat() / ratioX, y.toFloat() / ratioY, paint)

}