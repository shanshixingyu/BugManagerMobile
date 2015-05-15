package com.gf.BugManagerMobile.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.gf.BugManagerMobile.R;
import com.gf.BugManagerMobile.utils.SizeUtils;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

import java.util.ArrayList;
import java.util.List;

/**
 * 卫星菜单
 * Created by Administrator on 5/11 0011.
 */
public class SatelliteMenuLyt extends ViewGroup {
    private static final String TAG = "SatelliteMenuLyt";

    public enum Status {
        OPEN, CLOSE,
    }

    private Status status = Status.CLOSE;

    private float radius;
    private int distanceRight;
    private int distanceBottom;

    private View centerView;
    private int centerViewLeft;
    private int centerViewTop;
    private OnSatelliteMenuItemClickListener onSatelliteMenuItemClickListener = null;

    public SatelliteMenuLyt(Context context) {
        this(context, null);
    }

    public SatelliteMenuLyt(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SatelliteMenuLyt(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SatelliteMenu, defStyle, 0);
            radius = a.getDimensionPixelSize(R.styleable.SatelliteMenu_radius, SizeUtils.dp2px(context, 150));
            distanceRight = a.getDimensionPixelSize(R.styleable.SatelliteMenu_distanceRight, 0);
            distanceBottom = a.getDimensionPixelSize(R.styleable.SatelliteMenu_distanceBottom, 0);
            a.recycle();
        } else {
            radius = SizeUtils.dp2px(context, 150);
            distanceRight = 0;
            distanceBottom = 0;
        }
    }

    private View getCenterView() {
        if (centerView != null)
            return centerView;
        ImageView imageView = new ImageView(getContext());
        int size = SizeUtils.dp2px(getContext(), 43);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(size, size);
        imageView.setLayoutParams(lp);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageView.setImageResource(R.drawable.ic_satellite_manager);
        imageView.setClickable(true);
        return imageView;
    }

    private boolean once = true;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int childCount = getChildCount();
        if (childCount < 2)
            throw new IllegalStateException("至少需要2个子控件");
        for (int i = 0; i < childCount; i++) {
            measureChild(getChildAt(i), widthMeasureSpec, heightMeasureSpec);
        }
        if (once) {
            this.centerView = getCenterView();
            addView(centerView);
            measureChild(centerView, widthMeasureSpec, heightMeasureSpec);
            once = false;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private int drawWidthRight;
    private int drawHeightBottom;

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (changed) {
            // 获得整个布局的宽高
            drawWidthRight = getMeasuredWidth() - getPaddingRight() - distanceRight;
            drawHeightBottom = getMeasuredHeight() - getPaddingBottom() - distanceBottom;

            // 获得主按钮的宽高
//            centerView = getChildAt(0);
            int centerImgvWidth = centerView.getMeasuredWidth();
            int centerImgvHeight = centerView.getMeasuredHeight();
            centerViewLeft = drawWidthRight - centerImgvWidth;
            centerViewTop = drawHeightBottom - centerImgvHeight;
            centerView.layout(centerViewLeft, centerViewTop, drawWidthRight, drawHeightBottom);
            centerView.setOnClickListener(centerItemListener);
            centerView.bringToFront();// 将第一个子控件放到界面最前面，也就是放到了最后一个索引上

            layoutBtn();
        }
    }

    /**
     * 加载所有的按钮(除了最后一个，因为layout中已经将位置调整好了)
     */
    private void layoutBtn() {
        View childView;
        int childCount = getChildCount();
        double mPerAngle = Math.PI / (2 * (childCount - 2));
        int tempRight;
        int tempBottom;
        for (int i = 0; i < childCount - 1; i++) {
            /* 计算显示位置 */
            tempRight = (int) (drawWidthRight - radius * Math.sin(i * mPerAngle));
            tempBottom = (int) (drawHeightBottom - radius * Math.cos(i * mPerAngle));
            childView = getChildAt(i);
            Point point = new Point();
            point.x = tempRight - childView.getMeasuredWidth();
            point.y = tempBottom - childView.getMeasuredHeight();
            childView.setTag(new MenuItemViewHolder(i, point));
            childView.setOnClickListener(otherItemOnClickListener);

            /* 初始显示位置 */
            childView.layout(centerViewLeft, centerViewTop, centerViewLeft + childView.getMeasuredWidth(),
                centerViewTop + childView.getMeasuredHeight());
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 当菜单打开后，不允许拦截响应事件
        if (status == Status.OPEN)
            return true;
        return super.onTouchEvent(event);
    }

    /**
     * 中间的按钮的点击事件处理
     */
    private OnClickListener centerItemListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            toggleMenu();
        }
    };
    /**
     * 其它按钮的点击事件的处理
     */
    private OnClickListener otherItemOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (status == Status.CLOSE)
                return;

            /* 变大动画 */
            AnimatorSet biggerAnimatorSet = new AnimatorSet();
            biggerAnimatorSet.playTogether(ObjectAnimator.ofFloat(v, "scaleX", 1.5f),
                ObjectAnimator.ofFloat(v, "scaleY", 1.5f));
            /* 缩小动画 */
            AnimatorSet smallerAnimatorSet = new AnimatorSet();
            smallerAnimatorSet.playTogether(ObjectAnimator.ofFloat(v, "scaleX", 1f),
                ObjectAnimator.ofFloat(v, "scaleY", 1f));
            /* 动画播放顺序 */
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playSequentially(biggerAnimatorSet, smallerAnimatorSet);
            animatorSet.setDuration(200);
            animatorSet.start();
            /* 回调 */
            if (onSatelliteMenuItemClickListener != null) {
                MenuItemViewHolder itemViewHolder = (MenuItemViewHolder) v.getTag();
                onSatelliteMenuItemClickListener.onMenuItemClick(itemViewHolder.index, v);
            }
            /* 收回菜单 */
            // toggleMenu();
            closeMenu(400);
        }
    };

    /**
     * 菜单显示/关闭
     */
    public void toggleMenu() {
        if (status == Status.CLOSE) {
            openMenu();
        } else {
            closeMenu(0);
        }
    }

    /**
     * 关闭卫星菜单
     * @param delay
     */
    private void closeMenu(long delay) {
        // 每一项都从展开位置回到中间的所在的位置
        ObjectAnimator.ofFloat(centerView, "rotation", 45, 0).setDuration(200).start();
        View child;
        MenuItemViewHolder itemHolder;
        int childCount = getChildCount();
        for (int i = 0; i < childCount - 1; i++) {
            child = getChildAt(i);
            itemHolder = (MenuItemViewHolder) child.getTag();
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether(
                ObjectAnimator.ofFloat(child, "translationX", itemHolder.openLocation.x - centerViewLeft, 0f),
                ObjectAnimator.ofFloat(child, "translationY", itemHolder.openLocation.y - centerViewTop, 0f),
                ObjectAnimator.ofFloat(child, "rotation", 0, 1080));
            animatorSet.setDuration(150 + 75 * i);// 动画时长
            animatorSet.setStartDelay(delay);
            animatorSet.start();
        }
        status = Status.CLOSE;
    }

    private void openMenu() {
        // 每一项都从中间的所在的位置到展开位置
        ObjectAnimator.ofFloat(centerView, "rotation", 0, 45).setDuration(200).start();

        View child;
        MenuItemViewHolder itemHolder;
        int childCount = getChildCount();
        for (int i = 0; i < childCount - 1; i++) {
            child = getChildAt(i);
            itemHolder = (MenuItemViewHolder) child.getTag();
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether(
                ObjectAnimator.ofFloat(child, "translationX", 0f, itemHolder.openLocation.x - centerViewLeft),
                ObjectAnimator.ofFloat(child, "translationY", 0f, itemHolder.openLocation.y - centerViewTop),
                ObjectAnimator.ofFloat(child, "rotation", 0, 1080));
            animatorSet.setDuration(150 + 75 * i);// 动画时长
            animatorSet.start();
        }
        status = Status.OPEN;
    }

    private class MenuItemViewHolder {
        public int index;
        public Point openLocation;

        public MenuItemViewHolder(int index, Point location) {
            this.index = index;
            this.openLocation = location;
        }
    }

    public interface OnSatelliteMenuItemClickListener {
        public void onMenuItemClick(int index, View childView);
    }

    /**
     * 设置事件监听
     * @param listener
     */
    public void setOnSatelliteMenuItemClickListener(OnSatelliteMenuItemClickListener listener) {
        this.onSatelliteMenuItemClickListener = listener;
    }

}
