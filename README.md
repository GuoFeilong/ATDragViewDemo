# ATDragViewDemo
自定义双向滑动的seekbar
## 目标:双向拖动的自定义View ##

国际惯例先预览后实现![这里写图片描述](http://img.blog.csdn.net/20160901105432584)

我们要实现的就是一个段位样式的拖动条,用来做筛选条件用的,
信心的朋友可能会发现微信里面有个一个通用字体的,
拖动然后改变字体大小;

这个相对比微信那个的自定义view算是一个扩展,因为我们是双向滑动,这个多考虑的一点就是手指拖动的是哪一个滑动块!

我们先看下GIF预览,然后我们今天就一步步实现这个小玩意...

![这里写图片描述](http://img.blog.csdn.net/20160901105705078)


----------
**实现步骤**

 - 自定义属性的抽取
 - view尺寸的计算
 - 相关内容的绘制(文字,原点,背景进度条,当前进度条等等)
 - 处理滑动事件
 


----------
大体思路分四部分;我们一步步来;简单的就一部带过了

 - 自定义属性获取:
 
 

```
 public ATDragView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setBackgroundColor(ContextCompat.getColor(context, android.R.color.white));
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ATDragView, defStyleAttr, R.style.def_dragview);
        int indexCount = typedArray.getIndexCount();
        for (int i = 0; i < indexCount; i++) {
            int attr = typedArray.getIndex(i);
            switch (attr) {
                case R.styleable.ATDragView_seek_bg_color:
                    seekBgColor = typedArray.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.ATDragView_seek_pb_color:
                    seekPbColor = typedArray.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.ATDragView_seek_ball_solid_color:
                    seekBallSolidColor = typedArray.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.ATDragView_seek_ball_stroke_color:
                    seekBallStrokeColor = typedArray.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.ATDragView_seek_text_color:
                    seekTextColor = typedArray.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.ATDragView_seek_text_size:
                    seekTextSize = typedArray.getDimensionPixelSize(attr, 0);
                    break;
            }
        }
        typedArray.recycle();
        init();
    }
```
拿到我们的属性后,初始化我们需要的工具,比如画笔,等

```
    private void init() {
        currentMovingType = BallType.LEFT;
        seekTextPaint = creatPaint(seekTextColor, seekTextSize, Paint.Style.FILL, 0);
        seekBgPaint = creatPaint(seekBgColor, 0, Paint.Style.FILL, 0);
        seekBallPaint = creatPaint(seekBallSolidColor, 0, Paint.Style.FILL, 0);
        seekPbPaint = creatPaint(seekPbColor, 0, Paint.Style.FILL, 0);
        seekBallEndPaint = creatPaint(seekPbColor, 0, Paint.Style.FILL, 0);
        seekBallStrokePaint = creatPaint(seekBallStrokeColor, 0, Paint.Style.FILL, 0);
        seekBallStrokePaint.setShadowLayer(5, 2, 2, seekBallStrokeColor);
    }

```

 - 确定自定义view尺寸
   

```
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int measureHeight;
        if (heightMode == MeasureSpec.AT_MOST || heightMode == MeasureSpec.UNSPECIFIED) {
            measureHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEF_HEIGHT, getContext().getResources().getDisplayMetrics());
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(measureHeight, MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
```

 - 绘制相关的内容部分,
   这里我们分析效果图发现,需要绘制五部分,两个圆,两个进度条一个 一堆文字,我们根据计算出来的view尺寸以及UI给的比例,即可绘制出来他们这个就是canvas的API使用
   

```
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawTexts(canvas);
        drawSeekBG(canvas);
        drawSeekPB(canvas);
        drawLeftCircle(canvas);
        drawRightCircle(canvas);
    }
```

具体的文字绘制,是根据外界传入的数据来绘制的所以细节如下

```
  private void drawTexts(Canvas canvas) {
        if (null == data) return;
        int size = data.size();
        int unitWidth = getUnitWidth(size - 1);
        for (int i = 0; i < size; i++) {
            String tempDesc = data.get(i);
            float measureTextWidth = seekPbPaint.measureText(tempDesc);
            canvas.drawText(tempDesc, DEF_PADDING + unitWidth * i - measureTextWidth / 2, seekTextY, seekTextPaint);
        }
    }
```


----------
**这个View的核心部分不是绘制,而是计算,描述下我们具体的确定位置的思路**

 1. 根据外界传入的数据集合平均分view的宽度,求得平均一份的宽度大小
 2. 然后循环数据集合根据平均一份的宽度,确定没个文字所在的坐标值
 


----------
然后我们看下计算的代码;

```
// 计算单位宽度,view宽度-内容的左右边距以及圆球的半径,自己体会下为什么
   private int getUnitWidth(int count) {
        return (viewWidth - 2 * DEF_PADDING - 2 * seekBallRadio) / count;
    }
```
**这个方法可以说是最重要的一个了,**
```
//根据当前手指触摸的x坐标计算,手指离开屏幕以后,应该停留到哪个位置,比如滑动到400到500之间但是不到600,我们不能让他停留在半路上,让他自动找回他停留的左边,也就是GIF中的小小回弹效果
  private int getCurrentSeekX(int upX) {
        if (null == data) {
            return 0;
        }
        int unitWidth = getUnitWidth(data.size() - 1);
        return unitWidth * (upX / unitWidth);
    }
```


----------
**核心的代码全部完毕了,我们看下onTouch里面的处理**

```
 public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            //记录手指按下的坐标
                downX = (int) event.getX();
                // 根据当前坐标,确定要移动哪个球球,因为我们说了,我们这个是有两个球的,唯一的一个技巧点就是这个地方,根据手指按下的坐标找到距离哪个球位置最近就移动哪个球,这里注意下.
                currentMovingType = getMovingLeftOrRight(downX);
                if (BallType.LEFT == currentMovingType) {
                    leftSeekBallX = downX;
                } else if (BallType.RIGHT == currentMovingType) {
                    rightSeekBallX = downX;
                }
                seekPbRectF = new RectF(leftSeekBallX, viewHeight * SEEK_BG_SCALE, rightSeekBallX, viewHeight * SEEK_BG_SCALE + BG_HEIGHT);

                break;
            case MotionEvent.ACTION_MOVE:
            //移动的时候根据计算出来的位置以及方向改变两个小球的位置以及举行进度条的RectF的范围
                int moveX = (int) event.getX();
                // 特殊情况处理,两个球重合应该怎么办,
                if (leftSeekBallX == rightSeekBallX) {
                    if (moveX - downX > 0) {
                        currentMovingType = BallType.RIGHT;
                        rightSeekBallX = moveX;
                    } else {
                        currentMovingType = BallType.LEFT;
                        leftSeekBallX = moveX;
                    }
                } else {
                    if (BallType.LEFT == currentMovingType) {
                        leftSeekBallX = leftSeekBallX - rightSeekBallX >= 0 ? rightSeekBallX : moveX;
                    } else if (BallType.RIGHT == currentMovingType) {
                        rightSeekBallX = rightSeekBallX - leftSeekBallX <= 0 ? leftSeekBallX : moveX;
                    }
                }
                seekPbRectF = new RectF(leftSeekBallX, viewHeight * SEEK_BG_SCALE, rightSeekBallX, viewHeight * SEEK_BG_SCALE + BG_HEIGHT);
                break;
            case MotionEvent.ACTION_UP:
            // 手指离开的时候,确定返回给UI的数据集
                if (BallType.LEFT == currentMovingType) {
                    leftPosition = getDataPosition((int) event.getX());
                    leftSeekBallX = leftSeekBallX - rightSeekBallX >= 0 ? rightSeekBallX : getCurrentSeekX((int) event.getX()) + DEF_PADDING + seekBallRadio;
                } else if (BallType.RIGHT == currentMovingType) {
                    rightPosition = getDataPosition((int) event.getX());
                    rightSeekBallX = rightSeekBallX - leftSeekBallX <= 0 ? leftSeekBallX : getCurrentSeekX((int) event.getX()) + DEF_PADDING + seekBallRadio;
                }
                if (null != dragFinishedListener) {
                    dragFinishedListener.dragFinished(leftPosition, rightPosition);
                }
                break;
        }
// 边界处理,确保左边的球不会超过右边的,右边的不会超过左边的
        if (BallType.LEFT == currentMovingType) {
            if (leftSeekBallX < seekBallRadio + DEF_PADDING) {
                leftSeekBallX = seekBallRadio + DEF_PADDING;
            }
            if (leftSeekBallX > viewWidth - seekBallRadio - DEF_PADDING) {
                leftSeekBallX = viewWidth - seekBallRadio - DEF_PADDING;
            }
        } else if (BallType.RIGHT == currentMovingType) {
            if (rightSeekBallX < seekBallRadio + DEF_PADDING) {
                rightSeekBallX = seekBallRadio + DEF_PADDING;
            }
            if (rightSeekBallX > viewWidth - seekBallRadio - DEF_PADDING) {
                rightSeekBallX = viewWidth - seekBallRadio - DEF_PADDING;
            }
        }
        seekPbRectF = new RectF(leftSeekBallX, viewHeight * SEEK_BG_SCALE, rightSeekBallX, viewHeight * SEEK_BG_SCALE + BG_HEIGHT);
        invalidate();
        return true;
    }
```


----------
**大部分的核心的代码就这么多,然后剩下的view写完了就该把回调借口透出给UI 完活了.....**

```
public void setData(List<String> data, OnDragFinishedListener dragFinishedListener) {
        this.dragFinishedListener = dragFinishedListener;
        this.data = data;
        leftPosition = 0;
        if (null != data && data.size() != 0) {
            rightPosition = data.size() - 1;
        }
    }
```

## 写了这么多重要的还是这个地方源代码下载地址[https://github.com/GuoFeilong/ATDragViewDemo](https://github.com/GuoFeilong/ATDragViewDemo) ##

## 希望大家多多多star一下谢谢 ##


----------
## END ##

