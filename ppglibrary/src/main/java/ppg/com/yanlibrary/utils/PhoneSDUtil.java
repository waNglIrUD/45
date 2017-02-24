package ppg.com.yanlibrary.utils;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

/**
 * 电话ＳＤ卡操作，相交于APPSDUtil而言，它更底层一些
 * 
 * @author java1009
 * 
 */
public class PhoneSDUtil {

	private static final String INIT_EXCEPTION_MSG = "SD卡未安装";

	private static final String NOTINIT_EXCEPTION_MSG = "请先执行init()或方法";

	private static final String REMOVECATCH_MSG = "清定缓存中的文件";

	public static final String WHOLESALE_JPG = "jpg";

	public static final String WHOLESALE_LOG = "txt";

	private StatFs statfs;

	private String sdpath;

	private static PhoneSDUtil instance;

	public static PhoneSDUtil getInstance() {
		if (instance == null)
			instance = new PhoneSDUtil();
		return instance;
	}

	public PhoneSDUtil() {
		File path = Environment.getExternalStorageDirectory();
		String temp = Environment.getExternalStorageDirectory().toString();
		setSDpath(path.getPath());
		statfs = new StatFs(path.getPath());
	}
	
	/**
	 * 获取缓存大小
	 * @param dirPath	缓存路径
	 * @param suffix　  缓存文件后缀　
	 * @return
	 */
	public int getCache(String dirPath, String... suffix) {
		File dir = new File(dirPath);
		File[] files = dir.listFiles();
		if (files == null) {
			return 0;
		}
		SuffixJudge suffixjudge = new SuffixJudge(suffix);

		int dirSize = 0;
		for (int i = 0; i < files.length; i++) {
			if (suffixjudge.doJudge(files[i].getName())){
					dirSize += files[i].length();
			}
		}
		return dirSize;
	}

	public StatFs getStateFs() {
		return statfs;
	}

	public String getSDpath() {
		return sdpath;
	}

	public void setSDpath(String path) {
		sdpath = path;
	}

	/**
	 * 获取ＳＤ卡已用情况
	 * 
	 * @return long
	 */
	public long getAvailaleSize() {

		long blockSize = statfs.getBlockSize();

		long availableBlocks = statfs.getAvailableBlocks();

		return availableBlocks * blockSize;

		// (availableBlocks * blockSize)/1024 KIB 单位

		// (availableBlocks * blockSize)/1024 /1024 MIB单位
	}

	public long getAvailaleSizeKB() throws IllegalAccessException {
		return getAvailaleSize() / 1024;
	}

	public long getAvailaleSizeMB() {
		return getAvailaleSize() / 1024 / 1024;
	}

	/**
	 * 获取ＳＤ卡的总容量
	 * 
	 * @return long
	 */
	public long getAllSize() {

		long blockSize = statfs.getBlockSize();

		long availableBlocks = statfs.getBlockCount();

		return availableBlocks * blockSize;

	}

	public long getAllSizeKB() throws IllegalAccessException {
		return getAllSize() / 1024;
	}

	public long getAllSizeMB() throws IllegalAccessException {
		return getAllSize() / 1024 / 1024;
	}

	/**
	 * SD卡准备就绪
	 * 
	 * @return Boolean
	 */
	public static Boolean SDReadySuccess() {
		return Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
	}

	/** * 修改文件的最后修改时间 * @param dir * @param fileName */
	private void updateFileTime(String dir, String fileName) {
		File file = new File(dir, fileName);
		long newModifiedTime = System.currentTimeMillis();
		file.setLastModified(newModifiedTime);
	}
	
	/**
	 * 删除缓存文件
	 * @param dirPath		缓存路径
	 * @param cacheSize	缓存大小,超过该值便执行删除
	 * @param suffix			缓存文件后缀,可选参数，如果未填，所有dirpath下所有文件都是缓存文件
	 */
	public void removeCache(String dirPath, long cacheSize,String...suffix) {
		if(dirPath == null)return;
		File dir = new File(dirPath);
		File[] files = dir.listFiles();
		if (files == null) {
			return;
		}
		SuffixJudge sj = new SuffixJudge(suffix);
		int dirSize = getCache(dirPath, suffix);		
		// 占用空间
		if (dirSize > cacheSize) {
			int removeFactor;
			if(cacheSize == 0)
				removeFactor = files.length;
			else
				removeFactor = (int) ((0.4 * files.length) + 1);
			Arrays.sort(files, new FileLastModifSort());
			Log.i("removeCache", REMOVECATCH_MSG);
			for (int i = 0; i < removeFactor; i++) {
				if (sj.doJudge(files[i].getName())){
					files[i].delete();
				}
			}
		}
	}
}

/** * TODO 根据文件的最后修改时间进行排序 * */
class FileLastModifSort implements Comparator<File> {
	public int compare(File arg0, File arg1) {
		if (arg0.lastModified() > arg1.lastModified()) {
			return 1;
		} else if (arg0.lastModified() == arg1.lastModified()) {
			return 0;
		} else {
			return -1;
		}
	}
}
class SuffixJudge{
	private String[] suffixs;
	
	private Pattern p;
	
	public SuffixJudge(String...suffixs)
	{
		this.suffixs = suffixs;
		if(suffixs!=null)
		{
			String reg = null;
			if (suffixs.length > 0) {
				for (int i = 0; i < suffixs.length; i++) {
					if (i == 0)
						reg = suffixs[i];
					else
						reg = reg + "|" + suffixs[i];
				}
				p = Pattern.compile(reg);
			}			
		}
	}
	public boolean doJudge(String fileName)
	{
		if(p==null)return true;
		Matcher m = p.matcher(fileName);
		boolean result = m.find();
		return result;
		
	}
}