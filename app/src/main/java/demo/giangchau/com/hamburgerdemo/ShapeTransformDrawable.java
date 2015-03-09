package demo.giangchau.com.hamburgerdemo;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.shapes.Shape;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;

/**
 * Created by ChauGiang on 3/4/2015.
 */
public class ShapeTransformDrawable {

    private TransformState mState = null;
    private TransformState mBeginState = null;
    private TransformState mTagetState = null;
    private float mOffset = 0; /* between 0 to 1 */
    private long mTimeStart = 0;
    private long mDuration = 0;
    private Interpolator mInterpolator = null;

    private Shape mShape = null;
    private Paint mPaint = null;
    private View mView = null;

    private Runnable mAnimator = new Runnable() {
        @Override
        public void run() {
            if (mTagetState == null || mBeginState == null)
                return;
            long now = AnimationUtils.currentAnimationTimeMillis();
            long offsetTime = now - mTimeStart;
            mOffset = (float)offsetTime / mDuration;
            if (mOffset >= 1)
                mOffset = 1;

            updateTransform(mInterpolator.getInterpolation(mOffset));
            mView.invalidate();

            if (mOffset < 1)
                mView.postDelayed(this, 20);
            else {
                mTagetState = null;
                mBeginState = null;
                mOffset = 0;
            }
        }

        private void updateTransform(float interpolator)
        {
            mState.PosX = mBeginState.PosX + (int)((mTagetState.PosX - mBeginState.PosX) * interpolator);
            mState.PosY = mBeginState.PosY + (int)((mTagetState.PosY - mBeginState.PosY) * interpolator);
            mState.ScaleX = mBeginState.ScaleX + (mTagetState.ScaleX - mBeginState.ScaleX) * interpolator;
            mState.ScaleY = mBeginState.ScaleY + (mTagetState.ScaleY - mBeginState.ScaleY) * interpolator;
            mState.ScalePivotX = mBeginState.ScalePivotX + (int)((mTagetState.ScalePivotX - mBeginState.ScalePivotX) * interpolator);
            mState.ScalePivotY = mBeginState.ScalePivotY + (int)((mTagetState.ScalePivotY - mBeginState.ScalePivotY) * interpolator);
            mState.RotateDegrees =  mBeginState.RotateDegrees + (mTagetState.RotateDegrees - mBeginState.RotateDegrees) * interpolator;
            mState.RotatePivotX =  mBeginState.RotatePivotX + (int)((mTagetState.RotatePivotX - mBeginState.RotatePivotX) * interpolator);
            mState.RotatePivotY =  mBeginState.RotatePivotY + (int)((mTagetState.RotatePivotY - mBeginState.RotatePivotY) * interpolator);

            if (mState.RotateDegrees >= 360)
                mState.RotateDegrees = mState.RotateDegrees % 360;
        }
    };

    public ShapeTransformDrawable(Shape s, @NonNull View view) {
        if (s == null)
            throw new NullPointerException();
        if (view == null)
            throw new NullPointerException();
        mView = view;
        mShape = s;

        mState = new TransformState();
        mInterpolator = new AccelerateDecelerateInterpolator();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setAntiAlias(true);

    }

    public void draw(Canvas canvas) {
        if (mShape != null) {
            final int count = canvas.save();
            canvas.translate(mState.PosX, mState.PosY - mShape.getHeight() / 2);
            canvas.rotate(mState.RotateDegrees, mState.RotatePivotX, mState.RotatePivotY);
            canvas.scale(mState.ScaleX, mState.ScaleY, mState.ScalePivotX, mState.ScalePivotY);
            mShape.draw(canvas, mPaint);
            canvas.restoreToCount(count);
        } else {
            canvas.drawRect(new RectF(mState.PosX, mState.PosY, 10, 10), mPaint);
        }
    }

    public Paint getPaint()
    {
        return mPaint;
    }

    public TransformState getState()
    {
        return mState;
    }

    public void setState(TransformState state)
    {
        mState = (TransformState)state.clone();
        if (mState.RotateDegrees >= 360)
            mState.RotateDegrees = mState.RotateDegrees % 360;
        mView.invalidate();
    }

    public void setPosition(int left, int top)
    {
        mState.PosX = left;
        mState.PosY = top;
        mView.invalidate();
    }

    public void setRotateDegrees(float r)
    {
        if (mState.RotateDegrees != r)
            mState.RotateDegrees = r;
        mView.invalidate();
    }

    public void setRotatePivot(int left,int top)
    {
        mState.RotatePivotX = left;
        mState.RotatePivotY = top;
        mView.invalidate();
    }

    public void setScaleX(float scale)
    {
        mState.ScaleX = scale;
        mView.invalidate();
    }

    public void setScaleY(float scale)
    {
        mState.ScaleY = scale;
        mView.invalidate();
    }

    public void setScalePivot(int left, int top)
    {
        mState.ScalePivotX = left;
        mState.ScalePivotY = top;
        mView.invalidate();
    }

    public void setScalePivotX(int left)
    {
        mState.ScalePivotX = left;
        mView.invalidate();
    }

    public void setScalePivotY(int top)
    {
        mState.ScalePivotY = top;
        mView.invalidate();
    }

    public void transitionTo(TransformState newState, long duration)
    {
        if (newState == null)
            throw new NullPointerException();
        mTagetState = (TransformState)newState.clone();
        mDuration = duration;
        restartAnimation();
    }

    private void restartAnimation()
    {
        mBeginState = (TransformState)mState.clone();
        mTimeStart = AnimationUtils.currentAnimationTimeMillis();
        mOffset = 0;
        mView.post(mAnimator);
    }
}
