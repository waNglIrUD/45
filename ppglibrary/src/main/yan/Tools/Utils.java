package Tools;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.WindowManager;

/**
 * Created by ppg on 2016/1/22.
 */
public class Utils {

    /**
     * 设置添加屏幕的背景透明度
     * @param bgAlpha
     */
    public static void backgroundAlpha(Activity mContext,float bgAlpha)
    {
        WindowManager.LayoutParams lp = mContext.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        mContext.getWindow().setAttributes(lp);
    }

    //获取res内的图片的rid
    public static int getResId(String name,Activity activity) {
        ApplicationInfo appInfo = activity.getApplicationInfo();
        int resID = activity.getResources().getIdentifier(name, "mipmap", appInfo.packageName);
        return resID;
    }

    /**
     * 转换图片成圆形
     * @param bitmap 传入Bitmap对象
     * @return
     */
    public static Bitmap toRoundBitmap(Bitmap bitmap)
    {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float roundPx;
        float left,top,right,bottom,dst_left,dst_top,dst_right,dst_bottom;
        if (width <= height) {
            roundPx = width / 2 -5;
            top = 0;
            bottom = width;
            left = 0;
            right = width;
            height = width;
            dst_left = 0;
            dst_top = 0;
            dst_right = width;
            dst_bottom = width;
        } else {
            roundPx = height / 2 -5;
            float clip = (width - height) / 2;
            left = clip;
            right = width - clip;
            top = 0;
            bottom = height;
            width = height;
            dst_left = 0;
            dst_top = 0;
            dst_right = height;
            dst_bottom = height;
        }

        Bitmap output = Bitmap.createBitmap(width,
                height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect src = new Rect((int)left, (int)top, (int)right, (int)bottom);
        final Rect dst = new Rect((int)dst_left, (int)dst_top, (int)dst_right, (int)dst_bottom);
        final RectF rectF = new RectF(dst_left+15, dst_top+15, dst_right-20, dst_bottom-20);

        paint.setAntiAlias(true);

        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);

        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, src, dst, paint);
        return output;
    }

    /**
     * 把图片裁剪成圆形
     */
    public static Bitmap toRoundBit(Bitmap  bitmap) {
        int width =bitmap.getWidth();
        int height = bitmap.getHeight();
        float roundPx;
        float left,top,right,bottom,dst_left,dst_top,dst_right,dst_bottom;
        if(width<=height){
            roundPx=width/2;
            top=0;
            bottom=width;
            left=0;
            right=width;
            height=width;
            dst_left=0;
            dst_top=0;
            dst_right=width;
            dst_bottom=width;
        }else{
            roundPx=height/2;
            float clip=(width-height)/2;
            left=clip;
            right=width-clip;
            top=0;
            bottom=height;
            width=height;
            dst_left=0;
            dst_top=0;
            dst_right=height;
            dst_bottom=height;
        }
        Bitmap	outPut=Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_8888);
        Canvas canvas=new Canvas(outPut);
        final int color=0xff424242;
        final Paint paint =new Paint();
        final Rect src= new Rect((int)left,(int)top,(int)right,(int)bottom);
        final Rect dst= new Rect((int)dst_left,(int)dst_top,(int)dst_right,(int)dst_bottom);
        final RectF rectF = new RectF(dst);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, src, dst,paint);
        return outPut;
    }
}
