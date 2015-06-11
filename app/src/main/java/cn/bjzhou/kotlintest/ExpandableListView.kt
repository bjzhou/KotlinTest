package cn.bjzhou.kotlintest

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.AbsListView
import android.widget.ImageView
import android.widget.ListView
import org.jetbrains.anko.transcriptMode

/**
 * Created by bjzhou on 15-6-11.
 */
public class ExpandableListView @jvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : ListView(context, attrs, defStyleAttr), AbsListView.OnScrollListener {
    private var mScrollView: ImageView
    private var mScrollRect: Rect

    init {
        mScrollRect = Rect(0,0,0,0)
        mScrollView = ImageView(getContext());
        mScrollView.setImageResource(R.drawable.abc_btn_check_material);
        getOverlay().add(mScrollView)
        setOnScrollListener(this)
    }

    private var mItemsHeight: Int = 0

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super<ListView>.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthSpec = View.MeasureSpec.makeMeasureSpec(48, View.MeasureSpec.EXACTLY)
        val heightSpec = View.MeasureSpec.makeMeasureSpec(64, View.MeasureSpec.EXACTLY)
        mScrollView.measure(widthSpec, heightSpec)
        mItemsHeight = measureItemsHeight()
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super<ListView>.onLayout(changed, l, t, r, b)

        if (mScrollRect.top == 0) {
            setScrollPos(0, 0)
        }
    }

    fun setScrollPos(prevPos: Int, y: Int) {
        val dy = (y * getScrollRatio(true)).toInt()
        Log.d("bjzhou", "setScrollPos: " + (prevPos + dy) )
        mScrollRect.left = getWidth() - mScrollView.getMeasuredWidth()
        mScrollRect.top = prevPos + dy
        mScrollRect.right = getWidth()
        mScrollRect.bottom = prevPos + dy + mScrollView.getMeasuredHeight()
        mScrollView.layout(mScrollRect.left, mScrollRect.top, mScrollRect.right, mScrollRect.bottom)
    }

    var prevY = 0
    var prevTop = 0
    var prevScrollY = 0
    var beginDrag = false

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        //Log.d("bjzhou", "scroll touch : " + getHeight() + " " + getMeasuredHeight())
        when (ev.getAction()) {
            MotionEvent.ACTION_DOWN -> {
                if (mScrollRect.contains(ev.getX().toInt(), ev.getY().toInt())) {
                    beginDrag = true
                }
                if (beginDrag) {
                    prevY = ev.getY().toInt()
                    prevTop = mScrollView.getTop()
                    prevScrollY = getListScrollY()
                }
            }
            MotionEvent.ACTION_MOVE -> {
                if (beginDrag) {
                    scrollToPosition(prevScrollY, ev.getY().toInt() - prevY)
                    //setScrollPos(prevTop + ev.getY().toInt() - prevY)
                }
            }
            MotionEvent.ACTION_UP -> {
                if (beginDrag) {
                    beginDrag = false
                }
            }
        }
        if (beginDrag) {
            return true
        }
        return super<ListView>.onTouchEvent(ev)
    }

    override fun onScroll(view: AbsListView, firstVisibleItem: Int, visibleItemCount: Int, totalItemCount: Int) {
        if (totalItemCount == 0) return
        Log.d("bjzhou", "onScroll: " + prevTop + " " + getListScrollY() + " " + prevScrollY)
        setScrollPos(prevTop, getListScrollY() - prevScrollY)
    }

    override fun onScrollStateChanged(view: AbsListView?, scrollState: Int) {
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
            prevTop = mScrollView.getTop()
            prevScrollY = getListScrollY()
        }
    }

    fun getListScrollY(): Int {
        val c = getChildAt(0);
        return -c.getTop() + getFirstVisiblePosition() * c.getHeight();
    }

    fun measureItemsHeight(): Int {
        var totalHeight = 0
        for (i in 0..getAdapter().getCount() - 1) {
            val view = getAdapter().getView(i, null, this);
            view.measure(
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            totalHeight += view.getMeasuredHeight();
        }
        return totalHeight
    }

    fun scrollToPosition(prevY: Int, y: Int) {
        val dy = (y * getScrollRatio(false)).toInt()
        var totalHeight = 0
        for (i in 0..getAdapter().getCount() - 1) {
            val view = getAdapter().getView(i, null, this);
            view.measure(
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            totalHeight += view.getMeasuredHeight();

            if (totalHeight >= prevY + dy) {
                Log.d("bjzhou", "scrollToPosition: " + (prevY + dy))
                setSelectionFromTop(i, totalHeight - view.getMeasuredHeight() - (prevY + dy))
                return
            }
        }
    }

    fun getScrollRatio(reverse: Boolean): Float {
        val ratio = (mItemsHeight - getHeight()) / (getHeight() - mScrollView.getMeasuredHeight()).toFloat()
        if (reverse) {
            return 1 / ratio
        }
        return ratio
    }
}
