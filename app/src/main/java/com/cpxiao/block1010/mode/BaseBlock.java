package com.cpxiao.block1010.mode;

import android.content.Context;
import android.graphics.Color;

import com.cpxiao.R;
import com.cpxiao.block1010.mode.extra.BaseBlockData;

import java.util.Random;


/**
 * @author cpxiao on 2015/10/19.
 */
public class BaseBlock {
    public int[][] baseData;
    public int color;
    public int sizeWidth;
    public int sizeHeight;
    public int baseScore;

    public BaseBlock(Context c) {
        this.baseData = BaseBlockData.getBaseData();
        this.color = getColor(c);
        this.sizeWidth = getSizeWidth();
        this.sizeHeight = getSizeHeight();
        this.baseScore = getBaseScore();
    }

    private int getColor(Context c) {
        String[] colors = c.getResources().getStringArray(R.array.app);
        Random random = new Random();
        int i = random.nextInt(colors.length);
        return Color.parseColor(colors[i]);
    }

    private int getSizeWidth() {
        int result = 0;
        if (baseData != null) {
            for (int i = 0; i < BaseBlockData.ROW_NUM; i++) {
                for (int j = 0; j < BaseBlockData.ROW_NUM; j++) {
                    if (baseData[i][j] > 0 && j >= result) {
                        result = j + 1;
                    }
                }
            }
        }
        return result;
    }

    private int getSizeHeight() {
        int result = 0;
        if (baseData != null) {
            for (int i = 0; i < BaseBlockData.ROW_NUM; i++) {
                for (int j = 0; j < BaseBlockData.ROW_NUM; j++) {
                    if (baseData[i][j] > 0 && i >= result) {
                        result = i + 1;
                    }
                }
            }
        }
        return result;
    }

    private int getBaseScore() {
        int result = 0;
        if (baseData != null) {
            for (int i = 0; i < BaseBlockData.ROW_NUM; i++) {
                for (int j = 0; j < BaseBlockData.ROW_NUM; j++) {
                    if (baseData[i][j] > 0) {
                        result++;
                    }
                }
            }
        }
        return result;
    }

}
