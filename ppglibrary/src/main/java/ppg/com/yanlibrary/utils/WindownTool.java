package ppg.com.yanlibrary.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by yy on 2015/7/28.
 */
public class WindownTool {
    private int mScreenWidth;
    private int mScreenHeight;

    public WindownTool(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        mScreenWidth = outMetrics.widthPixels;
        mScreenHeight = outMetrics.heightPixels;
    }

    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    public static int getScreenLocationLeft(Context context, View v) {
        int[] location = new int[2];
        v.getLocationOnScreen(location);
        int x = location[0];

        return x;
    }

    public static int getScreenLocationTop(Context context, View v) {
        int[] location = new int[2];
        v.getLocationOnScreen(location);
        int y = location[1];
        return y;
    }

    public int getmScreenHeight() {
        return mScreenHeight;
    }

    public int getmScreenWidth() {
        return mScreenWidth;
    }
}
