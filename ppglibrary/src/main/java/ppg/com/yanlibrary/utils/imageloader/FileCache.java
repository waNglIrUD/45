package ppg.com.yanlibrary.utils.imageloader;


import android.content.Context;

import ppg.com.yanlibrary.utils.imageloader.utils.FileManager;

public class FileCache extends AbstractFileCache{

	public FileCache(Context context) {
		super(context);	
	}


	@Override
	public String getSavePath(String url) {
		String filename = String.valueOf(url.hashCode());
//		String filename = Encryption.md5(url);
		return getCacheDir() + filename+".jpg";
	}

	@Override
	public String getCacheDir() {
		return FileManager.getSaveFilePath();
	}

}
