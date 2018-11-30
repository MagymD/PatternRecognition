package magym.patternrecognitionvegetables.presentation.surface

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import magym.patternrecognitionvegetables.data.Item


class SurfaceView(context: Context, attributeSet: AttributeSet) : BaseSurfaceView(context, attributeSet) {

    internal var bitmap: Bitmap? = null
        set(value) {
            items = null
            field = value
        }
    internal var items: List<Item>? = null

    private var readyToDraw = false

    private val sizeX: Int by lazy { measuredWidth }
    private val sizeY: Int by lazy { measuredHeight }

    private val ratioX
        get() = (bitmap?.width ?: 0).toFloat() / sizeX
    private val ratioY
        get() = (bitmap?.height ?: 0).toFloat() / sizeY

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
            this.drawBitmap(bitmap!!, null, Rect(0, 0, sizeX, sizeY), paint)
        }
    }

    private fun Canvas.drawFrames() {
        items?.let {
            for (i in 0 until items!!.size) {
                val item = items!![i]
                this.drawRectWithRatio(item.boxPoints[0], item.boxPoints[1], item.boxPoints[2], item.boxPoints[3], paintRed)
                this.drawTextWithRatio("${item.name}: ${item.percentageProbability.toInt()}%", item.boxPoints[0], item.boxPoints[1], paintText)
            }
        }
    }

    private fun Canvas.drawRectWithRatio(left: Int, top: Int, right: Int, bottom: Int, paint: Paint) {
        this.drawRect(left.toFloat() / ratioX, top.toFloat() / ratioY, right.toFloat() / ratioX, bottom.toFloat() / ratioY, paint)
    }

    private fun Canvas.drawTextWithRatio(text: String, x: Int, y: Int, paint: Paint) {
        this.drawText(text, x.toFloat() / ratioX, y.toFloat() / ratioY, paint)
    }

}