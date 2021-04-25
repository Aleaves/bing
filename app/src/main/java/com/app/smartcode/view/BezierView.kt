package com.app.smartcode.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View

class BezierView @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null, defStyleAttr:Int = 0) :View(context,attributeSet,defStyleAttr){

    private val mPaint = Paint().apply {
        color = Color.RED
        isAntiAlias = true
        strokeWidth = 4f
        style = Paint.Style.STROKE
    }

    private val mPath = Path()

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
//        mPath.moveTo(200f,200f)
//        mPath.quadTo(500f,100f,800f,500f) //二阶贝塞尔曲线
//        mPath.quadTo(800f,300f,1000f,800f)
//        canvas.drawPath(mPath,mPaint)

        //三阶贝塞尔曲线
//        mPath.moveTo(300f, 500f);
//        mPath.cubicTo(500f, 100f, 600f, 1200f, 800f, 500f)
//        canvas.drawPath(mPath,mPaint)

        canvas.drawColor(Color.RED)


    }
}