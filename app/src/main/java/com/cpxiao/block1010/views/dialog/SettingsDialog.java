package com.cpxiao.block1010.views.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.cpxiao.R;
import com.cpxiao.androidutils.library.utils.MediaPlayerUtils;
import com.cpxiao.androidutils.library.utils.PreferencesUtils;
import com.cpxiao.block1010.mode.extra.Extra;

/**
 * @author cpxiao on 2015/10/22.
 */
public class SettingsDialog extends Dialog implements View.OnClickListener {

    private TextView mSound;
    private TextView mMusic;

    public SettingsDialog(Context context) {
        super(context, R.style.ScoreDialog);
        initWidget();
    }

    private void initWidget() {
        setContentView(R.layout.dialog_settings);
        // 点击外面区域不会让dialog消失
        setCanceledOnTouchOutside(false);
        setCancelable(true);

        findViewById(R.id.layout_sound).setOnClickListener(this);
        findViewById(R.id.layout_music).setOnClickListener(this);
        findViewById(R.id.dialog_btn_ok).setOnClickListener(this);

        mSound = (TextView) findViewById(R.id.tv_sound);
        mMusic = (TextView) findViewById(R.id.tv_music);

        boolean isSoundOn = PreferencesUtils.getBoolean(getContext(), Extra.Key.SETTING_SOUND, Extra.Key.SETTING_SOUND_DEFAULT);
        if (isSoundOn) {
            mSound.setText(R.string.settings_on);
        } else {
            mSound.setText(R.string.settings_off);
        }

        boolean isMusicOn = PreferencesUtils.getBoolean(getContext(), Extra.Key.SETTING_MUSIC, Extra.Key.SETTING_MUSIC_DEFAULT);
        if (isMusicOn) {
            mMusic.setText(R.string.settings_on);
        } else {
            mMusic.setText(R.string.settings_off);
        }

    }


    @Override
    public void onClick(View v) {
        Context context = getContext();
        int id = v.getId();

        if (id == R.id.layout_sound) {
            boolean isSoundOn = PreferencesUtils.getBoolean(context, Extra.Key.SETTING_SOUND, Extra.Key.SETTING_SOUND_DEFAULT);
            if (isSoundOn) {
                PreferencesUtils.putBoolean(context, Extra.Key.SETTING_SOUND, false);
                mSound.setText(R.string.settings_off);
            } else {
                PreferencesUtils.putBoolean(context, Extra.Key.SETTING_SOUND, true);
                mSound.setText(R.string.settings_on);
            }
        } else if (id == R.id.layout_music) {
            boolean isMusicOn = PreferencesUtils.getBoolean(context, Extra.Key.SETTING_MUSIC, Extra.Key.SETTING_MUSIC_DEFAULT);
            if (isMusicOn) {
                PreferencesUtils.putBoolean(context, Extra.Key.SETTING_MUSIC, false);
                mMusic.setText(R.string.settings_off);
                MediaPlayerUtils.getInstance().pause();
            } else {
                PreferencesUtils.putBoolean(context, Extra.Key.SETTING_MUSIC, true);
                mMusic.setText(R.string.settings_on);
                MediaPlayerUtils.getInstance().start();
            }
        } else if (id == R.id.dialog_btn_ok) {
            dismiss();
        }
    }
}
