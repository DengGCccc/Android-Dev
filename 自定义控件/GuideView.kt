package com.yy.hiyo.room.roomextend.miniradio

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.graphics.Region
import android.os.Build
import android.os.Handler
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout

import com.opensource.svgaplayer.SVGAImageView
import com.yy.base.logger.MLog
import com.yy.base.taskexecutor.YYTaskExecutor
import com.yy.base.utils.FP
import com.yy.base.utils.ResolutionUtils
import com.yy.framework.core.ui.svga.SvgaLoader
import com.yy.hiyo.room.R

import java.util.ArrayList
import java.util.Arrays

/**
 * Created by Deng on 2019/5/8.
 */
class GuideView : FrameLayout, View.OnClickListener {
    private val rectList = ArrayList<RectF>()
    private var mPaint: Paint? = null
    private var mCardPath: Path? = null
    private var mBgPath: Path? = null
    private var mClipPathExceptionHappened = false
    private val mHideGuide = Runnable { hideGuide() }

    constructor(context: Context) : super(context) {
        createView(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        createView(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        createView(context)
    }

    private fun createView(context: Context) {
        // 4.0 默认开启硬件加速导致canvas.clipPath崩溃
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        }
        LayoutInflater.from(context).inflate(R.layout.layout_miniradio_guide, this, true)
        this.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        if (v === this) {
            YYTaskExecutor.removeTask(mHideGuide)
            hideGuide()
        }
    }

    fun startGuide(vararg views: View?) {
        val myLocatin = IntArray(2)
        getLocationInWindow(myLocatin)

        rectList.clear()
        for (v in views) {
            if (null == v) {
                return
            }

            val location = IntArray(2)
            v.getLocationInWindow(location)

            val left = Math.max(0, location[0] - myLocatin[0] + v.paddingLeft)
            val top = Math.max(0, location[1] - myLocatin[1] + v.paddingTop)
            val width = v.width - v.paddingLeft - v.paddingRight
            val height = v.height - v.paddingTop - v.paddingBottom
            val animRect = RectF(left.toFloat(), top.toFloat(), (left + width).toFloat(), (top + height).toFloat())
            rectList.add(animRect)
        }

        // dispatchDraw 绘制相关
        mPaint = Paint()
        mPaint?.isAntiAlias = true
        mPaint?.color = Color.argb(0x80, 0, 0, 0)
        mPaint?.style = Paint.Style.FILL
        mCardPath = Path()
        mBgPath = Path()

        alpha = 1.0f
        visibility = View.VISIBLE

        invalidate()

        // post(new Runnable() {
        //     @Override
        //     public void run() {
        //         LayoutParams svga = (LayoutParams) mGuideSvga.getLayoutParams();
        //         svga.topMargin = (int) mAnimRect.top - getTop() + ResolutionUtils.dip2Px(24); // 偏下
        //         svga.height = height;
        //         svga.width = width;
        //         mGuideSvga.setLayoutParams(svga);
        //         SvgaLoader.load(mGuideSvga, SVGA_URL, true);
        //     }
        // });

        // checkHide();
    }

    // public void checkHide() {
    //     if (getVisibility() == View.VISIBLE) {
    //         YYTaskExecutor.removeTask(mHideGuide);
    //         YYTaskExecutor.postToMainThread(mHideGuide, 10000);
    //     }
    // }

    private fun hideGuide() {
        val objectAnimator = ObjectAnimator.ofFloat(this, "alpha", 1.0f, 0f)
        objectAnimator.duration = 200
        objectAnimator.start()
        objectAnimator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                // mGuideSvga.stopAnimation();
                visibility = View.GONE

                if (parent is ViewGroup) {
                    YYTaskExecutor.postToMainThread {
                        val parent = parent as ViewGroup
                        parent.removeView(this@GuideView)
                    }
                }
            }
        })
    }

    override fun dispatchDraw(canvas: Canvas) {
        if (null != mPaint && null != mCardPath && !FP.empty(rectList)) {
            mCardPath?.rewind()
            mBgPath?.rewind()
            mBgPath?.addRect(0f, 0f, measuredWidth.toFloat(), measuredHeight.toFloat(), Path.Direction.CW)

            for (rectF in rectList) {
                // mCardPath.addRect(rectF, Path.Direction.CW);
                mCardPath?.addRoundRect(rectF, ResolutionUtils.dip2Px(3f).toFloat(),
                    ResolutionUtils.dip2Px(3f).toFloat(), Path.Direction.CW)
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                mCardPath?.op(mBgPath, Path.Op.REVERSE_DIFFERENCE)
                canvas.drawPath(mCardPath, mPaint)
            } else if (!mClipPathExceptionHappened) { // 发生了异常之后就不再调用
                try {
                    // 4.0 默认开启硬件加速导致崩溃，setLayerType(LAYER_TYPE_SOFTWARE, null)解决，安全起见另外加 try catch
                    canvas.save()
                    canvas.clipPath(mCardPath, Region.Op.XOR)
                    canvas.drawPaint(mPaint)
                    canvas.restore()
                } catch (e: UnsupportedOperationException) {
                    MLog.error("GuideView", e)
                    mClipPathExceptionHappened = true
                }
            }
        }

        super.dispatchDraw(canvas)
    }
}
