package com.app.smartcode.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class BookPageView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    View(context, attributeSet, defStyleAttr) {

    private val pointPaint: Paint = Paint().apply {
        color = Color.RED
        textSize = 25f
    }

    private val bgPaint: Paint = Paint().apply {
        color = Color.GREEN
    }

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

    private val pathA: Path = Path()
    private val pathB: Path = Path()
    private val pathC: Path = Path()

    private lateinit var bitmap: Bitmap
    private lateinit var bitmapCanvas: Canvas

    private val a = Point()
    private val f = Point()
    private val g = Point()
    private val e = Point()
    private val h = Point()
    private val c = Point()
    private val j = Point()
    private var b = Point()
    private var k = Point()
    private val d = Point()
    private val i = Point()

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        f.x = MeasureSpec.getSize(widthMeasureSpec)
        f.y = MeasureSpec.getSize(heightMeasureSpec)
        a.x = f.x -10
        a.y = f.y -10
        calcPointsXY(a, f)
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        //canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), bgPaint)

        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        bitmapCanvas = Canvas(bitmap)
        bitmapCanvas.drawPath(getPathAFromLowerRight(), pathAPaint)
        bitmapCanvas.drawPath(getPathC(), pathCPaint)
        bitmapCanvas.drawPath(getPathB(), pathBPaint)

        canvas.drawBitmap(bitmap, 0f, 0f, null);

        canvas.drawText("a", a.x.toFloat(), a.y.toFloat(), pointPaint)

        canvas.drawText("f", f.x.toFloat(), f.y.toFloat(), pointPaint)

        canvas.drawText("g", g.x.toFloat(), g.y.toFloat(), pointPaint)

        canvas.drawText("e", e.x.toFloat(), e.y.toFloat(), pointPaint)

        canvas.drawText("h", h.x.toFloat(), h.y.toFloat(), pointPaint)

        canvas.drawText("c", c.x.toFloat(), c.y.toFloat(), pointPaint)

        canvas.drawText("j", j.x.toFloat(), j.y.toFloat(), pointPaint)

        canvas.drawText("b", b.x.toFloat(), b.y.toFloat(), pointPaint)

        canvas.drawText("k", k.x.toFloat(), k.y.toFloat(), pointPaint)

        canvas.drawText("d", d.x.toFloat(), d.y.toFloat(), pointPaint)

        canvas.drawText("i", i.x.toFloat(), i.y.toFloat(), pointPaint)

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
            moveTo(i.x.toFloat(), i.y.toFloat())
            lineTo(d.x.toFloat(), d.y.toFloat())
            lineTo(b.x.toFloat(), b.y.toFloat())
            lineTo(a.x.toFloat(), a.y.toFloat())
            lineTo(k.x.toFloat(), k.y.toFloat())
            close()
        }
        return pathC
    }


    /**
     * 获取f点在右下角的pathA
     */
    private fun getPathAFromLowerRight(): Path {
        return pathA.apply {
            reset()
            lineTo(0f, height.toFloat())
            lineTo(c.x.toFloat(), c.y.toFloat())
            quadTo(e.x.toFloat(), e.y.toFloat(), b.x.toFloat(), b.y.toFloat())
            lineTo(a.x.toFloat(), a.y.toFloat())
            lineTo(k.x.toFloat(), k.y.toFloat())
            quadTo(h.x.toFloat(), h.y.toFloat(), j.x.toFloat(), j.y.toFloat())
            lineTo(width.toFloat(), 0f)
            close()
        }
    }

    /**
     * 计算各点坐标
     * @param a
     * @param f
     */
    private fun calcPointsXY(a: Point, f: Point) {
        g.x = (a.x + f.x) / 2
        g.y = (a.y + f.y) / 2

        e.x = g.x - (f.y - g.y) * (f.y - g.y) / (f.x - g.x);
        e.y = f.y

        h.x = f.x
        h.y = g.y - (f.x - g.x) * (f.x - g.x) / (f.y - g.y);

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
        lineOne_My_pointOne: Point,
        lineOne_My_pointTwo: Point,
        lineTwo_My_pointOne: Point,
        lineTwo_My_pointTwo: Point
    ): Point {
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

        return Point(pointX, pointY)

    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {

        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                setTouchPoint(event.x,event.y)
            }

            MotionEvent.ACTION_MOVE -> {
                setTouchPoint(event.x,event.y)
            }

        }
        return true
    }

    private fun setTouchPoint(x:Float,y :Float){
        a.x = x.toInt()
        a.y = y.toInt()
        calcPointsXY(a,f)
        postInvalidate()
    }


}