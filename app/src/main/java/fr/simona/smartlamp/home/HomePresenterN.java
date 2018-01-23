package fr.simona.smartlamp.home;

import android.content.Context;
import android.util.Log;

import fr.simona.smartlamp.common.BasePresenter;
import fr.simona.smartlamp.common.utils.CommonUtils;

/**
 * Created by Amrane Ait Zeouay on 29-Nov-17.
 */

public class HomePresenterN extends BasePresenter<HomeView> {

    public HomePresenterN(Context context) {
        super(context);
        if (!CommonUtils.isIntroScreenSeen(context)) {
            Log.e("Amrane", "From here");
            view.displayIntroScreen();
        }
    }
}
