package fr.simona.smartlamp.intro;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.simona.smartlamp.R;
import fr.simona.smartlamp.home.HomeActivity;
import fr.simona.smartlamp.test.TestActivity;

/**
 * Created by aaitzeouay on 31/08/2017.
 */

public class SplashScreenActivity extends AppCompatActivity {

    private static final String TAG = SplashScreenActivity.class.getSimpleName();
    private static final int TIME_VIEW_SPLASH = 3000;

    private boolean waitTimerFinish = false;

    /**
     * See link https://github.com/Taishi-Y/InstagramLikeColorTransitionAndroid
     */
    @BindView(R.id.ll_animated_background)
    RelativeLayout llAnimatedBackground;
    private AnimationDrawable animationDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            findViewById(android.R.id.content).setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
        setContentView(R.layout.splash_screen_layout);
        ButterKnife.bind(this);

        animationDrawable = (AnimationDrawable) llAnimatedBackground.getBackground();
        animationDrawable.setEnterFadeDuration(250);
        animationDrawable.setExitFadeDuration(500);
        startWaitTimer();
    }

    // Starting animation:- start the animation on onResume.
    @Override
    protected void onResume() {
        super.onResume();
        if (animationDrawable != null && !animationDrawable.isRunning())
            animationDrawable.start();
    }

    // Stopping animation:- stop the animation on onPause.
    @Override
    protected void onPause() {
        super.onPause();
        if (animationDrawable != null && animationDrawable.isRunning())
            animationDrawable.stop();
    }

    private void startWaitTimer() {
        new CountDownTimer(TIME_VIEW_SPLASH, 1000) {
            public void onTick(long millisUntilFinished) {
                //do nothing
            }

            public void onFinish() {
                waitTimerFinish = true;
                tryGoToNextScreen();
            }
        }.start();
    }

    private void tryGoToNextScreen() {
        if (waitTimerFinish) {
            startActivity(new Intent(this, HomeActivity.class));
            //startActivity(new Intent(this, TestActivity.class));
            finish();
        }
    }
}
