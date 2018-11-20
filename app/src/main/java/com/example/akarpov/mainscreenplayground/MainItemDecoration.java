package com.example.akarpov.mainscreenplayground;


import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.support.v8.renderscript.RenderScript;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;

import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by a.karpov on 12.11.2018.
 */

public class MainItemDecoration extends RecyclerView.ItemDecoration {

    private int colorActive = 0xFFFF7800;

    private int colorInactive = 0xFFFF7800;

    private static final float DP = Resources.getSystem().getDisplayMetrics().density;

    private final float mCircleRadiusInactive = (DP * 4);

    private final float mCircleRadiusActive = (DP * 8);

    private int mDirection = 0;

    private int blurRadius = 20;

    /**
     * Height of the space the indicator takes up at the bottom of the view.
     */
    private final int mIndicatorHeight = (int) (DP * 16);

    /**
     * Indicator width.
     */
    private final float mIndicatorItemLength = DP * 12;

    /**
     * Padding between indicators.
     */
    private final float mIndicatorItemPadding = DP * 4;

    private final Interpolator mInterpolator = new AccelerateDecelerateInterpolator();

    private final Paint mPaint = new Paint();

    private boolean inited = false;

    private int itemBelow = 0;

    View topView;

    View bottomView;

    RenderScript rs;

    Bitmap bmp;

    int backgroundwidth = -1;

    int backgroundHeight = -1;

    Scheduler bs = Schedulers.newThread();

    public MainItemDecoration() {
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setAntiAlias(true);
        mPaint.setColor(colorActive);
        mPaint.setMaskFilter(new BlurMaskFilter(blurRadius, BlurMaskFilter.Blur.NORMAL));
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        if (rs == null) {
            rs = RenderScript.create(parent.getContext());
        }
        int itemCount = parent.getAdapter().getItemCount();

        if (itemCount < 1) {
            return;
        }

        topView = parent.findViewHolderForAdapterPosition(0).itemView;
        topView.setDrawingCacheEnabled(true);

        bmp = topView.getDrawingCache();

        backgroundwidth = topView.getMeasuredWidth();
        backgroundHeight = bottomView.getBottom() - topView.getTop();

        Bitmap b = createBackground(bmp);
//        c.drawBitmap(b, topView.getLeft(), topView.getTop(), null);
        c.drawBitmap(b, 0, 0, null);
        b.recycle();
    }

    private Bitmap createBackground(Bitmap item) {
        Bitmap bitmap =
//                Bitmap.createBitmap(item);
                Bitmap.createBitmap(item.getWidth(), backgroundHeight, Bitmap.Config.ARGB_8888);

        Canvas background = new Canvas(bitmap);
//
//        drawRect(background, 0, 0,
//                bottomView != null ? bottomView.getRight(): topView.getRight(),
//                bottomView != null ? bottomView.getBottom() : topView.getBottom());
//
        drawRect(background, blurRadius, blurRadius + 4,
                item.getWidth() + blurRadius*3,
                100);


//            Create allocation from Bitmap
//            Allocation allocation = Allocation.createFromBitmap(rs, bitmap);
//
//            Type t = allocation.getType();
//
//            //Create allocation with the same type
//            Allocation blurredAllocation = Allocation.createTyped(rs, t);
//
//            //Create script
//            ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
//            //Set blur radius (maximum 25.0)
//            blurScript.setRadius(20f);
//                //Set input for script
//            blurScript.setInput(allocation);
//                //Call script for output allocation
//            blurScript.forEach(blurredAllocation);
//
//                //Copy script result into bitmap
//            blurredAllocation.copyTo(bitmap);
//
//                //Destroy everything to free memory
//            allocation.destroy();
//            blurredAllocation.destroy();
//            blurScript.destroy();
//            t.destroy();
//            rs.destroy();
        return bitmap;
    }

    private void drawRect(Canvas c, int left, int top, int right, int bottom) {
        Rect r = new Rect(left, top, right, bottom);
        c.drawRect(r, mPaint);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        bottomView = view;
//        outRect.left = -mIndicatorHeight;
//        outRect.right = -mIndicatorHeight;
//        outRect.top = mIndicatorHeight;
//        outRect.bottom = mIndicatorHeight;
        if (!inited) {
            parent.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    if (dx < 0) {
                        mDirection = -1;
                    } else {
                        mDirection = 0;
                    }
                }
            });
            inited = true;
        }
    }


}
