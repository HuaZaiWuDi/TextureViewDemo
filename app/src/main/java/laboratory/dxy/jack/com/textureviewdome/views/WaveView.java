package laboratory.dxy.jack.com.textureviewdome.views;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import laboratory.dxy.jack.com.jackupdate.util.L;
import laboratory.dxy.jack.com.textureviewdome.views.animator.LinearEvaluator;


/**
 * The description of use:
 * <br />
 * Created time:2014/6/13 16:01
 * Created by Dave
 */
public class WaveView extends View implements LinearEvaluator.EvaluatorListener, Animator.AnimatorListener {

    private static final String LOG_TAG = WaveView.class.getSimpleName();
    private static final boolean DEBUG = true;

    public void debug(String msg) {
        if (DEBUG) android.util.Log.v(LOG_TAG, msg);
    }

    private static final long DURATION_DRAW_WAVE = 330;

    private static final int WAVE_PEAK_LEFT_HEIGHT = 1;

    /**
     * The max peek(contains the wave line) that will be shown
     */
    private static final int MAX_VALID_ENTRIES = 10;

    /**
     * the view width as 9 weights
     */
    private static final int WIDTH_TOTAL_WEIGHTS = 9;
    /**
     * the view height as 9 weights
     */
    private static final int HEIGHT_TOTAL_WEIGHTS = 9;

    //The start x and y of wave
    private float m_StartX, m_StartY;


    //The peak point, all of numbers are weight, such as (4/total_width_weight)*width, (0/total_height_weight)*height
    //(8,7) to (6,0)

    //need to draw line by other software

    //draw the wave peak when the valid heartrate is gotten.
    //LLL 0  1 2 3 4 5 6 7 8 9
    //0                    (7,0)[2]
    //1				  				
    //2							
    //3								
    //4							
    //5							
    //6			   				
    //7	  (0, 6)[5]	 (4,6)[4]	
    //8	
    //9				          (8,6)[1]  	 
    //10				(5,9)[3]


    private static final int[][] m_WavePeak_Weight = new int[][]{
            new int[]{4, 0, 6, 6},
            new int[]{5, 4, HEIGHT_TOTAL_WEIGHTS, 6},
            new int[]{7, 5, 0, HEIGHT_TOTAL_WEIGHTS},
            new int[]{WIDTH_TOTAL_WEIGHTS, 7, 6, 0}
    };

    //draw the line when the heartrate is not gotten.

    //LLL 0  1 2 3 4 5 6 7 8 9
    //0                       
    //1				  				
    //2			
    //3			
    //4			
    //5			
    //6			   
    //7	 (0, 6)[2]<<<<<<<<<<<<(8, 6)[1]	
    //8	
    //9				          	 
    //10				
    private static final int[] m_WaveLine_Weight = new int[]{WIDTH_TOTAL_WEIGHTS, 0, 6, 6};


    private List<float[]> m_PeakObjects;
    private float[] m_LineObjects;


    private CountDownTimer m_Timer;


    private Paint m_PeakPaint;
    private Paint m_EmptyPaint = new Paint();

    private Path m_WavePath = new Path();

    private WaveViewController m_InnerController;

    public WaveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setLayerType(View.LAYER_TYPE_HARDWARE, null);
        setValues();

    }

    private void setValues() {
        m_InnerController = new WaveViewController();

        m_PeakPaint = new Paint();
        m_PeakPaint.setColor(Color.WHITE);
        m_PeakPaint.setAntiAlias(true);
        m_PeakPaint.setStyle(Style.STROKE);
        m_PeakPaint.setStrokeWidth(2.f);
    }

    public void startPaint(final long duration, final long interval) {
        initWave();
        float extra = interval - DURATION_DRAW_WAVE;
        long tmp = interval;
        if (extra <= 200) {
            tmp = DURATION_DRAW_WAVE + 200;
        }
        final long newInterval = tmp;
        m_InnerController.saveStartPaintTime();
        m_Timer = new CountDownTimer(duration, newInterval) {
            @Override
            public void onTick(long millisUntilFinished) {
//            	disable hardware test
//                int value = 0;
//                if(m_TrickListener != null){
//                    value = m_TrickListener.getPainterValue();
//                }
//            	emulate test
                int value = m_InnerController.getEmulateHeartRate();
                if (m_TrickListener != null) {
                    m_TrickListener.onPainterTrick(value);
                }
                if (getVisibility() != View.VISIBLE) {
                    debug("The view is hidden by others");
                }
                m_InnerController.addPaintValue(value);
                startWave(value, DURATION_DRAW_WAVE);
            }

            @Override
            public void onFinish() {
                m_InnerController.computeAverage();
                if (m_TrickListener != null) {
                    m_TrickListener.onPainterFinished();
                }
            }
        };
        m_Timer.start();
    }

    private void initWave() {
        debug("initWave");
        m_WavePath = new Path();
        m_WavePath.moveTo(m_StartX, m_StartY);
        m_InnerController.clearHistory();
        invalidate();
    }

    private void startWave(int value, long duration) {
        doAnimation(value, duration);
    }


    private void doAnimation(int value, long duration) {
        if (value == 0) {
            Animator animator = getLineAnimator(m_LineObjects, duration);
            animator.addListener(this);
            animator.start();
        } else {
            AnimatorSet set = new AnimatorSet();
            List<Animator> peakAnim = getPeakAnimator(duration);
            set.playSequentially(peakAnim);
            set.start();
        }
    }

    private Animator getLineAnimator(float[] evaluatorObjects, long duration) {
        float startX = evaluatorObjects[0];
        float startY = evaluatorObjects[1];
        float endX = evaluatorObjects[2];
        float endY = evaluatorObjects[3];
        LinearEvaluator evaluator = new LinearEvaluator(startY, endY);
        evaluator.addListener(this);
        ValueAnimator animator = ValueAnimator.ofObject(evaluator, startX, endX);
        animator.setDuration((long) (duration * evaluatorObjects[4]));
        return animator;
    }

    private List<Animator> getPeakAnimator(long duration) {
        int size = m_PeakObjects.size();
        List<Animator> peakAnimators = new ArrayList<Animator>();
        for (int i = 0; i < size; i++) {
            Animator animator = getLineAnimator(m_PeakObjects.get(i), duration);
            peakAnimators.add(animator);
            if (i == size - 1) {
                animator.addListener(this);
            }
        }
        return peakAnimators;
    }

    public interface OnPainterTrickListener {

        int getPainterValue();

        void onPainterTrick(int value);

        void onPainterFinished();
    }

    private OnPainterTrickListener m_TrickListener;

    public void setOnPainterTrickListener(OnPainterTrickListener listener) {
        m_TrickListener = listener;
    }


    public void stopPaint() {
        if (m_Timer != null) {
            m_Timer.cancel();
        }

        recycleWaveBitmap();
    }


    public int getAverage() {
        return m_InnerController.getAverage();
    }

    public long getMeasureStartTime() {
        return m_InnerController.getMeasureStartTime();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        L.d("onSizeChanged");

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        L.d("onMeasure");
        doCalculate(getWidth(), getHeight());
    }

    private float m_CellWidth;

    private void doCalculate(int wWidth, int wHeight) {
        if (m_PeakObjects == null) {//Just testing condition.
            final int validWidth = wWidth;
            final int validHeight = wHeight;
            float cellWidth = validWidth / MAX_VALID_ENTRIES;
            float cellHeight = validHeight;
            m_CellWidth = cellWidth;
            caculateByWeights(cellWidth, cellHeight - WAVE_PEAK_LEFT_HEIGHT);
        }
    }


    private void caculateByWeights(float cellWidth, float cellHeight) {
        m_PeakObjects = new ArrayList<float[]>();
        int size = m_WavePeak_Weight.length;
        for (int i = size - 1; i >= 0; i--) {
            float[] wave = m_InnerController.retreiveWaveLine(m_WavePeak_Weight[i], cellWidth, cellHeight);
            //debugWaveLine(wave);
            if (i == size - 1) {
                m_StartX = wave[0];
                m_StartY = wave[1];
            }

            m_PeakObjects.add(wave);
        }
        m_LineObjects = m_InnerController.retreiveWaveLine(m_WaveLine_Weight, cellWidth, cellHeight);
        //debugWaveLine(m_LineObjects);
    }

    private void debugWaveLine(float[] waveLine) {
        debug(
                "the line Object " + "\n" +
                        "the startX is " + waveLine[0] + "\n" +
                        "the startY is " + waveLine[1] + "\n" +
                        "the endX is " + waveLine[2] + "\n" +
                        "the endY is " + waveLine[3] + "\n" +
                        "the fraction is " + waveLine[4] + "\n"
        );
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (waveBitmap != null) {
            canvas.drawBitmap(waveBitmap, 0, 0, m_EmptyPaint);
        }
    }

    private Bitmap waveBitmap;

    @Override
    public void onEvalutor(final float x, final float y) {
        debug("onEvalutor--- x is " + x + " & y is " + y);
        m_WavePath.lineTo(x, y);
        recycleWaveBitmap();
        waveBitmap = getBitmap(m_WavePath, m_PeakPaint);
        invalidate();
    }

    private void recycleWaveBitmap() {
        if (waveBitmap != null) {
            waveBitmap.recycle();
            waveBitmap = null;
        }
    }


    @Override
    public void onAnimationStart(Animator animation) {
    }

    @Override
    public void onAnimationEnd(Animator animation) {
        m_WavePath.offset(m_CellWidth, 0);
    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }


    private Bitmap getBitmap(Path path, Paint m_PeakPaint) {
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawPath(path, m_PeakPaint);
        return bitmap;
    }

    private static class WaveViewController {

        private List<Integer> m_AllEntries;
        private long m_StartTime;
        private int m_Average;
        private Random m_EmulateRan;
        private int[] m_EmulateHeartRate = new int[]{
                0, 49, 0,
                55, 0, 68,
                0, 66, 0,
                75
        };

//      private int[] m_EmulateHeartRate = new int[]{
//      35, 49, 46,
//      55, 88, 68,
//      35, 66, 67,
//      75
//};

//private int[] m_EmulateHeartRate = new int[]{
//      0, 0, 0,
//      0, 0, 0,
//      0, 0, 0,
//      0
//};

        private WaveViewController() {
            m_AllEntries = new ArrayList<Integer>();
            m_EmulateRan = new Random();
        }

        public long getMeasureStartTime() {
            return m_StartTime;
        }

        public int getAverage() {
            return m_Average;
        }

        public int getEmulateHeartRate() {
            return m_EmulateHeartRate[m_EmulateRan.nextInt(10)];
        }

        public void clearHistory() {
            m_AllEntries.clear();
        }

        public void computeAverage() {
            m_Average = calculateAverage();
        }

        private int calculateAverage() {
            int size = m_AllEntries.size();
            int validSize = 0;
            int validTotal = 0;
            for (int i = 0; i < size; i++) {
                if (m_AllEntries.get(i) != 0) {
                    validSize++;
                    validTotal += m_AllEntries.get(i);
                }
            }
            if (validSize != 0) {
                return validTotal / validSize;
            } else {
                return -1;
            }
        }

        public void addPaintValue(int value) {
            m_AllEntries.add(0, value);
        }

        public void saveStartPaintTime() {
            m_StartTime = System.currentTimeMillis();
        }

        private float[] retreiveWaveLine(int[] m_WavePeak_Weight, float cellWidth, float cellHeight) {
            int size = m_WavePeak_Weight.length;
            if (size != 4) {
                throw new RuntimeException("wrong size of array");
            }

            float startX, startY, endX = 0f, endY, fractionFromX, fractionToX, fractionFromY, fractionToY;
            int weightFromX, weightToX, weightFromY, weightToY;

            weightFromX = m_WavePeak_Weight[0];
            weightToX = m_WavePeak_Weight[1];
            weightFromY = m_WavePeak_Weight[2];
            weightToY = m_WavePeak_Weight[3];

            fractionFromX = getFractionOfWeight(weightFromX, WIDTH_TOTAL_WEIGHTS);
            fractionToX = getFractionOfWeight(weightToX, WIDTH_TOTAL_WEIGHTS);
            startX = cellWidth * fractionFromX;
            endX = cellWidth * fractionToX;

            fractionFromY = getFractionOfWeight(weightFromY, HEIGHT_TOTAL_WEIGHTS);
            fractionToY = getFractionOfWeight(weightToY, HEIGHT_TOTAL_WEIGHTS);
            startY = cellHeight * fractionFromY;
            endY = cellHeight * fractionToY;

            return new float[]{startX, startY, endX, endY, fractionFromX - fractionToX};
        }

        private float getFractionOfWeight(int weight, int totalWeights) {
            return (float) weight / totalWeights;
        }
    }
}
