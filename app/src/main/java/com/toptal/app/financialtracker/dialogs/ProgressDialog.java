package com.toptal.app.financialtracker.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.toptal.app.financialtracker.R;


/**
 * This class represents the full size progress dialog.
 */
public class ProgressDialog extends Dialog {

    private ImageView mLoadingIconIv;

    /**
     * Constructor method.
     * @param ownerActivity - The owner activity.
     */
    public ProgressDialog(Activity ownerActivity) {
        super(ownerActivity, R.style.AppTheme_FullSizeDialog);
        setCancelable(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_progress);

        mLoadingIconIv = (ImageView) findViewById(R.id.loading_icon_iv);

        RotateAnimation anim = new RotateAnimation(-180, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setInterpolator(new LinearInterpolator());
        anim.setRepeatCount(Animation.INFINITE);
        anim.setDuration(1000);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                mLoadingIconIv.startAnimation(animation);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
        mLoadingIconIv.startAnimation(anim);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        dismiss();
    }
}
