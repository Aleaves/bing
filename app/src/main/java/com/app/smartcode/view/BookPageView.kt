package com.app.smartcode.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.Scroller
import com.app.smartcode.entity.MyPoint

class BookPageView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    View(context, attributeSet, defStyleAttr) {

    private val pathAPaint: Paint = Paint().apply {
        color = Color.GREEN
        isAntiAlias = true
    }

    private val pathCPaint: Paint = Paint().apply {
        color = Color.YELLOW
        isAntiAlias = true
        xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_ATOP)
    }

    private val pathBPaint: Paint = Paint().apply {
        color = Color.BLUE
        isAntiAlias = true
        xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_ATOP)
    }

    private val textPaint: Paint = Paint().apply {
        color = Color.BLACK
        textAlign = Paint.Align.CENTER
        isSubpixelText = true
        textSize = 30f
    }

    private lateinit var mScroller: Scroller

    private val pathA: Path = Path()
    private val pathB: Path = Path()
    private val pathC: Path = Path()

    private lateinit var bitmap: Bitmap
    private lateinit var bitmapCanvas: Canvas

    private val a = MyPoint()
    private val f = MyPoint()
    private val g = MyPoint()
    private val e = MyPoint()
    private val h = MyPoint()
    private val c = MyPoint()
    private val j = MyPoint()
    private var b = MyPoint()
    private var k = MyPoint()
    private val d = MyPoint()
    private val i = MyPoint()

    companion object {
        private const val STYLE_LEFT = 1
        private const val STYLE_RIGHT = 2
        private const val STYLE_MIDDLE = 3
        private const val STYLE_TOP_RIGHT = 4
        private const val STYLE_LOWER_RIGHT = 5
    }

    init {
        mScroller = Scroller(context, LinearInterpolator())
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        a.x = -1f
        a.y = -1f
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        bitmapCanvas = Canvas(bitmap)

        if (a.x == -1f && a.y == -1f) {
            bitmapCanvas.drawPath(getPathDefault(), pathAPaint)
        } else {
            if (f.x == width.toFloat() && f.y == 0f) {
                bitmapCanvas.drawPath(getPathAFromTopRight(), pathAPaint)
            } else if (f.x == width.toFloat() && f.y == height.toFloat()) {
                bitmapCanvas.drawPath(getPathAFromLowerRight(), pathAPaint)
            }
            bitmapCanvas.drawPath(getPathC(), pathCPaint)
            bitmapCanvas.drawPath(getPathB(), pathBPaint)
        }

        canvas.drawBitmap(bitmap, 0f, 0f, null);

    }

    private fun drawPathAContent(canvas: Canvas, pathA: Path, pathPaint: Paint) {
        val contentBitmap = Bitmap.createBitmap(width,height,Bitmap.Config.RGB_565)
        val contentCanvas = Canvas(contentBitmap)
        contentCanvas.drawPath(pathA,pathPaint)
        contentCanvas.drawText("这是在A区域的内容...AAAA",width -260f,height -100f, textPaint)

        canvas.save()
        canvas.clipPath(pathA, Region.Op.INTERSECT)
        canvas.drawBitmap(contentBitmap,0f,0f,null)
        canvas.restore()
    }

    private fun drawPathBContent(canvas: Canvas, pathA: Path, pathPaint: Paint) {
        val contentBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        val contentCanvas = Canvas(contentBitmap)
        contentCanvas.drawPath(getPathB(),pathPaint)
        contentCanvas.drawText("这是在B区域的内容...BBBB",width -260f,height -100f,textPaint)

        canvas.save()

        if(Build.VERSION.SDK_INT >= 26){
            val pathB = getPathB()
            val pathC = getPathC()
            //canvas.clipPath(pathA)
            pathC.op(pathA,Path.Op.UNION)
            //canvas.clipPath(pathC)
            pathB.op(pathC,Path.Op.REVERSE_DIFFERENCE)
            canvas.clipPath(pathB)
        }else {
            canvas.clipPath(pathA) //裁剪出A区域
            canvas.clipPath(getPathC(), Region.Op.UNION) //裁剪出A和C区域的全集
            canvas.clipPath(getPathB(), Region.Op.REVERSE_DIFFERENCE) //裁剪出B区域中不同于与AC区域的部分
        }

        canvas.drawBitmap(contentBitmap, 0f, 0f, null);
        canvas.restore()
    }


    private fun getPathB(): Path {
        return pathB.apply {
            lineTo(0f, height.toFloat())
            lineTo(width.toFloat(), height.toFloat())
            lineTo(width.toFloat(), 0f)
            close()
        }
    }


    private fun getPathC(): Path {
        pathC.apply {
            reset()
            moveTo(i.x, i.y)
            lineTo(d.x, d.y)
            lineTo(b.x, b.y)
            lineTo(a.x, a.y)
            lineTo(k.x, k.y)
            close()
        }
        return pathC
    }


    private fun calcPointCX(a: MyPoint, f: MyPoint): Float {
        val g = MyPoint()
        val e = MyPoint()
        g.x = (a.x + f.x) / 2
        g.y = (a.y + f.y) / 2

        e.x = g.x - (f.y - g.y) * (f.y - g.y) / (f.x - g.x)
        e.y = f.y

        return e.x - (f.x - e.x) / 2f
    }

    private fun getPathDefault(): Path {
        return pathA.apply {
            reset()
            lineTo(0f, height.toFloat())
            lineTo(width.toFloat(), height.toFloat())
            lineTo(width.toFloat(), 0f)
            close()
        }
    }


    private fun getPathAFromTopRight(): Path {
        return pathA.apply {
            reset()
            lineTo(c.x, c.y)
            quadTo(e.x, e.y, b.x, b.y)
            lineTo(a.x, a.y)
            lineTo(k.x, k.y)
            quadTo(h.x, h.y, j.x, j.y)
            lineTo(width.toFloat(), height.toFloat())
            lineTo(0f, height.toFloat())
            close()
        }
    }

    /**
     * 获取f点在右下角的pathA
     */
    private fun getPathAFromLowerRight(): Path {
        return pathA.apply {
            reset()
            lineTo(0f, height.toFloat())
            lineTo(c.x, c.y)
            quadTo(e.x, e.y, b.x, b.y)
            lineTo(a.x, a.y)
            lineTo(k.x, k.y)
            quadTo(h.x, h.y, j.x, j.y)
            lineTo(width.toFloat(), 0f)
            close()
        }
    }

    /**
     * 计算各点坐标
     * @param a
     * @param f
     */
    private fun calcPointsXY(a: MyPoint, f: MyPoint) {
        g.x = (a.x + f.x) / 2
        g.y = (a.y + f.y) / 2

        e.x = g.x - (f.y - g.y) * (f.y - g.y) / (f.x - g.x)
        e.y = f.y

        h.x = f.x
        h.y = g.y - (f.x - g.x) * (f.x - g.x) / (f.y - g.y)

        c.x = e.x - (f.x - e.x) / 2
        c.y = f.y

        j.x = f.x
        j.y = h.y - (f.y - h.y) / 2;

        b = getIntersectionPoint(a, e, c, j)
        k = getIntersectionPoint(a, h, c, j)

        d.x = (c.x + 2 * e.x + b.x) / 4
        d.y = (2 * e.y + c.y + b.y) / 4

        i.x = (j.x + 2 * h.x + k.x) / 4
        i.y = (2 * h.y + j.y + k.y) / 4

    }

    /**
     * 计算两线段相交点坐标
     * @param lineOne_My_pointOne
     * @param lineOne_My_pointTwo
     * @param lineTwo_My_pointOne
     * @param lineTwo_My_pointTwo
     * @return 返回该点
     */
    private fun getIntersectionPoint(
        lineOne_My_pointOne: MyPoint,
        lineOne_My_pointTwo: MyPoint,
        lineTwo_My_pointOne: MyPoint,
        lineTwo_My_pointTwo: MyPoint
    ): MyPoint {
        val x1 = lineOne_My_pointOne.x
        val y1 = lineOne_My_pointOne.y
        val x2 = lineOne_My_pointTwo.x
        val y2 = lineOne_My_pointTwo.y
        val x3 = lineTwo_My_pointOne.x
        val y3 = lineTwo_My_pointOne.y
        val x4 = lineTwo_My_pointTwo.x
        val y4 = lineTwo_My_pointTwo.y

        val pointX =
            ((x1 - x2) * (x3 * y4 - x4 * y3) - (x3 - x4) * (x1 * y2 - x2 * y1)) / ((x3 - x4) * (y1 - y2) - (x1 - x2) * (y3 - y4))

        val pointY =
            ((y1 - y2) * (x3 * y4 - x4 * y3) - (x1 * y2 - x2 * y1) * (y3 - y4)) / ((y1 - y2) * (x3 - x4) - (x1 - x2) * (y3 - y4))

        return MyPoint(pointX, pointY)

    }

    private var touchStyle = 0

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                val x = event.x
                val y = event.y
                if (x <= width / 3) {
                    touchStyle = STYLE_LEFT
                } else if (x > width / 3 && y <= height / 3) {
                    touchStyle = STYLE_TOP_RIGHT
                } else if (x > width * 2 / 3 && y > height / 3 && y <= height * 2 / 3) {
                    touchStyle = STYLE_RIGHT
                } else if (x > width / 3 && y > height * 2 / 3) {
                    touchStyle = STYLE_LOWER_RIGHT
                } else if (x > width / 3 && x < width * 2 / 3 && y > height / 3 && y < height * 2 / 3) {
                    touchStyle = STYLE_MIDDLE
                }
                setTouchPoint(x, y, touchStyle)
            }

            MotionEvent.ACTION_MOVE -> {
                setTouchPoint(event.x, event.y, touchStyle)
            }

            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                //setDefaultPath()
                startCancelAnim()
            }

        }
        return true
    }

    private fun setTouchPoint(x: Float, y: Float, style: Int) {

        a.x = x
        a.y = y
        val touchPoint = MyPoint(x, y)

        when (style) {
            STYLE_TOP_RIGHT -> {
                f.x = width.toFloat()
                f.y = 0f
                calcPointsXY(a, f)
                if (calcPointCX(touchPoint, f) < 0) {
                    calcPointAByTouchPoint()
                    calcPointsXY(a, f)
                }
                postInvalidate()
            }
            STYLE_LEFT, STYLE_RIGHT -> {
                a.y = height - 1f
                f.x = width.toFloat()
                f.y = height.toFloat()
                calcPointsXY(a, f)
                postInvalidate()
            }
            STYLE_LOWER_RIGHT -> {
                f.x = width.toFloat()
                f.y = height.toFloat()
                calcPointsXY(a, f)
                if (calcPointCX(touchPoint, f) < 0) {
                    calcPointAByTouchPoint()
                    calcPointsXY(a, f)
                }
                postInvalidate()
            }
        }
    }

    private fun calcPointAByTouchPoint() {
        val w0 = width - c.x
        val w1 = Math.abs(f.x - a.x)
        val w2 = width * w1 / w0
        a.x = Math.abs(f.x - w2)

        val h1 = Math.abs(f.y - a.y)
        val h2 = w2 * h1 / w1
        a.y = Math.abs(f.y - h2)
    }

    private fun setDefaultPath() {
        a.x = -1f
        a.y = -1f
        postInvalidate()
    }

    private fun startCancelAnim() {
        var dx = 0f
        var dy = 0f
        if (touchStyle == STYLE_TOP_RIGHT) {
            dx = width - 1 - a.x
            dy = 1 - a.y
        } else {
            dx = width - 1 - a.x
            dy = height - 1 - a.y
        }
        mScroller.startScroll(a.x.toInt(), a.y.toInt(), dx.toInt(), dy.toInt(), 400)
    }

    override fun computeScroll() {
        if (mScroller.computeScrollOffset()) {
            val x = mScroller.currX
            val y = mScroller.currY
            if (touchStyle == STYLE_TOP_RIGHT) {
                setTouchPoint(x.toFloat(), y.toFloat(), STYLE_TOP_RIGHT)
            }else{
                setTouchPoint(x.toFloat(), y.toFloat(), STYLE_LOWER_RIGHT)
            }
            if(mScroller.finalX == x && mScroller.finalY == y){
                setDefaultPath()
            }
        }
    }


}