package demo.giangchau.com.hamburgerdemo;

/**
 * Created by ChauGiang on 3/6/2015.
 */
public class TransformState implements Cloneable
{
    public int PosX;
    public int PosY;
    public float ScaleX;
    public float ScaleY;
    public int ScalePivotX;
    public int ScalePivotY;
    public float RotateDegrees;
    public int RotatePivotX;
    public int RotatePivotY;


    public TransformState()
    {
        init(0, 0, 1, 1, 0, 0, 0, 0, 0);
    }

    @Override
    protected Object clone() {
        return new TransformState(this);
    }

    public TransformState(TransformState state)
    {
        this(state.PosX, state.PosY, state.ScaleX, state.ScaleY, state.ScalePivotX, state.ScalePivotY,
                state.RotateDegrees, state.RotatePivotX, state.RotatePivotY);
    }

    public TransformState(int posX, int posY, float scaleX, float scaleY, int scalePiX, int scalePiY,
                          float rotateDeg, int rotatePiX, int rotatePiY)
    {
        init(posX, posY, scaleX, scaleY, scalePiX, scalePiY, rotateDeg,rotatePiX, rotatePiY);
    }

    private void init(int posX, int posY, float scaleX, float scaleY, int scalePiX, int scalePiY,
                      float rotateDeg, int rotatePiX, int rotatePiY)
    {
        PosX = posX;
        PosY = posY;
        ScaleX = scaleX;
        ScaleY = scaleY;
        ScalePivotX = scalePiX;
        ScalePivotY = scalePiY;
        RotateDegrees = rotateDeg;
        RotatePivotX = rotatePiX;
        RotatePivotY = rotatePiY;
    }
}
