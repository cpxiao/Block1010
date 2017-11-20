package com.cpxiao.block1010.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.MotionEvent;

import com.cpxiao.AppConfig;
import com.cpxiao.R;
import com.cpxiao.androidutils.library.utils.PreferencesUtils;
import com.cpxiao.androidutils.library.utils.SoundPoolUtils;
import com.cpxiao.block1010.imp.onGameListener;
import com.cpxiao.block1010.mode.BaseBlock;
import com.cpxiao.block1010.mode.SingleBlock;
import com.cpxiao.block1010.mode.extra.BaseBlockData;
import com.cpxiao.block1010.mode.extra.Extra;
import com.cpxiao.gamelib.views.BaseSurfaceView;

import java.util.HashMap;


/**
 * @author cpxiao on 2015/10/19.
 * @version cpxiao on 2017/10/24. 继承BaseSurfaceViewFPS
 */
public class GameView extends BaseSurfaceView {
    private static final boolean DEBUG = AppConfig.DEBUG;
    private static final String TAG = GameView.class.getSimpleName();

    private Context mContext;

    private int mGameType = 10;
    private float mPaddingTop = 0, mPaddingLeft = 0;
    private float mBlockWH;

    /**
     * 分数
     */
    private int mScore = 0;
    private onGameListener mGameListener;

    /**
     * 待选图案
     */
    private static final float BASE_BLOCK_PERCENTAGE_SMALL = 0.618f;
    private static final float BASE_BLOCK_PERCENTAGE_BIG = 0.88f;
    private BaseBlock[] mBaseBlockArray;
    private static final int mBaseBlockCount = 3;
    private int mBaseBlockChecked = -1;
    /**
     * 绘制的待选块占格子数
     */
    private static final int BASE_BLOCK_COUNT = 4;

    private int mBaseBlockSizeSmall;
    private int mBaseBlockSizeBig;

    private SingleBlock[][] mBlockStore;

    private static final int SOUND_POOL_CLEAR = 0;


    public GameView(Context context) {
        super(context);
        init(context, true);
    }

    public GameView(Context context, int gameType, boolean isNewGame) {
        super(context);
        mContext = context;
        mGameType = gameType;
        init(context, isNewGame);
    }


    private void init(Context c, boolean isNewGame) {
        initSound(c);

        mScore = 0;

        //若读取进度失败，开始新游戏
        if (mBlockStore == null) {
            mScore = 0;
            initBlocks();
        }
        initBaseBlock();
    }

    private void initSound(Context context) {
        SoundPoolUtils.getInstance().createSoundPool(20);
        HashMap<Integer, Integer> map = new HashMap<>();
        map.put(SOUND_POOL_CLEAR, R.raw.clear);
        SoundPoolUtils.getInstance().loadSound(context, map);
    }

    public void setOnGameListener(onGameListener listener) {
        mGameListener = listener;
    }

    public void save() {
        saveScore();
        saveBlocks();
    }

    private void saveScore() {
    }

    private void saveBlocks() {
        StringBuilder tmp = new StringBuilder();
        for (int i = 0; i < mGameType; i++) {
            for (int j = 0; j < mGameType; j++) {
                if (i == mGameType && j == mGameType) {
                    tmp.append(mBlockStore[i][j].mData).append(",").append(mBlockStore[i][j].mColor);
                } else {
                    tmp.append(mBlockStore[i][j].mData).append(",").append(mBlockStore[i][j].mColor).append(";");
                }
            }
        }
    }


    private void initBlocks() {
        mBlockStore = new SingleBlock[mGameType][mGameType];
        int colorDefault = ContextCompat.getColor(mContext, R.color.colorBlockDefault);
        for (int i = 0; i < mGameType; i++) {
            for (int j = 0; j < mGameType; j++) {
                mBlockStore[j][i] = new SingleBlock.Builder().setColorDefault(colorDefault).build();
            }
        }
    }

    private void initBaseBlock() {
        mBaseBlockArray = new BaseBlock[mBaseBlockCount];
        for (int i = 0; i < mBaseBlockCount; i++) {
            mBaseBlockArray[i] = new BaseBlock(mContext);
        }
    }

    @Override
    protected void initWidget() {
        setBgColor(ContextCompat.getColor(mContext, R.color.colorBackgroundDefault));


        int blockWidth = mViewWidth / (mGameType + 2);
        int blockHeight = mViewHeight / (mGameType + 4);

        mBlockWH = Math.min(blockWidth, blockHeight);
        mBaseBlockSizeSmall = (int) (mBlockWH * BASE_BLOCK_PERCENTAGE_SMALL);
        mBaseBlockSizeBig = (int) (mBlockWH * BASE_BLOCK_PERCENTAGE_BIG);

        mPaddingTop = (mViewHeight - mGameType * mBlockWH - BaseBlockData.ROW_NUM * mBaseBlockSizeSmall) / 2;
        mPaddingLeft = (mViewWidth - mGameType * mBlockWH) / 2;

    }

    protected void logic() {

        //判断横排
        int line_num_h = 0;
        for (int i = 0; i < mGameType; i++) {
            int count = 0;
            for (int j = 0; j < mGameType; j++) {
                if (mBlockStore[j][i].mData > 0) {
                    count++;
                }
            }
            if (count == mGameType) {
                line_num_h++;
                for (int k = 0; k < mGameType; k++) {
                    mBlockStore[k][i].ifNeedReset = true;
                }
            }
        }

        //判断竖排
        int line_num_v = 0;
        for (int i = 0; i < mGameType; i++) {
            int count = 0;
            for (int j = 0; j < mGameType; j++) {
                if (mBlockStore[i][j].mData > 0) {
                    count++;
                }
            }
            if (count == mGameType) {
                line_num_v++;
                for (int k = 0; k < mGameType; k++) {
                    mBlockStore[i][k].ifNeedReset = true;
                }
            }
        }

        //更新数据
        for (int i = 0; i < mGameType; i++) {
            for (int j = 0; j < mGameType; j++) {
                if (mBlockStore[i][j].ifNeedReset) {
                    mBlockStore[i][j].reset();
                }
            }
        }

        int line_num_sum = line_num_h + line_num_v;
        if (line_num_sum > 0) {
            if (DEBUG) {
                Log.d("CPXIAO", "sum = " + line_num_sum + ",h = " + line_num_h + ",v = " + line_num_v);
            }
            boolean isSoundOn = PreferencesUtils.getBoolean(getContext(), Extra.Key.SETTING_SOUND, Extra.Key.SETTING_SOUND_DEFAULT);
            for (int i = 0; i < line_num_sum; i++) {
                if (isSoundOn) {
                    SoundPoolUtils.getInstance().play(SOUND_POOL_CLEAR);
                }
            }
        }
        mScore += mGameType * ((line_num_sum + 1) * line_num_sum / 2);
        //更新分数
        if (mGameListener != null) {
            mGameListener.onScoreChange(mScore);
        }
    }

    @Override
    public void drawCache() {
        if (mCanvasCache == null || mBaseBlockArray == null) {
            return;
        }
        mCanvasCache.drawColor(ContextCompat.getColor(mContext, R.color.colorBackgroundDefault));
        drawBlocks(mCanvasCache);
        drawAllBaseBlock(mCanvasCache, mBaseBlockArray);
    }

    /**
     * 绘制圆角正方形
     *
     * @param canvas         canvas
     * @param x              正方形中心x坐标
     * @param y              正方形中心y坐标
     * @param halfSideLength 正方形边长的一半
     * @param paint          paint
     */
    private void drawRoundSquare(Canvas canvas, float x, float y, float halfSideLength, Paint paint) {
        halfSideLength -= Math.max(halfSideLength / 12, 1.5f);
        RectF rectF = new RectF(x - halfSideLength, y - halfSideLength, x + halfSideLength, y + halfSideLength);
        canvas.drawRoundRect(rectF, 0.3f * halfSideLength, 0.3f * halfSideLength, paint);
    }

    /**
     * 绘制游戏区域方块
     *
     * @param canvas canvas
     */
    private void drawBlocks(Canvas canvas) {
        for (int i = 0; i < mGameType; i++) {
            for (int j = 0; j < mGameType; j++) {
                SingleBlock block = mBlockStore[i][j];
                mPaint.setColor(block.mColor);
                if (block.isTempColor) {
                    mPaint.setAlpha(160);
                } else {
                    mPaint.setAlpha(255);
                }
                drawRoundSquare(canvas, mPaddingLeft + (j + 0.5f) * mBlockWH, mPaddingTop + (i + 0.5f) * mBlockWH,
                        mBlockWH / 2, mPaint);
            }
        }
    }

    /**
     * 绘制游戏待选方块
     *
     * @param canvas canvas
     * @param data   data
     */
    private void drawAllBaseBlock(Canvas canvas, BaseBlock[] data) {
        drawBaseBlock(canvas, data, false);
        drawBaseBlock(canvas, data, true);

    }


    /**
     * 绘制游戏待选方块
     *
     * @param canvas         canvas
     * @param baseBlockArray baseBlockArray
     * @param isChecked      isChecked
     */
    private void drawBaseBlock(Canvas canvas, BaseBlock[] baseBlockArray, boolean isChecked) {
        if (canvas == null || baseBlockArray == null || baseBlockArray.length < mBaseBlockCount) {
            return;
        }
        for (int i = 0; i < mBaseBlockCount; i++) {
            float x = mPaddingLeft + i * mBlockWH * BASE_BLOCK_COUNT;
            float y = mPaddingTop + mBaseBlockSizeSmall + mGameType * mBlockWH;

            if (isChecked && i == mBaseBlockChecked) {
                drawBaseBlock(canvas, eventX - differentX, eventY - differentY, baseBlockArray[i],
                        mBaseBlockSizeBig, true);
            } else if (!isChecked && i != mBaseBlockChecked) {
                drawBaseBlock(canvas, x, y, baseBlockArray[i], mBaseBlockSizeSmall, false);
            }
        }
    }


    private void drawBaseBlock(Canvas canvas, float x, float y, BaseBlock baseBlock, int baseBlockSize,
                               boolean isChecked) {
        mPaint.setColor(baseBlock.color);

        for (int i = 0; i < BaseBlockData.ROW_NUM; i++) {
            for (int j = 0; j < BaseBlockData.ROW_NUM; j++) {
                if (baseBlock.baseData[i][j] == 1) {
                    if (isChecked) {
                        drawRoundSquare(canvas, x + (j + 0.5f) * mBlockWH, y + (i + 0.5f) *
                                mBlockWH, baseBlockSize / 2, mPaint);
                    } else {
                        drawRoundSquare(canvas, x + (j + 0.5f) * mBaseBlockSizeSmall, y + (i + 0.5f) *
                                mBaseBlockSizeSmall, baseBlockSize / 2, mPaint);
                    }
                }
            }
        }
    }

    /**
     * 当前点击坐标位置
     */
    float eventX, eventY;
    /**
     * 当前点击位置与baseBlock左上角的差值
     */
    float differentX, differentY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        eventX = event.getX();
        eventY = event.getY();
        if (event.getAction() == MotionEvent.ACTION_DOWN) {

            float minY = (mPaddingTop + mBaseBlockSizeSmall + mGameType * mBlockWH);
            float maxY = minY + mBlockWH * BASE_BLOCK_COUNT;
            if (eventY >= minY && eventY <= maxY) {
                for (int i = 0; i < mBaseBlockCount; i++) {
                    float minX = mPaddingLeft + i * mBlockWH * BASE_BLOCK_COUNT;
                    float maxX = minX + mBlockWH * BASE_BLOCK_COUNT;
                    if (eventX >= minX && eventX <= maxX) {
                        mBaseBlockChecked = i;
                        differentX = (int) (eventX - minX);
                        differentY = mBlockWH * (mBaseBlockArray[i].sizeHeight + 1f);
                    }
                }
            }
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            if (mBaseBlockChecked >= 0) {
                int numX = (int) ((eventX - differentX - mPaddingLeft + mBlockWH * 0.5) / mBlockWH);
                int numY = (int) ((eventY - differentY - mPaddingTop + mBlockWH * 0.5) / mBlockWH);

                if (isCanBePlace(numX, numY, mBaseBlockArray[mBaseBlockChecked])) {
                    //放置成功，更新数据
                    mScore += mBaseBlockArray[mBaseBlockChecked].baseScore;
                    updateBlocks(numX, numY, true);
                    //更新待选块数据
                    updateBaseBlock();
                    mBaseBlockChecked = -1;
                } else {
                    //baseBlock回归原处
                    mBaseBlockChecked = -1;
                }
            }
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            if (mBaseBlockChecked >= 0) {
                int numX = (int) ((eventX - differentX - mPaddingLeft + mBlockWH * 0.5) / mBlockWH);
                int numY = (int) ((eventY - differentY - mPaddingTop + mBlockWH * 0.5) / mBlockWH);

                if (isCanBePlace(numX, numY, mBaseBlockArray[mBaseBlockChecked])) {
                    //放置成功，更新数据
                    updateBlocks(numX, numY, false);
                } else {
                    clearTempColor();
                }
            }
        }

        logic();
        myDraw();
        //判断游戏是否结束
        if (isGameOver()) {
            if (DEBUG) {
                Log.d(TAG, "game over!!!!!");
            }
            //            thread_over_flag = true;
            if (mGameListener != null) {
                mGameListener.onGameOver();
            }
        }
        return true;
    }


    private boolean updateBlocks(int x, int y, boolean isSave) {
        clearTempColor();
        for (int i = 0; i < BaseBlockData.ROW_NUM; i++) {
            for (int j = 0; j < BaseBlockData.ROW_NUM; j++) {
                if (mBaseBlockArray[mBaseBlockChecked].baseData[i][j] > 0) {
                    if (isSave) {
                        SingleBlock block = mBlockStore[i + y][j + x];
                        block.mData = 1;
                        block.mColor = mBaseBlockArray[mBaseBlockChecked].color;
                    } else {
                        SingleBlock block = mBlockStore[i + y][j + x];
                        block.mColor = mBaseBlockArray[mBaseBlockChecked].color;
                        block.isTempColor = true;
                    }
                }
            }
        }
        return true;
    }

    private void clearTempColor() {
        for (int y = 0; y < mBlockStore.length; y++) {
            for (int x = 0; x < mBlockStore[0].length; x++) {
                SingleBlock block = mBlockStore[y][x];
                if (block.isTempColor) {
                    block.isTempColor = false;
                    block.reset();
                }
            }
        }
    }

    private void updateBaseBlock() {
        if (mBaseBlockChecked >= 0) {
            mBaseBlockArray[mBaseBlockChecked] = new BaseBlock(mContext);
        }
    }


    private boolean isGameOver() {
        for (int i = 0; i < mBaseBlockCount; i++) {
            for (int x = 0; x < mGameType; x++) {
                for (int y = 0; y < mGameType; y++) {
                    if (isCanBePlace(x, y, mBaseBlockArray[i])) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public boolean isCanBePlace(int x, int y, BaseBlock baseBlock) {
        for (int i = 0; i < BaseBlockData.ROW_NUM; i++) {
            for (int j = 0; j < BaseBlockData.ROW_NUM; j++) {
                if (baseBlock.baseData[i][j] > 0) {
                    if (y + i < 0 || x + j < 0) {
                        return false;
                    } else if (y + i >= mGameType || x + j >= mGameType) {
                        return false;
                    } else if (mBlockStore[y + i][x + j].mData > 0) {
                        return false;
                    }
                }
            }
        }
        return true;
    }


}
