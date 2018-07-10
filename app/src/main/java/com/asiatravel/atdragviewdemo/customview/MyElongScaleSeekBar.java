package com.asiatravel.atdragviewdemo.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;


import com.asiatravel.atdragviewdemo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author feilong
 */
public class MyElongScaleSeekBar extends View {
    private static final String TAG = "MyElongScaleSeekBar";
    private int scaleProgressNormalColor;
    private int scaleProgressSectionColor;
    private int scaleLeftBallBgColor;
    private int scaleLeftBallStrokeColor;
    private int scaleLeftBallStrokeWith;
    private int scaleRightBallBgColor;
    private int scaleRightBallStrokeColor;
    private int scaleRightBallStrokeWith;
    private int scaleBallRadio;
    private int scaleSeekHeight;
    private int scaleLeftTextColor;
    private int scaleLeftTextSize;
    private int scaleRightTextColor;
    private int scaleRightTextSize;
    private int scaleTextMarginBall;
    private int scaleBallShadowColor;
    private int scaleBallShadowRadio;
    private int scaleBallStickHeight;
    private int scaleBallStickWidth;
    private int scaleBallStickColor;
    private int scaleBallStickMargin;

    private Paint scaleProgressNormalPaint;
    private Paint scaleProgressSectionPaint;
    private Paint scaleLeftBallBgPaint;
    private Paint scaleLeftBallStrokePaint;
    private Paint scaleRightBallBgPaint;
    private Paint scaleRightBallStrokePaint;
    private Paint scaleLeftTextPaint;
    private Paint scaleRightTextPaint;
    private Paint scaleStickPaint;

    private RectF scaleSeekNormalRectF;
    private RectF scaleSeekSectionRectF;
    private SeekPoint leftBallPoint;
    private SeekPoint rightBallPoint;
    private float radioWithStroke;

    private RectF stickLeftRectF1;
    private RectF stickLeftRectF2;
    private RectF stickLeftRectF3;

    private RectF stickRightRectF1;
    private RectF stickRightRectF2;
    private RectF stickRightRectF3;

    private Path scaleLeftBallPath;
    private Path scaleRightBallPath;

    private Region scaleLeftBallRegion;
    private Region scaleRightBallRegion;
    /**
     * 拼接在数据之前要展示的符号
     */
    private String symbolFront;
    /**
     * 左边的文案描述
     */
    private String leftDesc;
    /**
     * 右边的文案描述
     */
    private String rightDesc;

    /**
     * 进度条的最大值
     */
    private int maxValue;
    /**
     * 进度条步长(不设置,默认是50一步)
     */
    private int scaleProgressUnit;
    /**
     * 单位步长对应的X轴距离
     */
    private float xCoordinateUnit;
    /**
     * 当前左边球的数据实体
     */
    private UnitValueEntity currentLeft;
    /**
     * 当前右边球的数据实体
     */
    private UnitValueEntity currentRight;
    /**
     * 尺寸数据源
     */
    private List<UnitValueEntity> valueEntities;

    private OnSeekBarDragListener seekBarDragListener;


    public MyElongScaleSeekBar(Context context) {
        this(context, null);
    }

    public MyElongScaleSeekBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyElongScaleSeekBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
        initPaints();
    }


    public void setMaxValue(int maxValue, @Nullable OnSeekBarDragListener seekBarDragListener, int scaleProgressUnit) {
        this.seekBarDragListener = seekBarDragListener;
        this.maxValue = maxValue;
        this.scaleProgressUnit = scaleProgressUnit;
    }

    /**
     * 初始化画笔
     */
    private void initPaints() {
        scaleProgressNormalPaint = creatPaint(scaleProgressNormalColor, 0, Paint.Style.FILL, 0);
        scaleProgressSectionPaint = creatPaint(scaleProgressSectionColor, 0, Paint.Style.FILL, 0);
        scaleLeftBallBgPaint = creatPaint(scaleLeftBallBgColor, 0, Paint.Style.FILL, 0);
        scaleLeftBallStrokePaint = creatPaint(scaleLeftBallStrokeColor, 0, Paint.Style.FILL, 0);
        scaleRightBallBgPaint = creatPaint(scaleRightBallBgColor, 0, Paint.Style.FILL, 0);
        scaleRightBallStrokePaint = creatPaint(scaleRightBallStrokeColor, 0, Paint.Style.FILL, 0);
        scaleLeftTextPaint = creatPaint(scaleLeftTextColor, scaleLeftTextSize, Paint.Style.FILL, 0);
        scaleRightTextPaint = creatPaint(scaleRightTextColor, scaleRightTextSize, Paint.Style.FILL, 0);
        scaleStickPaint = creatPaint(scaleBallStickColor, 0, Paint.Style.FILL, 0);
    }

    /**
     * 初始化自定义属性
     *
     * @param context 上下文
     * @param attrs   自定义属性
     */
    private void initAttrs(Context context, @Nullable AttributeSet attrs) {
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.MyElongScaleSeekBar, 0, R.style.default_scale_seekbar_style);
        int indexCount = typedArray.getIndexCount();
        for (int i = 0; i < indexCount; i++) {
            int attr = typedArray.getIndex(i);
            switch (attr) {
                case R.styleable.MyElongScaleSeekBar_scale_progress_normal_color:
                    scaleProgressNormalColor = typedArray.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.MyElongScaleSeekBar_scale_progress_section_color:
                    scaleProgressSectionColor = typedArray.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.MyElongScaleSeekBar_scale_left_ball_bg_color:
                    scaleLeftBallBgColor = typedArray.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.MyElongScaleSeekBar_scale_left_ball_stroke_color:
                    scaleLeftBallStrokeColor = typedArray.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.MyElongScaleSeekBar_scale_left_ball_stroke_with:
                    scaleLeftBallStrokeWith = typedArray.getDimensionPixelOffset(attr, 0);
                    break;
                case R.styleable.MyElongScaleSeekBar_scale_right_ball_bg_color:
                    scaleRightBallBgColor = typedArray.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.MyElongScaleSeekBar_scale_right_ball_stroke_color:
                    scaleRightBallStrokeColor = typedArray.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.MyElongScaleSeekBar_scale_right_ball_stroke_with:
                    scaleRightBallStrokeWith = typedArray.getDimensionPixelOffset(attr, 0);
                    break;
                case R.styleable.MyElongScaleSeekBar_scale_ball_radio:
                    scaleBallRadio = typedArray.getDimensionPixelOffset(attr, 0);
                    break;
                case R.styleable.MyElongScaleSeekBar_scale_seek_height:
                    scaleSeekHeight = typedArray.getDimensionPixelOffset(attr, 0);
                    break;
                case R.styleable.MyElongScaleSeekBar_scale_left_text_color:
                    scaleLeftTextColor = typedArray.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.MyElongScaleSeekBar_scale_left_text_size:
                    scaleLeftTextSize = typedArray.getDimensionPixelOffset(attr, 0);
                    break;
                case R.styleable.MyElongScaleSeekBar_scale_right_text_color:
                    scaleRightTextColor = typedArray.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.MyElongScaleSeekBar_scale_right_text_size:
                    scaleRightTextSize = typedArray.getDimensionPixelOffset(attr, 0);
                    break;
                case R.styleable.MyElongScaleSeekBar_scale_text_margin_ball:
                    scaleTextMarginBall = typedArray.getDimensionPixelOffset(attr, 0);
                    break;
                case R.styleable.MyElongScaleSeekBar_scale_progress_unit:
                    scaleProgressUnit = typedArray.getInteger(attr, 0);
                    break;
                case R.styleable.MyElongScaleSeekBar_scale_ball_shadow_color:
                    scaleBallShadowColor = typedArray.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.MyElongScaleSeekBar_scale_ball_shadow_radio:
                    scaleBallShadowRadio = typedArray.getDimensionPixelOffset(attr, 0);
                    break;
                case R.styleable.MyElongScaleSeekBar_scale_ball_stick_color:
                    scaleBallStickColor = typedArray.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.MyElongScaleSeekBar_scale_ball_stick_height:
                    scaleBallStickHeight = typedArray.getDimensionPixelOffset(attr, 0);
                    break;
                case R.styleable.MyElongScaleSeekBar_scale_ball_stick_width:
                    scaleBallStickWidth = typedArray.getDimensionPixelOffset(attr, 0);
                    break;
                case R.styleable.MyElongScaleSeekBar_scale_ball_stick_margin:
                    scaleBallStickMargin = typedArray.getDimensionPixelOffset(attr, 0);
                    break;
                case R.styleable.MyElongScaleSeekBar_scale_symbol_front:
                    symbolFront = typedArray.getString(attr);
                    break;
                default:
            }
        }
        typedArray.recycle();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        //球的半径包含描边
        radioWithStroke = scaleBallRadio + scaleLeftBallStrokeWith * 0.5F;
        xCoordinateUnit = radioWithStroke * 2;
        valueEntities = calculateAllXCoordinate(scaleProgressUnit, w - radioWithStroke * 2, maxValue);

        if (valueEntities != null && valueEntities.size() > 0) {
            currentLeft = valueEntities.get(0);
            currentRight = valueEntities.get(valueEntities.size() - 1);

            leftDesc = creatCurrentDataDesc(currentLeft);
            rightDesc = creatCurrentDataDesc(currentRight);

            if (this.seekBarDragListener != null) {
                this.seekBarDragListener.seekMoveValue(currentLeft.value, currentRight.value);
            }
        }

        if (currentLeft != null && currentRight != null) {
            // 背景进度条的区域
            scaleSeekNormalRectF = new RectF();
            scaleSeekNormalRectF.left = 0;
            float top = h - (scaleBallRadio + scaleLeftBallStrokeWith * 0.5F) - scaleSeekHeight * 0.5F - scaleBallShadowRadio;
            scaleSeekNormalRectF.top = top;
            scaleSeekNormalRectF.bottom = top + scaleSeekHeight;
            scaleSeekNormalRectF.right = w;


            // 左边球的圆心坐标
            leftBallPoint = new SeekPoint();
            leftBallPoint.x = currentLeft.xCoordinat + radioWithStroke;
            leftBallPoint.y = h - radioWithStroke - scaleBallShadowRadio;

            // 左边球中间的猴三棍
            calculateLeftSticks();

            // 右边球的坐标
            rightBallPoint = new SeekPoint();
            rightBallPoint.x = currentRight.xCoordinat + radioWithStroke;
            rightBallPoint.y = h - radioWithStroke - scaleBallShadowRadio;

            // 右边球中间的猴三棍
            calculateRightSticks();

            // 选中背景条的间距部分
            scaleSeekSectionRectF = new RectF();
            scaleSeekSectionRectF.left = leftBallPoint.x - radioWithStroke;
            scaleSeekSectionRectF.right = rightBallPoint.x - radioWithStroke;
            scaleSeekSectionRectF.top = scaleSeekNormalRectF.top;
            scaleSeekSectionRectF.bottom = scaleSeekNormalRectF.bottom;

            // 圆球的路径和区域
            scaleLeftBallPath = new Path();
            scaleLeftBallPath.addCircle(leftBallPoint.x, leftBallPoint.y, radioWithStroke, Path.Direction.CW);

            scaleRightBallPath = new Path();
            scaleRightBallPath.addCircle(rightBallPoint.x, rightBallPoint.y, radioWithStroke, Path.Direction.CW);

            scaleLeftBallRegion = updateRegionByPath(scaleLeftBallPath);
            scaleRightBallRegion = updateRegionByPath(scaleRightBallPath);
        }
    }

    /**
     * 右边球中间的猴三棍
     */
    private void calculateRightSticks() {
        stickRightRectF2 = new RectF();
        stickRightRectF2.left = rightBallPoint.x - scaleBallStickWidth * 0.5F;
        stickRightRectF2.right = rightBallPoint.x + scaleBallStickWidth * 0.5F;
        stickRightRectF2.top = rightBallPoint.y - scaleBallStickHeight * 0.5F;
        stickRightRectF2.bottom = rightBallPoint.y + scaleBallStickHeight * 0.5F;

        stickRightRectF1 = new RectF();
        stickRightRectF1.left = stickRightRectF2.left - scaleBallStickMargin - scaleBallStickWidth;
        stickRightRectF1.right = stickRightRectF2.left - scaleBallStickMargin;
        stickRightRectF1.top = stickRightRectF2.top;
        stickRightRectF1.bottom = stickRightRectF2.bottom;

        stickRightRectF3 = new RectF();
        stickRightRectF3.left = stickRightRectF2.right + scaleBallStickMargin;
        stickRightRectF3.right = stickRightRectF2.right + scaleBallStickMargin + scaleBallStickWidth;
        stickRightRectF3.top = stickRightRectF2.top;
        stickRightRectF3.bottom = stickRightRectF2.bottom;
    }

    /**
     * 绘制左边的猴三棍
     */
    private void calculateLeftSticks() {
        stickLeftRectF2 = new RectF();
        stickLeftRectF2.left = leftBallPoint.x - scaleBallStickWidth * 0.5F;
        stickLeftRectF2.right = leftBallPoint.x + scaleBallStickWidth * 0.5F;
        stickLeftRectF2.top = leftBallPoint.y - scaleBallStickHeight * 0.5F;
        stickLeftRectF2.bottom = leftBallPoint.y + scaleBallStickHeight * 0.5F;

        stickLeftRectF1 = new RectF();
        stickLeftRectF1.left = stickLeftRectF2.left - scaleBallStickMargin - scaleBallStickWidth;
        stickLeftRectF1.right = stickLeftRectF2.left - scaleBallStickMargin;
        stickLeftRectF1.top = stickLeftRectF2.top;
        stickLeftRectF1.bottom = stickLeftRectF2.bottom;

        stickLeftRectF3 = new RectF();
        stickLeftRectF3.left = stickLeftRectF2.right + scaleBallStickMargin;
        stickLeftRectF3.right = stickLeftRectF2.right + scaleBallStickMargin + scaleBallStickWidth;
        stickLeftRectF3.top = stickLeftRectF2.top;
        stickLeftRectF3.bottom = stickLeftRectF2.bottom;
    }

    /**
     * 变化的region
     *
     * @param path 路径
     * @return 变化的region
     */
    private Region updateRegionByPath(Path path) {
        Region region = new Region();
        if (path != null) {
            RectF tempRectF = new RectF();
            path.computeBounds(tempRectF, true);
            region.setPath(path, new Region((int) tempRectF.left, (int) tempRectF.top, (int) tempRectF.right, (int) tempRectF.bottom));
        }
        return region;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int measureHeight;
        if (heightMode == MeasureSpec.AT_MOST || heightMode == MeasureSpec.UNSPECIFIED) {
            measureHeight = Math.max(scaleLeftTextSize, scaleRightTextSize) + 2 + scaleTextMarginBall + scaleBallRadio * 2;
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(measureHeight, MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawScaleSeekNormal(canvas);
        drawScaleSeekSection(canvas);
        drawLeftBall(canvas);
        drawRightBall(canvas);
        drawLeftText(canvas);
        drawRightText(canvas);
    }

    /**
     * 绘制右边的文字
     *
     * @param canvas 画布
     */
    private void drawRightText(Canvas canvas) {
        SeekPoint measureTextSize = measureTextSize(scaleRightTextPaint, rightDesc);

        float textX;
        float textY;
        float incrementX;
        if (rightBallPoint.x - leftBallPoint.x <= xCoordinateUnit) {
            incrementX = radioWithStroke / 2;
        } else {
            incrementX = 0;
        }
        if (measureTextSize != null) {
            textX = rightBallPoint.x - measureTextSize.x / 2 + incrementX;
        } else {
            textX = rightBallPoint.x;
        }
        textY = rightBallPoint.y - radioWithStroke - scaleTextMarginBall;
        canvas.drawText(rightDesc, textX, textY, scaleRightTextPaint);
    }

    /**
     * 绘制左边的文字
     *
     * @param canvas 画布
     */
    private void drawLeftText(Canvas canvas) {
        SeekPoint measureTextSize = measureTextSize(scaleLeftTextPaint, leftDesc);
        float textX;
        float textY;
        float incrementX;

        if (rightBallPoint.x - leftBallPoint.x <= xCoordinateUnit) {
            incrementX = radioWithStroke / 2;
        } else {
            incrementX = 0;
        }
        if (measureTextSize != null) {
            textX = leftBallPoint.x - measureTextSize.x / 2 - incrementX;
        } else {
            textX = leftBallPoint.x;
        }
        textY = leftBallPoint.y - radioWithStroke - scaleTextMarginBall;
        canvas.drawText(leftDesc, textX, textY, scaleLeftTextPaint);
    }

    /**
     * 绘制右边的滑块球
     *
     * @param canvas 画布
     */
    private void drawRightBall(Canvas canvas) {
        scaleRightBallStrokePaint.setShadowLayer(scaleBallShadowRadio, 0, 2, scaleBallShadowColor);
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        canvas.drawCircle(rightBallPoint.x, rightBallPoint.y, radioWithStroke, scaleRightBallStrokePaint);
        canvas.drawCircle(rightBallPoint.x, rightBallPoint.y, scaleBallRadio, scaleRightBallBgPaint);
        // 绘制右边边球猴三棍
        canvas.drawRoundRect(stickRightRectF1, scaleBallStickWidth * 0.5F, scaleBallStickWidth * 0.5F, scaleStickPaint);
        canvas.drawRoundRect(stickRightRectF2, scaleBallStickWidth * 0.5F, scaleBallStickWidth * 0.5F, scaleStickPaint);
        canvas.drawRoundRect(stickRightRectF3, scaleBallStickWidth * 0.5F, scaleBallStickWidth * 0.5F, scaleStickPaint);
    }

    /**
     * 绘制左边的滑块球
     *
     * @param canvas 画布
     */
    private void drawLeftBall(Canvas canvas) {
        scaleLeftBallStrokePaint.setShadowLayer(scaleBallShadowRadio, 0, 2, scaleBallShadowColor);
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        canvas.drawCircle(leftBallPoint.x, leftBallPoint.y, radioWithStroke, scaleLeftBallStrokePaint);
        canvas.drawCircle(leftBallPoint.x, leftBallPoint.y, scaleBallRadio, scaleLeftBallBgPaint);
        // 绘制左边球猴三棍
        canvas.drawRoundRect(stickLeftRectF1, scaleBallStickWidth * 0.5F, scaleBallStickWidth * 0.5F, scaleStickPaint);
        canvas.drawRoundRect(stickLeftRectF2, scaleBallStickWidth * 0.5F, scaleBallStickWidth * 0.5F, scaleStickPaint);
        canvas.drawRoundRect(stickLeftRectF3, scaleBallStickWidth * 0.5F, scaleBallStickWidth * 0.5F, scaleStickPaint);
    }

    /**
     * 绘制选中的进度条背景
     *
     * @param canvas 画布
     */
    private void drawScaleSeekSection(Canvas canvas) {
        canvas.drawRect(scaleSeekSectionRectF, scaleProgressSectionPaint);
    }

    /**
     * 绘制进度条背景
     *
     * @param canvas 画布
     */
    private void drawScaleSeekNormal(Canvas canvas) {
        canvas.drawRect(scaleSeekNormalRectF, scaleProgressNormalPaint);
    }


    private boolean leftChange;
    private boolean rightChange;

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                boolean touchLeftBall = scaleLeftBallRegion.contains((int) event.getX(), (int) event.getY()) && !scaleRightBallRegion.contains((int) event.getX(), (int) event.getY());
                boolean touchRightBall = scaleRightBallRegion.contains((int) event.getX(), (int) event.getY()) && !scaleLeftBallRegion.contains((int) event.getX(), (int) event.getY());

                //更新左边球的位置
                if (touchLeftBall) {
                    leftChange = true;
                    leftBallPoint.x = event.getX();
                    calculateLeftSticks();
                }

                // 更新右边球的位置
                if (touchRightBall) {
                    rightChange = true;
                    rightBallPoint.x = event.getX();
                    calculateRightSticks();
                }

                // 更新中间选中条的区域
                scaleSeekSectionRectF.left = leftBallPoint.x + radioWithStroke;
                scaleSeekSectionRectF.right = rightBallPoint.x + radioWithStroke;

                break;
            case MotionEvent.ACTION_MOVE:

                // 左边球正在移动
                if (leftChange) {
                    // 限制左边界
                    leftBallPoint.x = event.getX();
                    if (event.getX() < radioWithStroke) {
                        leftBallPoint.x = radioWithStroke;
                    }
                    // 左边球右滑动的右边界
                    // 两球最近的距离为单位步长的x距离轴
                    if (rightBallPoint.x + radioWithStroke - event.getX() < xCoordinateUnit) {
                        leftBallPoint.x = rightBallPoint.x + radioWithStroke - xCoordinateUnit;
                    }

                    calculateLeftSticks();

                    // 查找移动的时候坐标对应的数据实体
                    int computation = (int) realTimeComputation(event.getX(), maxValue, getWidth()) < 0 ? 0 : (int) realTimeComputation(event.getX(), maxValue, getWidth());
                    String tempL = leftDesc;

                    if (computation % scaleProgressUnit == 0) {
                        leftDesc = symbolFront + computation;
                    } else {
                        leftDesc = tempL;
                    }
                }

                // 右边球正在移动
                if (rightChange) {
                    rightBallPoint.x = event.getX();
                    if (event.getX() > getWidth() - radioWithStroke) {
                        rightBallPoint.x = getWidth() - radioWithStroke;
                    }
                    // 右边球滑动的左边界(两球最近的距离为单位步长的x距离轴)
                    if (event.getX() - leftBallPoint.x - radioWithStroke < xCoordinateUnit) {
                        rightBallPoint.x = leftBallPoint.x - radioWithStroke + xCoordinateUnit;
                    }
                    calculateRightSticks();

                    // 查找移动的时候坐标对应的数据实体
                    int computation = (int) realTimeComputation(event.getX(), maxValue, getWidth()) > maxValue ? maxValue : (int) realTimeComputation(event.getX(), maxValue, getWidth());
                    String tempR = rightDesc;
                    if (computation % scaleProgressUnit == 0) {
                        rightDesc = symbolFront + computation;
                    } else {
                        rightDesc = tempR;
                    }
                }

                // 更新中间选中条的区域
                scaleSeekSectionRectF.left = leftBallPoint.x + radioWithStroke;
                scaleSeekSectionRectF.right = rightBallPoint.x + radioWithStroke;

                break;
            case MotionEvent.ACTION_UP:
                Log.e(TAG, "rightBallPoint.x=" + rightBallPoint.x + "--->leftBallPoint.x=" + leftBallPoint.x + "--想减=" + (rightBallPoint.x - leftBallPoint.x) + "xCoordinateUnit=" + xCoordinateUnit);

                // 根据拖拽的单位步长,计算出最后回弹的正确位置
                if (leftChange) {
                    // 限制左边界
                    leftBallPoint.x = event.getX();

                    List<UnitValueEntity> temp = new ArrayList<>();
                    for (int i = 0; i <= currentRight.position; i++) {
                        temp.add(valueEntities.get(i));
                    }
                    // 如果之前不是0,但是没找到,那么赋值给他之前的元素
                    UnitValueEntity tempPre = currentLeft;
                    currentLeft = binarySearchKey(temp, (int) event.getX());
                    if (currentLeft.position == 0) {
                        currentLeft = tempPre;
                    }
                    // 重合返回前一个
                    if (currentLeft.position == currentRight.position) {
                        currentLeft = tempPre;
                    }

                    // 左边临界点
                    if (event.getX() < radioWithStroke) {
                        leftBallPoint.x = radioWithStroke;
                        currentLeft = binarySearchKey(valueEntities, (int) leftBallPoint.x);
                    }


                    if (event.getX() < valueEntities.get(1).xCoordinat - radioWithStroke) {
                        currentLeft = valueEntities.get(0);
                    }

                    // 重新最终左边球抬起来的时候坐标
                    if (currentLeft != null) {
                        leftBallPoint.x = currentLeft.xCoordinat + radioWithStroke;
                        scaleSeekSectionRectF.left = leftBallPoint.x + radioWithStroke;
                        calculateLeftSticks();
                    }
                }

                if (rightChange) {
                    // 左边球的抬起位置
                    rightBallPoint.x = event.getX();

                    List<UnitValueEntity> temp = new ArrayList<>();
                    for (int i = currentLeft.position + 1; i < valueEntities.size(); i++) {
                        temp.add(valueEntities.get(i));
                    }

                    currentRight = binarySearchKey(temp, (int) event.getX());

                    // 右边界的限制坐标
                    if (event.getX() > getWidth() - radioWithStroke) {
                        rightBallPoint.x = getWidth() - radioWithStroke;
                        currentRight = binarySearchKey(valueEntities, (int) rightBallPoint.x);
                    }

                    if (valueEntities.size() > 1) {
                        if (event.getX() > valueEntities.get(valueEntities.size() - 2).xCoordinat) {
                            // 超过倒数第二个直接赋值最后一个
                            currentRight = valueEntities.get(valueEntities.size() - 1);
                        }
                    }

                    // 最后确认右边球抬起的最终位置
                    if (currentRight != null) {
                        rightBallPoint.x = currentRight.xCoordinat + radioWithStroke;
                        scaleSeekSectionRectF.right = rightBallPoint.x + radioWithStroke;
                        calculateRightSticks();
                    }
                }

                // 更新两个球的最后path
                scaleLeftBallPath = new Path();
                scaleLeftBallPath.addCircle(leftBallPoint.x, leftBallPoint.y, radioWithStroke, Path.Direction.CW);

                scaleRightBallPath = new Path();
                scaleRightBallPath.addCircle(rightBallPoint.x, rightBallPoint.y, radioWithStroke, Path.Direction.CW);

                scaleLeftBallRegion = updateRegionByPath(scaleLeftBallPath);
                scaleRightBallRegion = updateRegionByPath(scaleRightBallPath);

                // 重置标记位
                leftChange = false;
                rightChange = false;

                // 抬起的时候确认要显示的文案
                leftDesc = creatCurrentDataDesc(currentLeft);
                rightDesc = creatCurrentDataDesc(currentRight);

                if (this.seekBarDragListener != null) {
                    this.seekBarDragListener.seekMoveValue(currentLeft.value, currentRight.value);
                }
                break;
            default:
                break;
        }

        invalidate();
        return true;
    }

    /**
     * 创建画笔
     *
     * @return 画笔
     */
    private Paint creatPaint(int paintColor, int textSize, Paint.Style style, int lineWidth) {
        Paint paint = new Paint();
        paint.setColor(paintColor);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(lineWidth);
        paint.setDither(true);
        paint.setTextSize(textSize);
        paint.setStyle(style);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeJoin(Paint.Join.ROUND);
        return paint;
    }

    /**
     * 坐标对象
     */
    class SeekPoint {
        public float x;
        public float y;

        SeekPoint() {
        }

        public SeekPoint(float x, float y) {
            this.x = x;
            this.y = y;
        }
    }


    /**
     * 计算单位坐标下对应的具体的x坐标和对应的value
     *
     * @param scaleProgressUnit 单位
     * @param seekWidth         最大长度(去除两个半径的长度,因为最后一个球的终点是按照最左边来计算的)
     * @param maxValue          最大值
     * @return 返回对应坐标段数实体的集合
     */
    private List<UnitValueEntity> calculateAllXCoordinate(int scaleProgressUnit, float seekWidth, int maxValue) {
        List<UnitValueEntity> xValueEntitys = new ArrayList<>();
        //先判断是否有剩余不足一段的数据
        boolean hasLastOne = maxValue % scaleProgressUnit != 0;
        // 有的话,总数加1,没有的话,真好能除尽
        int count = hasLastOne ? maxValue / scaleProgressUnit + 1 : maxValue / scaleProgressUnit;
        // 计算各个点,对应生产的实体坐标
        for (int i = 0; i <= count; i++) {
            UnitValueEntity temp = new UnitValueEntity();
            temp.position = i;

            if (i != count) {
                temp.value = i * scaleProgressUnit;
                temp.xCoordinat = (seekWidth / maxValue) * i * scaleProgressUnit;
            } else {
                temp.value = maxValue;
                temp.xCoordinat = seekWidth;
            }

            xValueEntitys.add(temp);
        }
        return xValueEntitys;
    }

    /**
     * 根据传入的单位长度定义的实体对象
     */
    class UnitValueEntity implements Comparable<UnitValueEntity> {
        public int position;
        public float xCoordinat;
        public int value;


        @Override
        public int compareTo(@NonNull UnitValueEntity o) {
            return (int) (this.xCoordinat - o.xCoordinat);
        }

        @Override
        public String toString() {
            return "UnitValueEntity{" +
                    "position=" + position +
                    ", xCoordinat=" + xCoordinat +
                    ", value=" + value +
                    '}';
        }
    }

    /**
     * 查找最近接的数据点
     *
     * @param data      源数据集合
     * @param targetNum 当前的x轴的坐标
     * @return 当前点对应的实体
     */
    public UnitValueEntity binarySearchKey(List<UnitValueEntity> data, int targetNum) {
        if (data != null && data.size() > 0) {
            int left = 0, right = 0;
            for (right = data.size() - 1; left != right; ) {
                int midIndex = (right + left) / 2;
                int mid = (right - left);
                int midValue = (int) data.get(midIndex).xCoordinat;
                if (targetNum == midValue) {
                    return data.get(midIndex);
                }
                if (targetNum > midValue) {
                    left = midIndex;
                } else {
                    right = midIndex;
                }

                if (mid <= 1) {
                    break;
                }
            }
            UnitValueEntity rightnum = data.get(right);
            UnitValueEntity leftnum = data.get(left);
            return Math.abs((rightnum.xCoordinat - leftnum.xCoordinat) / 2) > Math.abs(rightnum.xCoordinat - targetNum) ? rightnum : leftnum;
        }
        return null;
    }

    /**
     * 返回文字的宽和高,x代表宽,y代表高度
     *
     * @param textPaint 画笔
     * @param text      文字
     * @return 文字的宽和高
     */
    private SeekPoint measureTextSize(Paint textPaint, String text) {
        SeekPoint point = new SeekPoint();
        if (!TextUtils.isEmpty(text)) {
            point.x = textPaint.measureText(text);
            Paint.FontMetrics fm = textPaint.getFontMetrics();
            point.y = (float) Math.ceil(fm.descent - fm.top);
        }
        return point;
    }

    /**
     * 当前对应坐标下的文案描述
     *
     * @param unitValueEntity 对应的坐标数据实体
     * @return 返回对应的文案
     */
    private String creatCurrentDataDesc(UnitValueEntity unitValueEntity) {
        return unitValueEntity == null ? symbolFront : symbolFront + unitValueEntity.value;
    }

    /**
     * 实时计算当前坐标对应的value值
     *
     * @param currentX  当前x坐标
     * @param maxValue  最大值
     * @param viewWidth view宽度
     * @return 当前值
     */
    private float realTimeComputation(float currentX, int maxValue, float viewWidth) {
        return maxValue * currentX / viewWidth;
    }

    /**
     * 透传给外界的值
     */
    public interface OnSeekBarDragListener {
        void seekMoveValue(int left, int right);
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        invalidate();
    }
}
