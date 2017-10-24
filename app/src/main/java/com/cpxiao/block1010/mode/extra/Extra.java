package com.cpxiao.block1010.mode.extra;

/**
 * Extra
 *
 * @author cpxiao on 2016/8/31
 */
public final class Extra {
    //    public static final class GameDifficulty {
    //        public static final int EASY = 1;
    //        public static final int NORMAL = 2;
    //        public static final int HARD = 3;
    //        public static final int INSANE = 4;
    //        public static final int DEFAULT = EASY;
    //    }


    /**
     * SharedPreferences 的key
     */
    public static final class Key {


        public static final String KEY_BEST_SCORE = "KEY_BEST_SCORE";

        /**
         * 音效开关，默认开
         */
        public static final String SETTING_SOUND = "SETTING_SOUND";
        public static final boolean SETTING_SOUND_DEFAULT = true;

        /**
         * 音乐开关，默认开
         */
        public static final String SETTING_MUSIC = "SETTING_MUSIC";
        public static final boolean SETTING_MUSIC_DEFAULT = true;


    }

    /**
     * Intent或Bundle的name
     */
    public static final class Name {
        public static final String INTENT_NAME_IS_NEW_GAME = "INTENT_NAME_IS_NEW_GAME";
    }

}
