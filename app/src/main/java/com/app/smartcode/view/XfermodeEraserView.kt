package com.app.smartcode.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class XfermodeEraserView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    View(context, attributeSet, defStyleAttr) {

    private val mH = 400f

    private var mTextPaint: Paint = Paint().apply {
        style = Paint.Style.FILL
        isAntiAlias = true
        textSize = 60f
        color = Color.RED
        textAlign = Paint.Align.CENTER
    }

    private var mBgPaint :Paint = Paint().apply {
        style = Paint.Style.FILL
        isAntiAlias = true
        color = Color.YELLOW
    }

    private var mPathPaint :Paint = Paint().apply {
        style = Paint.Style.STROKE
        isAntiAlias = true
        strokeWidth = 50f
    }

    private lateinit var bitmap: Bitmap

    private val mPath: Path = Path()

    private var mEventX: Float = 0f
    private var mEventY: Float = 0f

    init {
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)


        val fontMetrics = mTextPaint.fontMetrics
        val tY = mH / 2f + (fontMetrics.bottom - fontMetrics.top)/2 - fontMetrics.bottom

        canvas.drawText("恭喜获得奖金500万", width / 2f, tY, mTextPaint)

        canvas.save()

        bitmap = Bitmap.createBitmap(width, mH.toInt(), Bitmap.Config.ARGB_8888)

        val dstCanvas = Canvas(bitmap)

        dstCanvas.drawPath(mPath,mPathPaint)

        mBgPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_OUT)

        mBgPaint.color = Color.GRAY

        dstCanvas.drawRect(RectF(0f,0f,width.toFloat(),mH),mBgPaint)

        mBgPaint.xfermode = null

        canvas.drawBitmap(bitmap,0f,0f,null)

        canvas.restore()

    }



    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        super.onTouchEvent(event)
        when(event?.action){
            MotionEvent.ACTION_DOWN -> {
                mEventX = event.x
                mEventY = event.y
                mPath.moveTo(mEventX,mEventY)
            }
            MotionEvent.ACTION_MOVE -> {
                val endX = (event.x - mEventX) / 2 + mEventX
                val endY = (event.y - mEventY) / 2 + mEventY
                mPath.quadTo(mEventX, mEventY, endX, endY)
                mEventX = event.x
                mEventY = event.y
            }
        }
        invalidate()
        return true

    }

}