package ppg.com.yanlibrary.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.URLUtil;

public class ToolsUtil {

	public static final String TAG = ToolsUtil.class.getName();

	/**
	 * 比较当前版本与服务器版本是否一至
	 * 
	 * @param asv
	 * @return 如果一至返回true 否者返回false
	 */
	public static boolean comparseVersion(Context context, String rasvCode) {
		if (!AvailabelData.aString(rasvCode))
			return true;
		String v1 = rasvCode;
		String v2 = ToolsUtil.getSystemCurrVersion(context);
		return new BigDecimal(v2).doubleValue() >= new BigDecimal(v1)
				.doubleValue();
	}

	public static String getFilenameByURL(String URL) {
		if (URL == null)
			return null;
		String[] URLArray = URL.split("/");
		return URLArray[URLArray.length - 1];
	}

	public static String getImgFiletypeByAllName(String allFilename) {
		if (allFilename == null)
			return null;
		String[] allFilenameArray = allFilename.split("\\.");
		return allFilenameArray[allFilenameArray.length - 1];
	}

	public static Bitmap.CompressFormat getBitmapTypeByTypedesc(String typedesc) {
		if (typedesc.toLowerCase().equals("jpg")) {
			return Bitmap.CompressFormat.JPEG;
		} else if (typedesc.toLowerCase().equals("png")) {
			return Bitmap.CompressFormat.PNG;
		}
		// else if(typedesc.equals("webp"))
		// {
		// return Bitmap.CompressFormat.WEBP;
		// }
		return null;
	}

	public static Bitmap.CompressFormat getBitmapTypeByFilename(String fileName) {
		return getBitmapTypeByTypedesc(getImgFiletypeByAllName(fileName));
	}

	public static String getObjNameByClazzName(String clazzName) {
		if (AvailabelData.aString(clazzName)) {
			String[] strArray = clazzName.split("\\.");
			return strArray[strArray.length - 1];
		}
		return null;
	}

	public static ByteArrayOutputStream parse(InputStream in) throws Exception {
		ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
		int ch;
		while ((ch = in.read()) != -1) {
			swapStream.write(ch);
		}
		return swapStream;
	}

	public static ByteArrayInputStream parse(OutputStream out) throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		baos = (ByteArrayOutputStream) out;
		ByteArrayInputStream swapStream = new ByteArrayInputStream(
				baos.toByteArray());
		return swapStream;
	}

	public static String parse_String(InputStream in) throws Exception {
		ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
		int ch;
		while ((ch = in.read()) != -1) {
			swapStream.write(ch);
		}
		return swapStream.toString();
	}

	public static String parse_String(OutputStream out) throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		baos = (ByteArrayOutputStream) out;
		ByteArrayInputStream swapStream = new ByteArrayInputStream(
				baos.toByteArray());
		return swapStream.toString();
	}

	public static ByteArrayInputStream parse_inputStream(String in)
			throws Exception {
		ByteArrayInputStream input = new ByteArrayInputStream(in.getBytes());
		return input;
	}

	public static ByteArrayOutputStream parse_outputStream(String in)
			throws Exception {
		return parse(parse_inputStream(in));
	}

	public static boolean isPhoneNumberValid(String phoneNumber) {
		boolean isValid = false;
		/*
		 * 可接受的电话格式有：
		 */
		String expression = "^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{5})$";
		/*
		 * 可接受的电话格式有：
		 */
		String expression2 = "^\\(?(\\d{3})\\)?[- ]?(\\d{4})[- ]?(\\d{4})$";
		CharSequence inputStr = phoneNumber;
		Pattern pattern = Pattern.compile(expression);
		Matcher matcher = pattern.matcher(inputStr);

		Pattern pattern2 = Pattern.compile(expression2);
		Matcher matcher2 = pattern2.matcher(inputStr);
		if (matcher.matches() || matcher2.matches()) {
			isValid = true;
		}
		return isValid;
	}

	/**
	 * 获取手机屏目宽度
	 * 
	 * @param context
	 * @return int
	 */
	public static int getPhoneViewWidth(Context context) {
		DisplayMetrics metric = new DisplayMetrics();
		WindowManager windowMananger = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		windowMananger.getDefaultDisplay().getMetrics(metric);
		int width = metric.widthPixels; // 屏幕宽度（像素）
		return width;
	}

	public static int getPhoneViewHeight(Context context) {
		DisplayMetrics metric = new DisplayMetrics();
		WindowManager windowMananger = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		windowMananger.getDefaultDisplay().getMetrics(metric);
		int width = metric.heightPixels; // 屏幕宽度（像素）
		return width;
	}

	public static String getSystemCurrVersion(Context context) {
		// 获取packagemanager的实例
		PackageManager packageManager = context.getPackageManager();
		// getPackageName()是你当前类的包名，0代表是获取版本信息
		PackageInfo packInfo;
		try {
			packInfo = packageManager.getPackageInfo(context.getPackageName(),
					0);
			String version = packInfo.versionName;
			return version;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			Log.i(TAG, "获取应用版本信息发生异常!");
			return null;
		}
	}

	/**
	 * 调用手机通话
	 * 
	 * @param context
	 * @param tel
	 */
	public static void telCall(Context context, String tel) {
		if (tel == null)
			return;
		tel = "tel:" + tel;
		Intent phoneIntent = new Intent("android.intent.action.CALL",
				Uri.parse(tel));
		context.startActivity(phoneIntent);
	}

	/**
	 * 打开浏览器
	 * 
	 * @param context
	 * @param url
	 * @return boolean
	 */
	public static boolean openNet(Context context, String url) {
		if (url == null)
			return false;
		if (URLUtil.isNetworkUrl(url)) {
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
			context.startActivity(intent);
			return true;
		}
		return false;
	}

	public static void openFile(Context context, File file) {
		// TODO Auto-generated method stub
		Log.d("OpenFile", file.getName());
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(file),
				"application/vnd.android.package-archive");
		context.startActivity(intent);
	}

	public static class Math {
		public static String getSSWR(double num, int size) {
			if (size == 0)
				size = 2;
			NumberFormat format = NumberFormat.getNumberInstance();
			format.setMaximumFractionDigits(size);
			return format.format(num);
		}
	}

	/**
	 * 有效数据判断工具
	 * 
	 * @author java1009
	 * 
	 */
	public static class AvailabelData {
		public static boolean aCollection(Object val) {
			return aCollection(val, -9999, false);
		}

		public static boolean aCollection(Object val, int availableVal,
				boolean isThan) {
			if (val == null)
				return false;
			if (val instanceof Object[])
				return availableVal == -9999 ? ((Object[]) val).length > 0
						: isThan ? ((Object[]) val).length > availableVal
								: ((Object[]) val).length >= availableVal;
			if (val instanceof Long[])
				return availableVal == -9999 ? ((Long[]) val).length > 0
						: isThan ? ((Long[]) val).length > availableVal
								: ((Long[]) val).length >= availableVal;
			if (val instanceof List)
				return availableVal == -9999 ? ((List) val).size() > 0
						: isThan ? ((List) val).size() > availableVal
								: ((List) val).size() >= availableVal;
			if (val instanceof Map)
				return availableVal == -9999 ? ((Map) val).size() > 0
						: isThan ? ((Map) val).size() > availableVal
								: ((Map) val).size() >= availableVal;
			return false;
		}

		public static boolean aString(String str) {
			if (str == null)
				return false;
			if (str.equals(""))
				return false;
			return true;
		}

		public static boolean asIntent(Intent i, String key) {
			if (i != null) {
				if (i.hasExtra(key)) {
					return true;
				}
			}
			return false;
		}
	}

	/**
	 * 应用信息
	 * 
	 * @author java1009
	 * 
	 */
	public static class AppInfo {
		/**
		 * 获取当前应用的版本号
		 * 
		 * @param context
		 * @return
		 */
		public static String getSystemCurrVersion(Context context) {
			// 获取packagemanager的实例
			PackageManager packageManager = context.getPackageManager();
			// getPackageName()是你当前类的包名，0代表是获取版本信息
			PackageInfo packInfo;
			try {
				packInfo = packageManager.getPackageInfo(
						context.getPackageName(), 0);
				String version = packInfo.versionName;
				return version;
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				Log.i(TAG, "获取应用版本信息发生异常!");
				return null;
			}
		}
	}

	/**
	 * 键盘控制
	 * 
	 * @author java1009
	 */
	public static class Keyboard {
		public static final int DEL_CODE = 67;

		public static final int ENTER_CODE = 66;

		public static void toglleSoftInput(Context context) {
			InputMethodManager imm = (InputMethodManager) context
					.getSystemService(Activity.INPUT_METHOD_SERVICE);
			// 打开（自动控制的再次点击按钮就会消失的）
			imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	/**
	 * 手机信息工具
	 * 
	 * @author java1009
	 * 
	 */
	public static class PhoneInfo {
		/**
		 * 获取手机屏目宽度
		 * 
		 * @param context
		 * @return int
		 */
		public static int getPhoneViewWidth(Context context) {
			DisplayMetrics metric = new DisplayMetrics();
			WindowManager windowMananger = (WindowManager) context
					.getSystemService(Context.WINDOW_SERVICE);
			windowMananger.getDefaultDisplay().getMetrics(metric);
			int width = metric.widthPixels; // 屏幕宽度（像素）
			return width;
		}

		/**
		 * 获取手机的屏目信息
		 * 
		 * @param wm
		 * @return
		 */
		public static DisplayMetrics getDisplayMetrics(WindowManager wm) {
			if (wm == null)
				return null;
			DisplayMetrics dm = new DisplayMetrics();
			wm.getDefaultDisplay().getMetrics(dm);
			return dm;
		}

		/**
		 * 得到分辨率
		 * 
		 * @param dm
		 * @return
		 */
		public static String getPixels(DisplayMetrics dm) {
			if (dm == null)
				return null;
			return dm.widthPixels + "," + dm.heightPixels;
		}
		/**
		 * 获取当前手机密度
		 * @param activity
		 * @return
		 */
		public static int getDensity(Activity activity)
		{
			DisplayMetrics metric = new DisplayMetrics();
			activity.getWindowManager().getDefaultDisplay().getMetrics(metric);
	        int width = metric.widthPixels;  // 屏幕宽度（像素）
	        int height = metric.heightPixels;  // 屏幕高度（像素）
	        float density = metric.density;  // 屏幕密度（0.75 / 1.0 / 1.5）
	        int densityDpi = metric.densityDpi;  // 屏幕密度DPI（120 / 160 / 240）
	        return densityDpi;
		}
	}

}
