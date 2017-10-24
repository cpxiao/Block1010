package com.cpxiao.block1010.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cpxiao.R;
import com.cpxiao.androidutils.library.utils.PreferencesUtils;
import com.cpxiao.block1010.mode.extra.Extra;
import com.cpxiao.gamelib.fragment.BaseZAdsFragment;
import com.cpxiao.zads.core.ZAdPosition;

/**
 * @author cpxiao on 2017/09/06.
 */

public class SettingsFragment extends BaseZAdsFragment implements View.OnClickListener {

    private TextView mSound;
    private TextView mMusic;

    public static SettingsFragment newInstance(Bundle bundle) {
        SettingsFragment fragment = new SettingsFragment();
        if (bundle != null) {
            fragment.setArguments(bundle);
        }
        return fragment;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        loadZAds(ZAdPosition.POSITION_SETTINGS);

        Context context = getHoldingActivity();
        view.findViewById(R.id.layout_sound).setOnClickListener(this);
        view.findViewById(R.id.layout_music).setOnClickListener(this);
        mSound = (TextView) view.findViewById(R.id.tv_sound);
        mMusic = (TextView) view.findViewById(R.id.tv_music);

        boolean isSoundOn = PreferencesUtils.getBoolean(context, Extra.Key.SETTING_SOUND, Extra.Key.SETTING_SOUND_DEFAULT);
        if (isSoundOn) {
            mSound.setText(R.string.settings_on);
        } else {
            mSound.setText(R.string.settings_off);
        }

        boolean isMusicOn = PreferencesUtils.getBoolean(context, Extra.Key.SETTING_MUSIC, Extra.Key.SETTING_MUSIC_DEFAULT);
        if (isMusicOn) {
            mMusic.setText(R.string.settings_on);
        } else {
            mMusic.setText(R.string.settings_off);
        }

        Button okBtn = (Button) view.findViewById(R.id.ok);
        okBtn.setOnClickListener(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_settings;
    }

    @Override
    public void onClick(View v) {
        Context context = getHoldingActivity();
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
            } else {
                PreferencesUtils.putBoolean(context, Extra.Key.SETTING_MUSIC, true);
                mMusic.setText(R.string.settings_on);
            }
        } else if (id == R.id.ok) {
            removeFragment();
        }
    }

}
