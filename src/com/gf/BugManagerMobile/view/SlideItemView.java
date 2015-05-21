package com.gf.BugManagerMobile.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.Scroller;
import com.gf.BugManagerMobile.R;

/**
 * 每一项的横向滚动条
 * Created by Administrator on 5/15 0015.
 */
public class SlideItemView extends HorizontalScrollView {
    private static final String TAG = "SlideItemView";

    private View mFirstChildView = null;
    private View mSecondChildView = null;
    private int mScreenWidth = 0;
    private Scroller mScroller;
    private boolean once = true;

    public enum SlideItemState {
        Open, Close
    }

    private SlideItemState mSlideItemState = SlideItemState.Close;

    public SlideItemView(Context context) {
        this(context, null);
    }

    public SlideItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlideItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        mScreenWidth = wm.getDefaultDisplay().getWidth();
        mScroller = new Scroller(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (once) {
            mFirstChildView = findViewById(R.id.slide_item_first);
            mSecondChildView = findViewById(R.id.slide_item_second);
            mFirstChildView.getLayoutParams().width = mScreenWidth;
            once = false;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent e) {
        if (e.getAction() == MotionEvent.ACTION_UP) {
            if (mScroller == null)
                mScroller = new Scroller(getContext());
            if (!mScroller.isFinished())
                mScroller.forceFinished(true);
            int scrollX = getScrollX();
            int secondWidth = mSecondChildView.getMeasuredWidth();
            if (scrollX > secondWidth / 2) {
                mScroller.startScroll(scrollX, 0, secondWidth - scrollX, 0);
                invalidate();
                mSlideItemState = SlideItemState.Open;
            } else {
                mScroller.startScroll(scrollX, 0, -scrollX, 0);
                invalidate();
                mSlideItemState = SlideItemState.Close;
            }

        }
        return super.dispatchTouchEvent(e);
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            this.scrollTo(mScroller.getCurrX(), 0);
            postInvalidate();
        }
    }

    /**
     * 关闭
     */
    public void smoothCloseItem() {
        if (mSlideItemState == SlideItemState.Open) {
            if (mScroller == null)
                mScroller = new Scroller(getContext());
            if (!mScroller.isFinished())
                mScroller.forceFinished(true);
            int scrollX = getScrollX();
            mScroller.startScroll(scrollX, 0, -scrollX, 0);
            invalidate();
            mSlideItemState = SlideItemState.Close;
        }
    }

    /**
     * 关闭
     */
    public void closeItem() {
        if (mSlideItemState == SlideItemState.Open) {
            scrollTo(0, 0);
        }
    }

    /**
     * 打开
     */
    public void smoothOpenItem() {
        if (mSlideItemState == SlideItemState.Close) {
            if (mScroller == null)
                mScroller = new Scroller(getContext());
            if (!mScroller.isFinished())
                mScroller.forceFinished(true);
            int scrollX = getScrollX();
            mScroller.startScroll(scrollX, 0, mSecondChildView.getMeasuredWidth() - scrollX, 0);
            invalidate();
            mSlideItemState = SlideItemState.Close;
        }
    }

    /**
     * 打开
     */
    public void openItem() {
        if (mSlideItemState == SlideItemState.Close) {
            scrollTo(mSecondChildView.getMeasuredWidth(), 0);
        }
    }

}
