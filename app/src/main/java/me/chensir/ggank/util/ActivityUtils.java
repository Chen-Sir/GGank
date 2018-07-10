package me.chensir.ggank.util;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;

public class ActivityUtils {

    private ActivityUtils() {

    }

    public static void startTransitionAnimationActivity(Activity activity, Intent intent, View sharedElement, String sharedElementName) {

        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                activity, sharedElement, sharedElementName);
        try {
            ActivityCompat.startActivity(activity, intent, optionsCompat.toBundle());
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            activity.startActivity(intent);
        }

    }


}
