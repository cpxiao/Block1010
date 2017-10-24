package com.cpxiao.block1010.mode;


/**
 * @author cpxiao on 2015/10/19.
 */
public class SingleBlock {

    /**
     * 数据状态，是否已填充
     */
    public int mData;

    /**
     * 颜色
     */
    public int mColor;
    private int mColorDefault;

    /**
     * 是否需要重置，在判断是否消除时使用
     */
    public boolean ifNeedReset;

    /**
     * 是否临时着色
     */
    public boolean isTempColor = false;

    private SingleBlock(Builder build) {
        this.mColorDefault = build.colorDefault;
        this.mColor = build.colorDefault;
    }

    public void reset() {
        mData = 0;
        mColor = mColorDefault;
        ifNeedReset = false;
    }

    public static class Builder {
        private int colorDefault;

        public Builder setColorDefault(int colorDefault) {
            this.colorDefault = colorDefault;
            return this;
        }

        public SingleBlock build() {
            return new SingleBlock(this);
        }
    }
}
