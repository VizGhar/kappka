package xyz.kandrac.kappka.utils;

import android.content.Context;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;

/**
 * Created by jan on 9.2.2017.
 */

public class DisplayUtils {

    @SuppressWarnings("deprecation")
    public static int getColor(@NonNull Context context, @ColorRes int color) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return context.getResources().getColor(color);
        } else {
            return context.getColor(color);
        }
    }
}
