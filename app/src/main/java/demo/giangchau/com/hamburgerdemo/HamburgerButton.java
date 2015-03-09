package demo.giangchau.com.hamburgerdemo;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.shapes.RectShape;
import android.util.AttributeSet;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ChauGiang on 3/4/2015.
 */
public class HamburgerButton extends ImageButton {
    public enum HamburgerType {
        BackButton,
        CloseButton,
        CloseBackButton
    }

    public enum HamburgerState {
        Hamburger,
        Back,
        Close
    }

    public static final int MIN_SIZE = 100;
    public static final int LINE_WIDTH = 50;
    public static final int LINE_HEIGHT = 8;
    public static final int LINE_SPACE = 10;

    private HamburgerType mType; // type of humburger button
    private List<HamburgerState> mSupportStates = new ArrayList<>(); // list support state
    private int mCurrentState = 0; // current state of button
    private int mColor = 0;
    private int mLineWidth = LINE_WIDTH;
    private int mLineHeight = LINE_HEIGHT;
    private int mLineSpace = LINE_SPACE;
    private Point mCenterPoint;

    private ShapeTransformDrawable mLineTop = null;
    private ShapeTransformDrawable mLineCenter = null;
    private ShapeTransformDrawable mLineBottom = null;

    public HamburgerButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.HamburgerButton);
        try {
            mColor = a.getColor(R.styleable.HamburgerButton_color_line, 0);
            int type = a.getInt(R.styleable.HamburgerButton_button_type, 0);
            switch (type)
            {
                case 0:
                    mType = HamburgerType.BackButton;
                    mSupportStates.add(HamburgerState.Hamburger);
                    mSupportStates.add(HamburgerState.Back);
                    break;
                case 1:
                    mType = HamburgerType.CloseButton;
                    mSupportStates.add(HamburgerState.Hamburger);
                    mSupportStates.add(HamburgerState.Close);
                    break;
                case 2:
                    mType = HamburgerType.CloseBackButton;
                    mSupportStates.add(HamburgerState.Hamburger);
                    mSupportStates.add(HamburgerState.Close);
                    mSupportStates.add(HamburgerState.Back);
            }
        } finally {
            a.recycle();
        }
        mCurrentState = 0;

        // hover background
        setBackgroundResource(R.drawable.hamburger_bg_btn);
        // 3 rectangle for hamburger
        RectShape shape = new RectShape();
        shape.resize(mLineWidth, mLineHeight);
        mLineTop = new ShapeTransformDrawable(shape, this);
        mLineTop.getPaint().setColor(mColor);
        mLineCenter = new ShapeTransformDrawable(shape, this);
        mLineCenter.getPaint().setColor(mColor);
        mLineBottom = new ShapeTransformDrawable(shape, this);
        mLineBottom.getPaint().setColor(mColor);
    }

    @Override
    public boolean performClick() {
        switchState();
        return super.performClick();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width = 0, height = 0;
        if (widthMode == MeasureSpec.EXACTLY)
            width = widthSize;
        else if (widthMode == MeasureSpec.AT_MOST)
            width = Math.min(MIN_SIZE, widthSize);
        else
            width = widthSize;

        if (heightMode == MeasureSpec.EXACTLY)
            height = heightSize;
        else if (heightMode == MeasureSpec.AT_MOST)
            height = Math.min(MIN_SIZE, heightSize);
        else
            height = heightSize;

        setMeasuredDimension(width, height);
        // set center point base width height and width
        mCenterPoint = new Point(width / 2, height/2);
        int x = -mLineWidth / 2;
        int y = -mLineHeight / 2;

        mLineCenter.setPosition(x, y);
        mLineTop.setPosition(x, y - mLineSpace - mLineHeight);
        mLineBottom.setPosition(x, y + mLineSpace + mLineHeight);

        swichToState(mSupportStates.get(0), false);
//        mLineCenter.setRotatePivot(mLineWidth / 2, 0);
//        mLineTop.setRotatePivot(-x, -(y - mLineSpace - mLineHeight));
//        mLineTop.setScalePivot(mLineWidth / 2, 0);
//        mLineBottom.setRotatePivot(-x, -(y + mLineSpace + mLineHeight));
//        mLineBottom.setScalePivot(mLineWidth / 2, 0);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        final int count = canvas.save();
        canvas.translate(mCenterPoint.x, mCenterPoint.y);
        if (mLineCenter != null)
            mLineCenter.draw(canvas);
        if (mLineTop != null)
            mLineTop.draw(canvas);
        if (mLineBottom != null)
            mLineBottom.draw(canvas);
        canvas.restoreToCount(count);
    }

    void switchState()
    {
        mCurrentState++;
        if (mCurrentState >= mSupportStates.size())
            mCurrentState = 0;

        // animation
        swichToState(mSupportStates.get(mCurrentState), true);
    }

    private void swichToState(HamburgerState state, boolean animation)
    {
        if (animation)
        {
            mLineTop.transitionTo(getTransformStateAnimation(state, 0), 400);
            mLineCenter.transitionTo(getTransformStateAnimation(state, 1), 400);
            mLineBottom.transitionTo(getTransformStateAnimation(state, 2), 400);
        }
        else
        {
            mLineTop.setState(getTransformStateAnimation(state, 0));
            mLineCenter.setState(getTransformStateAnimation(state, 1));
            mLineBottom.setState(getTransformStateAnimation(state, 2));
        }
    }


    public TransformState getTransformStateAnimation(HamburgerState state, int line)
    {
        final float scaleX = 0.84f;
        int x = -mLineWidth / 2;
        int y = -mLineHeight / 2;
        switch (state)
        {
            case Back:
            {
                switch (line)
                {
                    case 0:
                        return new TransformState(x, y - mLineSpace - mLineHeight, scaleX, 1, mLineWidth / 2, 0,
                                225, -x, -(y - mLineSpace - mLineHeight)); // just scaleX and rotate
                    case 1:
                        return new TransformState(x, y, 1, 1, 0, 0, 180, mLineWidth / 2, mLineHeight / 2); // just rotate
                    case 2:
                        return new TransformState(x, y + mLineSpace + mLineHeight, scaleX, 1, mLineWidth / 2, 0,
                                135, -x, -(y + mLineSpace + mLineHeight));
                }
            }
            case Hamburger: {
                switch (line) {
                    case 0:
                        return new TransformState(x, y - mLineSpace - mLineHeight, 1, 1, mLineWidth / 2, 0,
                                360, -mLineTop.getState().PosX, -mLineTop.getState().PosY);
                    case 1:
                        return new TransformState(x, y, 1, 1, 0, 0, 360, mLineWidth / 2, mLineHeight / 2);
                    case 2:
                        return new TransformState(x, y + mLineSpace + mLineHeight, 1, 1, mLineWidth / 2, 0,
                                360, -mLineBottom.getState().PosX, -mLineBottom.getState().PosY);
                }
            }
            case Close:
            {
                switch (line)
                {
                    case 0:
                        return new TransformState(mLineTop.getState().PosX + 2,  mLineTop.getState().PosY + 25, 1, 1, 0, 0,
                                135, mLineWidth / 2, 0);
                    case 1:
                        return new TransformState(x, y, 0, 1, mLineWidth / 2, 0, 180, mLineWidth / 2, 0);
                    case 2:
                        return new TransformState(mLineBottom.getState().PosX + 2, mLineBottom.getState().PosY - 17, 1, 1, 0, 0,
                                45, mLineWidth / 2, 0);
                }
            }
        }
        return null;
    }
}
