package com.cpxiao.block1010.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageButton;

import com.cpxiao.R;
import com.cpxiao.androidutils.library.utils.PreferencesUtils;
import com.cpxiao.androidutils.library.utils.RateAppUtils;
import com.cpxiao.androidutils.library.utils.ShareAppUtils;
import com.cpxiao.block1010.mode.extra.Extra;
import com.cpxiao.gamelib.fragment.BaseZAdsFragment;
import com.cpxiao.zads.core.ZAdPosition;


/**
 * @author cpxiao on 2017/09/06.
 */

public class HomeFragment extends BaseZAdsFragment implements View.OnClickListener {
    public static HomeFragment newInstance(Bundle bundle) {
        HomeFragment fragment = new HomeFragment();
        if (bundle != null) {
            fragment.setArguments(bundle);
        }
        return fragment;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        loadZAds(ZAdPosition.POSITION_HOME);

        ImageButton btnPlay = (ImageButton) view.findViewById(R.id.btn_play);
        ImageButton btnRate = (ImageButton) view.findViewById(R.id.rate_app);
        ImageButton btnShare = (ImageButton) view.findViewById(R.id.share);
        ImageButton btnBestScore = (ImageButton) view.findViewById(R.id.best_score);
        ImageButton btnSettings = (ImageButton) view.findViewById(R.id.settings);

        btnPlay.setOnClickListener(this);
        btnRate.setOnClickListener(this);
        btnShare.setOnClickListener(this);
        btnBestScore.setOnClickListener(this);
        btnSettings.setOnClickListener(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        Context context = getHoldingActivity();
        if (id == R.id.btn_play) {
            Bundle bundle = GameFragment.makeBundle(true);
            addFragment(GameFragment.newInstance(bundle));
        } else if (id == R.id.rate_app) {
            RateAppUtils.rate(context);
        } else if (id == R.id.share) {
            String msg = getString(R.string.share_msg) + "\n" +
                    getString(R.string.app_name) + "\n" +
                    "https://play.google.com/store/apps/details?id=" + context.getPackageName();
            ShareAppUtils.share(context, getString(R.string.share), msg);
        } else if (id == R.id.best_score) {
            showBestScoreDialog(context);
        } else if (id == R.id.settings) {
            addFragment(SettingsFragment.newInstance(null));
        }
    }

    private void showBestScoreDialog(Context context) {

        String msg = "" + PreferencesUtils.getInt(context, Extra.Key.KEY_BEST_SCORE, 0);
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle(R.string.best_score)
                .setMessage(msg)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.show();
    }
}
