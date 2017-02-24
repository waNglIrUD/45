package ppg.com.yanlibrary.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;


public class ToastUtil {
	
	private static int backColor = 0x667BEDD2;

	/**中间提示
	 * @param context
	 * @param str
	 */
	public static void toast1_center(Context context,String str){
		if(context==null){
			System.out.println("context isnull, toast ="+str);
			return ;
		}
		Toast ts = Toast.makeText(context, str, 3000);
		ts.setGravity(Gravity.CENTER, 0, 0);
//		LinearLayout ll = new LinearLayout(context);
//		ll.setGravity(Gravity.CENTER);
//		TextView te = new TextView(context);
////		te.setBackgroundResource(drawable.topbackground);
//		te.setTextColor(Color.BLACK);
//		te.setPadding(10, 10, 10, 10);
//		te.setBackgroundColor(backColor);
//		LinearLayout.LayoutParams llip = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, 80);
//		te.setText(str);
//		te.setGravity(Gravity.CENTER);
//		ll.addView(te,llip);
//		ts.setView(ll);
		ts.show();
	}
	
	/**底部提示
	 * @param context
	 * @param str
	 */
	public static void toast2_bottom(Context context,String str){
		if(context==null){
			System.out.println("context isnull, toast ="+str);
			return ;
		}
		Toast ts = Toast.makeText(context, str, 3000);
//		LinearLayout ll = new LinearLayout(context);
//		ll.setGravity(Gravity.CENTER);
//		TextView te = new TextView(context);
////		te.setBackgroundResource(drawable.topbackground);
//		te.setTextColor(Color.BLACK);
//		te.setPadding(10, 10, 10, 10);
//		te.setBackgroundColor(backColor );
//		LinearLayout.LayoutParams llip = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, 80);
//		te.setText(str);
//		te.setGravity(Gravity.CENTER);
//		ll.addView(te,llip);
//		ts.setView(ll);
		ts.show();
	}
	
	public static void toast2_bottom(Context context,String str, int time){
		if(context==null){
			System.out.println("context isnull, toast ="+str);
			return ;
		}
		Toast ts = Toast.makeText(context, str, time);
//		LinearLayout ll = new LinearLayout(context);
//		ll.setGravity(Gravity.CENTER);
//		TextView te = new TextView(context);
////		te.setBackgroundResource(drawable.topbackground);
//		te.setTextColor(Color.BLACK);
//		te.setPadding(10, 10, 10, 10);
//		te.setBackgroundColor(backColor );
//		LinearLayout.LayoutParams llip = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, 80);
//		te.setText(str);
//		te.setGravity(Gravity.CENTER);
//		ll.addView(te,llip);
//		ts.setView(ll);
		ts.show();
	}
}