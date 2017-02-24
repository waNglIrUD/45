package ppg.com.yanlibrary.utils;

import android.content.Context;

/**
 * px dip 转换工具
 * @author jie.yang
 *
 */
public class DensityUtil {    

    public static int dip2px(Context context, float dipValue) {  

            //得到手机屏幕的密度 px = (density/160)dp
        final float scale = context.getResources().getDisplayMetrics().density;  

        return (int) (dipValue * scale + 0.5f);  

    }  

    public static int px2dip(Context context, float pxValue) {  

        final float scale = context.getResources().getDisplayMetrics().density;  

        return (int) (pxValue / scale + 0.5f);  

    }  

} 