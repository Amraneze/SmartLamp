package fr.simona.smartlamp.intro;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import fr.simona.smartlamp.R;
import fr.simona.smartlamp.home.HomeActivity;

/**
 * Created by aaitzeouay on 31/08/2017.
 */

public class SplashScreenActivity extends AppCompatActivity {

    private static final String TAG = SplashScreenActivity.class.getSimpleName();
    private static final int TIME_VIEW_SPLASH = 3000;

    private boolean waitTimerFinish = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen_layout);
        ButterKnife.bind(this);
        startWaitTimer();
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
            finish();
        }
    }
}
