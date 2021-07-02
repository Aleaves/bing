package com.app.smartcode.ui

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.Bundle
import android.util.TypedValue
import androidx.appcompat.app.AppCompatActivity
import com.app.smartcode.R
import com.app.smartcode.danmuk.DanmuControl
import kotlinx.android.synthetic.main.activity_danmuk.*
import master.flame.danmaku.controller.DrawHandler
import master.flame.danmaku.controller.IDanmakuView
import master.flame.danmaku.danmaku.model.*
import master.flame.danmaku.danmaku.model.android.DanmakuContext
import master.flame.danmaku.danmaku.model.android.Danmakus
import master.flame.danmaku.danmaku.model.android.SpannedCacheStuffer
import master.flame.danmaku.danmaku.parser.BaseDanmakuParser
import com.app.smartcode.danmuk.Danmu


class DanmakuActivity : AppCompatActivity() {

    private lateinit var mDanmuControl: DanmuControl

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_danmuk)
        //initDanmaku()
        bt.setOnClickListener {
            addDanmaku()
        }
        bt1.setOnClickListener {
            setData()
        }

        mDanmuControl =  DanmuControl(this)
        mDanmuControl.setDanmakuView(danmakuView)
    }

    private var danmakuContext: DanmakuContext? = null

    private fun initDanmaku() {
        // 设置最大显示行数(滚动弹幕最大显示行数)
        val maxLinesPair: HashMap<Int, Int> = HashMap()
        maxLinesPair[BaseDanmaku.TYPE_SCROLL_RL] = 3
        // 设置是否禁止重叠
        val overlappingEnablePair = HashMap<Int, Boolean>()
        overlappingEnablePair[BaseDanmaku.TYPE_SCROLL_RL] = true
        overlappingEnablePair[BaseDanmaku.TYPE_FIX_TOP] = true

        danmakuContext = DanmakuContext.create().also {
            it.setDanmakuStyle(IDisplayer.DANMAKU_STYLE_STROKEN, 3f)
                .setDuplicateMergingEnabled(false)
                .setScrollSpeedFactor(1.5f) //弹幕移动速度，倍数
                .setScaleTextSize(1.0f) //字体
                .setCacheStuffer(backgroundSpan, null)
                .setMaximumLines(maxLinesPair)
                .preventOverlapping(overlappingEnablePair)
                .setDanmakuMargin(dp2px(12f))
        }

        danmakuView.enableDanmakuDrawingCache(true)
        danmakuView.setCallback(object : DrawHandler.Callback {
            override fun prepared() {
                danmakuView.start()
            }

            override fun danmakuShown(danmaku: BaseDanmaku?) {
            }

            override fun updateTimer(timer: DanmakuTimer?) {
            }

            override fun drawingFinished() {
            }

        })
        danmakuView.setOnDanmakuClickListener(object :IDanmakuView.OnDanmakuClickListener{
            override fun onViewClick(view: IDanmakuView?): Boolean {
                return false
            }

            override fun onDanmakuClick(danmakus: IDanmakus?): Boolean {
                val last = danmakus?.last()
                if(null != last){
                    return true
                }
                return false
            }

            override fun onDanmakuLongClick(danmakus: IDanmakus?): Boolean {
                return false
            }
        })
        danmakuView.prepare(parser, danmakuContext)

    }

    /**
     * 绘制弹幕背景
     */
    private val backgroundSpan = object : SpannedCacheStuffer() {
        // 通过扩展SimpleTextCacheStuffer或SpannedCacheStuffer个性化你的弹幕样式
        val paint: Paint = Paint()

        override fun drawBackground(danmaku: BaseDanmaku, canvas: Canvas, left: Float, top: Float) {
            paint.color = Color.parseColor("#80000000")
            val radius = dp2px(15f).toFloat()
            canvas.drawRoundRect(
                RectF(left, top, left + danmaku.paintWidth, top + danmaku.paintHeight),
                radius, radius, paint)
        }

        override fun drawStroke(danmaku: BaseDanmaku?, lineText: String?, canvas: Canvas?, left: Float, top: Float, paint: Paint?) {
            // 禁用描边绘制
        }
    }

    private val backgroundSpan1 = object : SpannedCacheStuffer() {
        // 通过扩展SimpleTextCacheStuffer或SpannedCacheStuffer个性化你的弹幕样式
        val paint: Paint = Paint()

        override fun drawBackground(danmaku: BaseDanmaku, canvas: Canvas, left: Float, top: Float) {
            paint.color = Color.RED
            val radius = dp2px(15f).toFloat()
            canvas.drawRoundRect(
                RectF(left, top, left + danmaku.paintWidth, top + danmaku.paintHeight),
                radius, radius, paint)
            danmaku.userId
        }

        override fun drawStroke(danmaku: BaseDanmaku?, lineText: String?, canvas: Canvas?, left: Float, top: Float, paint: Paint?) {
            // 禁用描边绘制
        }
    }

    private val parser: BaseDanmakuParser = object : BaseDanmakuParser() {
        override fun parse(): IDanmakus? {
            return Danmakus()
        }
    }

    private var danmukuDuration: Long = 5000
    private fun addDanmaku(withBorder: Boolean = false) {
        danmakuContext?.let {
            val danmaku = it.mDanmakuFactory?.createDanmaku(BaseDanmaku.TYPE_SCROLL_RL)?.apply {
                text = "我的世界我我的世界\uD83D\uDC4D"+System.currentTimeMillis()
                padding = dp2px(7f)
                textSize = dp2px(20f).toFloat()
                textColor = Color.RED
                duration = Duration(danmukuDuration)
                isLive = true
                time = danmakuView.currentTime
                priority = 0
            } ?: return
            danmakuView.addDanmaku(danmaku)
        }
    }

    fun dp2px(dpVal: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dpVal, resources.displayMetrics).toInt()
    }

    override fun onPause() {
        super.onPause()
        mDanmuControl.pause()
    }

    override fun onResume() {
        super.onResume()
        mDanmuControl.resume()
    }

    override fun onDestroy() {
        super.onDestroy()
        mDanmuControl.destroy()
    }


    private fun setData() {
        val danmus = mutableListOf<Danmu>()
        val danmu1 = Danmu(0, 1, "Comment", R.mipmap.ic_default_header, "这是一条弹幕啦啦啦")
        val danmu2 = Danmu(0, 2, "Comment", R.mipmap.ic_default_header, "这是一条弹幕啦啦啦")
        val danmu3 = Danmu(0, 3, "Comment", R.mipmap.ic_default_header, "这是一条弹幕啦啦啦")
        val danmu4 = Danmu(0, 1, "Comment", R.mipmap.wat, "这又是一条弹幕啦啦啦")
        val danmu5 = Danmu(0, 2, "Comment", R.mipmap.wat, "这是一条弹幕啦啦啦")
        val danmu6 = Danmu(0, 3, "Comment", R.mipmap.wat, "这还是一条弹幕啦啦啦")
        danmus.add(danmu1)
        danmus.add(danmu2)
        danmus.add(danmu3)
        danmus.add(danmu4)
        danmus.add(danmu5)
        danmus.add(danmu6)
        mDanmuControl.addDanmuList(danmus)
    }
}