package com.gowbing.kunzhong.view.radiobutton;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.RadioButton;

import com.gowbing.kunzhong.AppApplication;
import com.gowbing.kunzhong.R;
import com.gowbing.kunzhong.util.DensityUtil;

/**
 * Created by Administrator on 2018-8-28.
 */

public class MyRadioButton extends RadioButton{
    private Paint mPaint;
    private boolean isShowDot;
    private boolean isShowNumberDot;

    private int width;
    private int height;

    /**
     * 顶部图片宽
     */
    private int IntrinsicWidth;

    private String numberText;
    /**
     * 圆点和未读消息的坐标
     * */
    private float pivotX;
    private float pivotY;
    /**
     * 圆点半径
     * */
    private final int circleDotRadius = DensityUtil.dip2px(AppApplication.getInstance(),3);

    public MyRadioButton(Context context) {
        this(context, null);
    }

    public MyRadioButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        Drawable drawable = getCompoundDrawables()[1];
        if (drawable != null) {
            IntrinsicWidth = drawable.getIntrinsicWidth();
        }
        /**
         * 给RadioButton增加一定的padding
         * */
        if (getPaddingBottom() == 0) {
            setPadding(DensityUtil.dip2px(AppApplication.getInstance(),10),DensityUtil.dip2px(AppApplication.getInstance(),10),
                    DensityUtil.dip2px(AppApplication.getInstance(),10),DensityUtil.dip2px(AppApplication.getInstance(),10));
        }
    }

    @Override
    public void setCompoundDrawables(@Nullable Drawable left, @Nullable Drawable top, @Nullable Drawable right, @Nullable Drawable bottom) {
        super.setCompoundDrawables(left, top, right, bottom);
        Drawable drawable = getCompoundDrawables()[1];
        if (drawable != null) {
            IntrinsicWidth = drawable.getIntrinsicWidth();
        }
        /**
         * 给RadioButton增加一定的padding
         * */
//        if (getPaddingBottom() == 0) {
//            setPadding(DensityUtil.dip2px(AppApplication.getInstance(),10),DensityUtil.dip2px(AppApplication.getInstance(),10),DensityUtil.dip2px(AppApplication.getInstance(),10),
//                    DensityUtil.dip2px(AppApplication.getInstance(),10));
//        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(width / 2, height / 2);
        mPaint = new Paint();
        mPaint.setStrokeWidth(2);
        mPaint.setAntiAlias(true);
        mPaint.setColor(getResources().getColor(R.color.red));
        mPaint.setTextSize(16f);
        pivotX = width/2;
        pivotY = 0;
        if (isShowDot) {
            canvas.drawCircle(pivotX, -pivotY, circleDotRadius, mPaint);
        } else if (isShowNumberDot && !TextUtils.isEmpty(numberText)) {
            float textWidth = mPaint.measureText(numberText);
            Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
            float textHeight = Math.abs((fontMetrics.top + fontMetrics.bottom));
            /**
             * 数字左右增加一定的边距
             * */
            pivotX = width/2 - textWidth - DensityUtil.dip2px(AppApplication.getInstance(),10);
            RectF rectF = new RectF(pivotX-DensityUtil.dip2px(AppApplication.getInstance(),4), -pivotY-textHeight/2-DensityUtil.dip2px(AppApplication.getInstance(),2),
                    pivotX + textWidth+DensityUtil.dip2px(AppApplication.getInstance(),4), -pivotY+textHeight);
            canvas.drawRoundRect(rectF, DensityUtil.dip2px(AppApplication.getInstance(),6),DensityUtil.dip2px(AppApplication.getInstance(),6), mPaint);
            mPaint.setColor(getResources().getColor(R.color.white));
            canvas.drawText(numberText, pivotX, -pivotY + textHeight / 2, mPaint);
        }
    }
    /**
     * `
     * 设置是否显示小圆点
     */
    public void setShowSmallDot(boolean isShowDot) {
        this.isShowDot = isShowDot;
        invalidate();
    }

    /**
     * 设置是否显示数字
     */
    public void setNumberDot(boolean isShowNumberDot, @NonNull String text) {
        this.isShowNumberDot = isShowNumberDot;
        this.numberText = text;
        if (isShowNumberDot) {
            isShowDot = false;
        }
        invalidate();
    }
}
