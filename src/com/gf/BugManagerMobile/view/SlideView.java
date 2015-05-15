package com.gf.BugManagerMobile.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.*;
import android.widget.HorizontalScrollView;
import android.widget.Scroller;
import com.gf.BugManagerMobile.R;
import com.nineoldandroids.view.ViewHelper;

/**
 * 仿QQ的侧滑面板
 * Created by Administrator on 5/10 0010.
 */
public class SlideView extends HorizontalScrollView {
    private static final String TAG = "SlideView";

    /**
     * 状态枚举类
     */
    public enum SlideState {
        OPEN, CLOSE
    }

    /**
     * 当前状态
     */
    private SlideState slideState = SlideState.CLOSE;

    private boolean once = true;
    private int screenWidth = 0;
    private int leftMenuWidth = 0;
    private ViewGroup leftMenuLyt;
    private ViewGroup centerContentLyt;
    private View centerContentCoverView = null;
    private Scroller mScroller;
    private int mScaleTouchSlop;

    public SlideView(Context context) {
        this(context, null);
    }

    public SlideView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlideView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.setOverScrollMode(OVER_SCROLL_NEVER);
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        screenWidth = wm.getDefaultDisplay().getWidth();

        mScroller = new Scroller(context);
        mScaleTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (once) {
            leftMenuLyt = (ViewGroup) findViewById(R.id.left_menu_lyt);
            centerContentLyt = (ViewGroup) findViewById(R.id.center_content_lyt);
            centerContentCoverView = findViewById(R.id.center_content_cover);
            centerContentCoverView.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        toggleMenu();
                    }
                    return true;
                }
            });
            centerContentLyt.getLayoutParams().width = screenWidth;
            once = false;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed) {
            this.leftMenuWidth = leftMenuLyt.getMeasuredWidth();
            this.scrollTo(this.leftMenuWidth, 0);
            this.slideState = SlideState.CLOSE;
        }
        if (slideState == SlideState.CLOSE) {
            centerContentCoverView.setVisibility(View.GONE);
        } else if (slideState == SlideState.OPEN) {
            centerContentCoverView.bringToFront();
            centerContentCoverView.setVisibility(View.VISIBLE);
            centerContentCoverView.requestFocus();
        }
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        /* 计算基本的比例 */
        float scale = l / (float) leftMenuLyt.getWidth();
        /* 处理左边的菜单布局 */
        ViewHelper.setTranslationX(leftMenuLyt, l * 0.6f);
        float leftMenuLytScale = 1.0f - scale * 0.3f;
        float leftMenuLytAlpha = 0.6f + 0.4f * (1 - scale);
        ViewHelper.setScaleX(leftMenuLyt, leftMenuLytScale);
        ViewHelper.setScaleY(leftMenuLyt, leftMenuLytScale);
        ViewHelper.setAlpha(leftMenuLyt, leftMenuLytAlpha);
        /* 处理中间的内容布局 */
        float centerContentLytScale = 0.7f + 0.3f * scale;
        ViewHelper.setPivotX(centerContentLyt, 0);
        ViewHelper.setPivotY(centerContentLyt, centerContentLyt.getHeight() / 2);
        ViewHelper.setScaleX(centerContentLyt, centerContentLytScale);
        ViewHelper.setScaleY(centerContentLyt, centerContentLytScale);
        if (slideState == SlideState.OPEN) {
            centerContentCoverView.requestFocus();
        }
    }

    /**
     * 打开/关闭侧滑菜单
     */
    public void toggleMenu() {
        if (mScroller == null)
            mScroller = new Scroller(getContext());
        if (!mScroller.isFinished())
            mScroller.forceFinished(true);
        int scrollX = getScrollX();

        if (this.slideState == SlideState.CLOSE) {
            mScroller.startScroll(scrollX, 0, -scrollX, 0);
            invalidate();
            this.slideState = SlideState.OPEN;
            centerContentCoverView.bringToFront();
            centerContentCoverView.setVisibility(View.VISIBLE);
            centerContentCoverView.requestFocus();
        } else {
            mScroller.startScroll(scrollX, 0, leftMenuWidth - scrollX, 0);
            invalidate();
            this.slideState = SlideState.CLOSE;
            centerContentCoverView.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        optTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 处理触摸事件
     *
     * @param e
     */
    private void optTouchEvent(MotionEvent e) {
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                if (mScroller == null)
                    mScroller = new Scroller(getContext());
                if (!mScroller.isFinished())
                    mScroller.forceFinished(true);
                int scrollX = getScrollX();
                if (getScrollX() >= leftMenuWidth / 2) {// 出界部分超过了1/2，变成关闭状态
                    mScroller.startScroll(scrollX, 0, leftMenuWidth - scrollX, 0);
                    invalidate();
                    this.slideState = SlideState.CLOSE;
                    centerContentCoverView.setVisibility(View.GONE);
                } else {// 出界部分小于1/2，变成打开状态
                    mScroller.startScroll(scrollX, 0, -scrollX, 0);
                    invalidate();
                    this.slideState = SlideState.OPEN;
                    centerContentCoverView.bringToFront();
                    centerContentCoverView.setVisibility(View.VISIBLE);
                    centerContentCoverView.requestFocus();
                }
                break;
        }
    }

    /**
     * 获得当前状态
     *
     * @return
     */
    public SlideState getSlideState() {
        return this.slideState;
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            this.scrollTo(mScroller.getCurrX(), 0);
            postInvalidate();
        }
    }

}
