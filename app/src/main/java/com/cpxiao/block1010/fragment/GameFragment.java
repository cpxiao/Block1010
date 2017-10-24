package com.cpxiao.block1010.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cpxiao.R;
import com.cpxiao.androidutils.library.utils.MediaPlayerUtils;
import com.cpxiao.androidutils.library.utils.PreferencesUtils;
import com.cpxiao.androidutils.library.utils.ThreadUtils;
import com.cpxiao.block1010.imp.onGameListener;
import com.cpxiao.block1010.mode.extra.Extra;
import com.cpxiao.block1010.views.GameView;
import com.cpxiao.block1010.views.dialog.SettingsDialog;
import com.cpxiao.gamelib.fragment.BaseZAdsFragment;
import com.cpxiao.zads.core.ZAdPosition;

/**
 * @author cpxiao on 2017/09/06.
 */

public class GameFragment extends BaseZAdsFragment implements onGameListener {

    /**
     * 当前分数
     */
    private TextView mScoreView;
    /**
     * 最高分
     */
    private TextView mBestScoreView;
    /**
     * 游戏View
     */
    private GameView mGameView;

    private LinearLayout layout;

    public static GameFragment newInstance(Bundle bundle) {
        GameFragment fragment = new GameFragment();
        if (bundle != null) {
            fragment.setArguments(bundle);
        }
        return fragment;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        loadZAds(ZAdPosition.POSITION_GAME);

        final Context context = getHoldingActivity();
        MediaPlayerUtils.getInstance().init(context, R.raw.block1010_bgm);

        Bundle bundle = getArguments();
        if (bundle == null) {
            if (DEBUG) {
                throw new NullPointerException("bundle must not be null!");
            }
            return;
        }
        boolean isNewGame = bundle.getBoolean(Extra.Name.INTENT_NAME_IS_NEW_GAME, true);

        mScoreView = (TextView) view.findViewById(R.id.score);
        mBestScoreView = (TextView) view.findViewById(R.id.best);
        setScoreView(0);

        updateBestScore(0);

        //设置
        ImageView settingsView = (ImageView) view.findViewById(R.id.btn_settings);
        settingsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final SettingsDialog dialog = new SettingsDialog(context);
                dialog.setCancelable(true);
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();
            }
        });
        layout = (LinearLayout) view.findViewById(R.id.game_view);
        initGameView(isNewGame);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_game;
    }

    @Override
    public void onResume() {
        super.onResume();
        Context context = getHoldingActivity();
        boolean isMusicOn = PreferencesUtils.getBoolean(context, Extra.Key.SETTING_MUSIC, Extra.Key.SETTING_MUSIC_DEFAULT);
        if (isMusicOn) {
            MediaPlayerUtils.getInstance().start();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        MediaPlayerUtils.getInstance().pause();
        mGameView.save();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MediaPlayerUtils.getInstance().stop();
    }

    private void initGameView(boolean isNewGame) {
        loadZAds(ZAdPosition.POSITION_GAME);
        Context context = getHoldingActivity();
        layout.removeAllViews();
        mGameView = new GameView(context, 10, isNewGame);
        mGameView.setOnGameListener(this);
        layout.addView(mGameView);
    }


    private void setScoreView(int score) {
        String text = String.valueOf(score);
        mScoreView.setText(text);
    }

    private void setBestScoreView(int score) {
        String text = getResources().getString(R.string.best) + ": " + score;
        mBestScoreView.setText(text);
    }

    private void updateBestScore(int score) {
        Context context = getHoldingActivity();
        int bestScore = PreferencesUtils.getInt(context, Extra.Key.KEY_BEST_SCORE, 0);
        if (score > bestScore) {
            bestScore = score;
            PreferencesUtils.putInt(context, Extra.Key.KEY_BEST_SCORE, score);
        }
        setBestScoreView(bestScore);
    }


    @Override
    public void onScoreChange(final int score) {
        ThreadUtils.getInstance().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                setScoreView(score);
                updateBestScore(score);
            }
        });
    }

    @Override
    public void onGameOver() {
        final Context context = getHoldingActivity();
        showGameOverDialog(context);

    }

    private void showGameOverDialog(final Context context) {
        ThreadUtils.getInstance().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String title = getString(R.string.game_over);
                String msg = getString(R.string.play_again);
                AlertDialog dialog = new AlertDialog.Builder(context)
                        .setTitle(title)
                        .setMessage(msg)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                setScoreView(0);
                                initGameView(true);
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                removeFragment();
                            }
                        })
                        .create();
                dialog.setCancelable(false);
                dialog.show();
            }
        });
    }


    public static Bundle makeBundle(boolean newGame) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(Extra.Name.INTENT_NAME_IS_NEW_GAME, newGame);
        return bundle;
    }

}
