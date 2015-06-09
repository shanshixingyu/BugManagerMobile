package com.gf.BugManagerMobile.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import com.gf.BugManagerMobile.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * 显示图片的对话框
 * Created by GuLang on 2015-06-08.
 */
public class ShowImageDialog extends Dialog {
    private static final String TAG = "ShowImageDialog";

    public enum TouchState {
        Drag, Zoon, None
    }

    private ImageView mShowImgv;
    private String mShowUrl;

    public ShowImageDialog(Context context) {
        this(context, 0);
    }

    public ShowImageDialog(Context context, int theme) {
        super(context, R.style.CustomDialogTheme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_show_image);

        mShowImgv = (ImageView) findViewById(R.id.show_image_imgv);
        mShowImgv.setOnTouchListener(onTouchListener);
        // mShowImgv.setClickable(true);
        // mShowImgv.setFocusableInTouchMode(true);
        mShowImgv.setAdjustViewBounds(true);
        mShowImgv.setScaleType(ImageView.ScaleType.FIT_CENTER);
        // mShowImgv.setFocusable(true);
        // mShowImgv.setOverScrollMode(ImageView.OVER_SCROLL_IF_CONTENT_SCROLLS);
        if (!ImageLoader.getInstance().isInited())
            ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(getContext()));

        if (mShowUrl != null)
            ImageLoader.getInstance().displayImage(mShowUrl, mShowImgv);
    }

    public void setShowImage(String url) {
        mShowUrl = url;
        if (mShowImgv != null) {
            mShowImgv.setImageDrawable(null);
            if (url == null)
                return;
            ImageLoader.getInstance().displayImage(url, mShowImgv);
        }
    }

    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        private TouchState touchState = TouchState.None;
        private PointF startPointF = new PointF();
        private Matrix currentMatrix = new Matrix();

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            Log.i(TAG, "onTouch " + event.getAction());
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    touchState = TouchState.Drag;
                    startPointF.set(event.getX(), event.getY());
                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    touchState = TouchState.Zoon;

                    break;
                case MotionEvent.ACTION_MOVE:
                    if (touchState == TouchState.Drag) {
                        dragImage(startPointF, event);
                    } else if (touchState == TouchState.Zoon) {

                    }

                    break;
                case MotionEvent.ACTION_POINTER_UP:

                    break;
            }
            return true;
        }

        private void dragImage(PointF startPointF, MotionEvent event) {
            Log.i(TAG, "dragImage");
            float dx = event.getX() - startPointF.x;
            float dy = event.getY() - startPointF.y;

            currentMatrix.set(mShowImgv.getImageMatrix());
            currentMatrix.postTranslate(dx, dy);
            mShowImgv.setImageMatrix(currentMatrix);
            mShowImgv.invalidate();
        }

    };

}
