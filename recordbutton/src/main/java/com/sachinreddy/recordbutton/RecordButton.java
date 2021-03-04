//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.sachinreddy.recordbutton;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.graphics.drawable.Animatable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.annotation.Nullable;

import com.sachinreddy.recordbutton.R.styleable;

public class RecordButton extends View implements Animatable {
    private Paint buttonPaint;
    private Paint progressEmptyPaint;
    private Paint progressPaint;
    private RectF rectF;
    private Bitmap bitmap;
    private float buttonRadius;
    private int progressStroke;
    private float buttonGap;
    private int buttonColor;
    private int progressEmptyColor;
    private int progressColor;
    private int recordIcon;
    private boolean isRecording = false;
    private float currentMiliSecond = 0f;
    private int maxMilisecond;
    private int startAngle = 270;
    private float sweepAngle;
    OnRecordListener recordListener;

    public RecordButton(Context context) {
        super(context);
        this.init(context, (AttributeSet) null);
    }

    public RecordButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.init(context, attrs);
    }

    public RecordButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init(context, attrs);
    }

    @SuppressLint({"NewApi"})
    public RecordButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, styleable.RecordButton);
        this.buttonRadius = a.getDimension(styleable.RecordButton_buttonRadius, this.getResources().getDisplayMetrics().scaledDensity * 40.0F);
        this.progressStroke = a.getInt(styleable.RecordButton_progressStroke, 10);
        this.buttonGap = a.getDimension(styleable.RecordButton_buttonGap, this.getResources().getDisplayMetrics().scaledDensity * 8.0F);
        this.buttonColor = a.getColor(styleable.RecordButton_buttonColor, -65536);
        this.progressEmptyColor = a.getColor(styleable.RecordButton_progressEmptyColor, -3355444);
        this.progressColor = a.getColor(styleable.RecordButton_progressColor, -16776961);
        this.recordIcon = a.getResourceId(styleable.RecordButton_recordIcon, -1);
        this.maxMilisecond = a.getInt(styleable.RecordButton_maxMilisecond, 5000);
        a.recycle();
        this.buttonPaint = new Paint(1);
        this.buttonPaint.setColor(this.buttonColor);
        this.buttonPaint.setStyle(Style.FILL);
        this.progressEmptyPaint = new Paint(1);
        this.progressEmptyPaint.setColor(this.progressEmptyColor);
        this.progressEmptyPaint.setStyle(Style.STROKE);
        this.progressEmptyPaint.setStrokeWidth((float) this.progressStroke);
        this.progressPaint = new Paint(1);
        this.progressPaint.setColor(this.progressColor);
        this.progressPaint.setStyle(Style.STROKE);
        this.progressPaint.setStrokeWidth((float) this.progressStroke);
        this.progressPaint.setStrokeCap(Cap.ROUND);
        this.rectF = new RectF();
        this.bitmap = BitmapFactory.decodeResource(context.getResources(), this.recordIcon);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int cx = this.getWidth() / 2;
        int cy = this.getHeight() / 2;
        canvas.drawCircle((float) cx, (float) cy, this.buttonRadius, this.buttonPaint);
        canvas.drawCircle((float) cx, (float) cy, this.buttonRadius + this.buttonGap, this.progressEmptyPaint);
        if (this.recordIcon != -1) {
            canvas.drawBitmap(this.bitmap, (float) (cx - this.bitmap.getHeight() / 2), (float) (cy - this.bitmap.getWidth() / 2), (Paint) null);
        }

        this.sweepAngle = 360 * this.currentMiliSecond / this.maxMilisecond;
        this.rectF.set((float) cx - this.buttonRadius - this.buttonGap, (float) cy - this.buttonRadius - this.buttonGap, (float) cx + this.buttonRadius + this.buttonGap, (float) cy + this.buttonRadius + this.buttonGap);
        canvas.drawArc(this.rectF, (float) this.startAngle, this.sweepAngle, false, this.progressPaint);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int size = (int) this.buttonRadius * 2 + (int) this.buttonGap * 2 + this.progressStroke + 30;
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width;
        if (widthMode == 1073741824) {
            width = widthSize;
        } else if (widthMode == -2147483648) {
            width = Math.min(size, widthSize);
        } else {
            width = size;
        }

        int height;
        if (heightMode == 1073741824) {
            height = heightSize;
        } else if (heightMode == -2147483648) {
            height = Math.min(size, heightSize);
        } else {
            height = size;
        }

        this.setMeasuredDimension(width, height);
    }

    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case 0:
                this.start();
                this.progressAnimate().start();
                return true;
            case 1:
                this.stop();
                return true;
            default:
                return false;
        }
    }

    public void start() {
        this.isRecording = true;
        this.scaleAnimation(1.1F, 1.1F);
    }

    public void stop() {
        this.isRecording = false;
        this.currentMiliSecond = 0;
        this.scaleAnimation(1.0F, 1.0F);
    }

    public boolean isRunning() {
        return this.isRecording;
    }

    private void scaleAnimation(float scaleX, float scaleY) {
        this.animate().scaleX(scaleX).scaleY(scaleY).start();
    }

    private ObjectAnimator progressAnimate() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(this, "progress", new float[]{(float) this.currentMiliSecond, (float) this.maxMilisecond});
        animator.addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = ((Float) ((Float) animation.getAnimatedValue())).intValue();
                if (RecordButton.this.isRecording) {
//                    RecordButton.this.setCurrentMiliSecond(value);
                    if (RecordButton.this.recordListener != null) {
                        RecordButton.this.recordListener.onRecord();
                    }
                } else {
                    animation.cancel();
                    RecordButton.this.isRecording = false;
                    if (RecordButton.this.recordListener != null) {
                        RecordButton.this.recordListener.onRecordCancel();
                    }
                }

                if (value == RecordButton.this.maxMilisecond) {
                    if (RecordButton.this.recordListener != null) {
                        RecordButton.this.recordListener.onRecordFinish();
                    }

                    RecordButton.this.stop();
                }

            }
        });
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration((long) this.maxMilisecond);
        return animator;
    }

    public void setCurrentMiliSecond(float currentMiliSecond) {
        this.currentMiliSecond = currentMiliSecond;
        this.postInvalidate();
    }

    public float getCurrentMiliSecond() {
        return this.currentMiliSecond;
    }

    public Paint getButtonPaint() {
        return this.buttonPaint;
    }

    public void setButtonPaint(Paint buttonPaint) {
        this.buttonPaint = buttonPaint;
    }

    public Paint getProgressEmptyPaint() {
        return this.progressEmptyPaint;
    }

    public void setProgressEmptyPaint(Paint progressEmptyPaint) {
        this.progressEmptyPaint = progressEmptyPaint;
    }

    public Paint getProgressPaint() {
        return this.progressPaint;
    }

    public void setProgressPaint(Paint progressPaint) {
        this.progressPaint = progressPaint;
    }

    public Bitmap getBitmap() {
        return this.bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public float getButtonRadius() {
        return this.buttonRadius;
    }

    public void setButtonRadius(int buttonRadius) {
        this.buttonRadius = (float) buttonRadius;
    }

    public int getProgressStroke() {
        return this.progressStroke;
    }

    public void setProgressStroke(int progressStroke) {
        this.progressStroke = progressStroke;
    }

    public float getButtonGap() {
        return this.buttonGap;
    }

    public void setButtonGap(int buttonGap) {
        this.buttonGap = (float) buttonGap;
    }

    public int getButtonColor() {
        return this.buttonColor;
    }

    public void setButtonColor(int buttonColor) {
        this.buttonColor = buttonColor;
    }

    public int getProgressEmptyColor() {
        return this.progressEmptyColor;
    }

    public void setProgressEmptyColor(int progressEmptyColor) {
        this.progressEmptyColor = progressEmptyColor;
    }

    public int getProgressColor() {
        return this.progressColor;
    }

    public void setProgressColor(int progressColor) {
        this.progressColor = progressColor;
    }

    public int getMaxMilisecond() {
        return this.maxMilisecond;
    }

    public void setMaxMilisecond(int maxMilisecond) {
        this.maxMilisecond = maxMilisecond;
    }

    public int getRecordIcon() {
        return this.recordIcon;
    }

    public void setRecordIcon(int recordIcon) {
        this.recordIcon = recordIcon;
    }

    public void setRecordListener(OnRecordListener recordListener) {
        this.recordListener = recordListener;
    }
}
