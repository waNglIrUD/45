package ppg.com.yanlibrary.utils.imageloader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListAdapter;

import ppg.com.yanlibrary.utils.imageloader.utils.FileManager;

/**
 * @author jie.yang
 */
public class ImageLoader {

    final int REQUIRED_SIZE = 100;
    private MemoryCache memoryCache = new MemoryCache();
    private AbstractFileCache fileCache;
    private Map<ImageView, String> imageViews = Collections
            .synchronizedMap(new WeakHashMap<ImageView, String>());
    // 线程池
    private ExecutorService executorService;
    private boolean isFadeIn = true;
    private static ImageLoader mImageLoader=null;
    private volatile static int mScrollState;
    private final String DECODE_NAME = "decode_name";

    public File getFileForUrl(String url) {
        return fileCache.getFile(url);
    }

    public void reset(Context context) {
        clearCache();
        imageViews.clear();
//		executorService.shutdown();
    }

    public static ImageLoader createImageLoader(Context context) {
        if (mImageLoader == null){
            synchronized (ImageLoader.class){
                if(mImageLoader==null) {
                    mImageLoader = new ImageLoader(context);
                }
            }
        }
        return mImageLoader;
    }

    public static void release() {
        if (mImageLoader != null) {
            mImageLoader.clearMemoryCache();
            mImageLoader = null;
        }
    }

    private ImageLoader(Context context) {
        fileCache = new FileCache(context);
        executorService = Executors.newFixedThreadPool(5);
    }

    public void setBitmapFromMemory(String url, Bitmap bitmap) {
        memoryCache.put(url, bitmap);
    }

    public void configurationLoadAnimation(boolean isFadeIn) {
        this.isFadeIn = isFadeIn;
    }

    public void configurationFilePath(String path) {
        FileManager.FLODER_NAME_PATH = path;
    }

    public void displayImage(String url, ImageView imageView, int loadRid) {
//		imageView.setImageResource(loadRid);
        displayImage(url, loadRid, imageView, false, null, false, null);
    }

    public void displayImage(String url, ImageView imageView, int loadRid, onloadListener loadListener) {
//		imageView.setImageResource(loadRid);
        displayImage(url, loadRid, imageView, false, null, false, loadListener);
    }

    public void displayImage(String url, ImageView imageView, int loadRid,
                             boolean isDecodeBitmap) {
//		imageView.setImageResource(loadRid);
        displayImage(url, loadRid, imageView, false, null, isDecodeBitmap, null);
    }

    public void displayImage(String url, ImageView imageView, int loadRid,
                             boolean isListScrollStopLoad, boolean isDecodeBitmap) {
//		imageView.setImageResource(loadRid);
        displayImage(url, loadRid, imageView, isListScrollStopLoad, null, isDecodeBitmap,
                null);
    }

    public void displayImage(String url, ImageView imageView, int loadRid,
                             boolean isListScrollStopLoad, boolean isDecodeBitmap, onloadListener loadListener) {
//		imageView.setImageResource(loadRid);
        displayImage(url, loadRid, imageView, isListScrollStopLoad, null, isDecodeBitmap,
                loadListener);
    }

    public void displayImage(String url, ImageView imageView,
                             boolean isListScrollStopLoad) {
        displayImage(url, -1, imageView, isListScrollStopLoad, null, false, null);
    }

    public void displayImage(String url, ImageView imageView,
                             boolean isListScrollStopLoad, boolean isDecodeBitmap) {
        displayImage(url, -1, imageView, isListScrollStopLoad, null, isDecodeBitmap,
                null);
    }

    public void displayImage(String url, ImageView imageView,
                             boolean isListScrollStopLoad, obtainBitmapListener listener) {
        displayImage(url, -1, imageView, isListScrollStopLoad, listener, false, null);
    }

    public void displayImage(String url, ImageView imageView,
                             boolean isListScrollStopLoad, boolean isDecodeBitmap,
                             onloadListener loadListener) {
        displayImage(url, -1, imageView, isListScrollStopLoad, null, isDecodeBitmap,
                loadListener);
    }

    public Bitmap getBitmapFromMemory(String url, boolean isDecode) {
        if (isDecode)
            return memoryCache.get(url + DECODE_NAME);
        else
            return memoryCache.get(url);
    }

    // 最主要的方法
    public void displayImage(String url, int loadRid, ImageView imageView,
                             boolean isListScrollStopLoad, obtainBitmapListener listener,
                             boolean isDecode, onloadListener loadListener) {
        if (url == null || url.length() == 0 || "".equals(url.trim()))
            return;
        imageViews.put(imageView, url);
        String decodeUrl = isDecode ? url + DECODE_NAME : url;
        // 先从内存缓存中查找
        Bitmap bitmap = memoryCache.get(decodeUrl);
        if (bitmap != null) {
            if (listener != null)
                imageView.setImageBitmap(listener.getDisplayerBitmap(imageView,
                        bitmap));
            else
                imageView.setImageBitmap(bitmap);
            if (loadListener != null)
                loadListener.onloaded(imageView, bitmap);

        } else {
            if (loadRid != -1)
                imageView.setImageResource(loadRid);
            if (!isListScrollStopLoad)  // list 滚动停止才加载网络图片
                // 若没有的话则开启新线程加载图片
                queuePhoto(url, imageView, listener, isDecode, loadListener);
            else if (mScrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE)
                queuePhoto(url, imageView, listener, isDecode, loadListener);
        }
    }

    private void queuePhoto(String url, ImageView imageView,
                            obtainBitmapListener listener, boolean isDecode,
                            onloadListener loadListener) {
        PhotoToLoad p = new PhotoToLoad(url, imageView);
        executorService.submit(new PhotosLoader(p, listener, isDecode,
                loadListener));
//		new Thread(new PhotosLoader(p, listener, isDecode,
//				loadListener)).start();
    }

    public void loadBitmapFromFileToMemory(String url, boolean isDecode) {
        File f = fileCache.getFile(url);
        Bitmap b = null;
        if (f != null && f.exists()) {
            b = isDecode ? decodeFile(f) : noDecodeFile(f);
            memoryCache.put(url, b);
        }
    }

    private Bitmap getBitmap(String url, boolean isDecode) {
        String decodeUrl = isDecode ? url + DECODE_NAME : url;
        File f = fileCache.getFile(decodeUrl);

        // 先从文件缓存中查找是否有
        Bitmap b = null;
        if (f != null && f.exists()) {
//			b = isDecode ? decodeFile(f) : noDecodeFile(f);
            b = noDecodeFile(f);
        }
        if (b != null) {
//            try {Thread.sleep(300);} catch (InterruptedException e) {e.printStackTrace();}
            return b;
        }
        // 最后从指定的url中下载图片
        File noDecode = fileCache.getFile(url);

        HttpURLConnection conn;
        InputStream is = null;
        try {
            Bitmap bitmap = null;
            URL imageUrl = new URL(url);
            conn = (HttpURLConnection) imageUrl
                    .openConnection();
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(30000);
            conn.setInstanceFollowRedirects(true);
            is = conn.getInputStream();
            OutputStream os = new FileOutputStream(noDecode);
            if (!CopyStream(is, os)) {  // 不能成功写入图片的情况
                bitmap = BitmapFactory.decodeStream(is);
                os.close();
            } else {
                os.close();
                bitmap = isDecode ? decodeFile(noDecode) : noDecodeFile(noDecode);
                if (isDecode)
                    saveBitmap(fileCache.getFile(url + DECODE_NAME), bitmap);
            }
            return bitmap;
        } catch (Exception ex) {
            Log.e("", "getBitmap catch Exception...\nmessage = " + ex.getMessage());
            return getBitmapForException(is);
        }
    }

    private void saveBitmap(File f, Bitmap mBitmap) {
        try {
            f.createNewFile();
        } catch (IOException e) {
            Log.e("saveMyBitmap", "在保存图片时出错：" + e.toString());
        }
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
        try {
            fOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Bitmap getBitmapForException(InputStream is) {
        Bitmap bitmap = null;
        try {
//			URL imageUrl = new URL(url);
//			HttpURLConnection conn = (HttpURLConnection) imageUrl
//					.openConnection();
//			conn.setConnectTimeout(30000);
//			conn.setReadTimeout(30000);
//			conn.setInstanceFollowRedirects(true);
//			InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (Exception ex) {
            Log.e("",
                    "getBitmapForException...\nmessage = "
                            + ex.getMessage());
        }
        return bitmap;
    }

    private Bitmap noDecodeFile(File f) {
        try {
            return BitmapFactory.decodeStream(new FileInputStream(f));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    // decode这个图片并且按比例缩放以减少内存消耗，虚拟机对每张图片的缓存大小也是有限制的
    private Bitmap decodeFile(File f) {
        try {
            // decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);

            // Find the correct scale value. It should be the power of 2.
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while (true) {
                if (width_tmp / 2 < REQUIRED_SIZE
                        || height_tmp / 2 < REQUIRED_SIZE)
                    break;
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }

            // decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {
        }
        return null;
    }

    // Task for the queue
    private class PhotoToLoad {
        public String url;
        public ImageView imageView;

        public PhotoToLoad(String u, ImageView i) {
            url = u;
            imageView = i;
        }
    }

    class PhotosLoader implements Runnable {
        PhotoToLoad photoToLoad;
        obtainBitmapListener mListener;
        onloadListener loadListener;
        boolean isDecode;

        PhotosLoader(PhotoToLoad photoToLoad, obtainBitmapListener listener,
                     boolean isDecode, onloadListener loadListener) {
            this.photoToLoad = photoToLoad;
            mListener = listener;
            this.isDecode = isDecode;
            this.loadListener = loadListener;
        }

        @Override
        public void run() {
            if (imageViewReused(photoToLoad))
                return;
            Bitmap bmp = getBitmap(photoToLoad.url, isDecode);
            String decodeUrl = isDecode ? photoToLoad.url + DECODE_NAME : photoToLoad.url;
            memoryCache.put(decodeUrl, bmp);
            if (imageViewReused(photoToLoad))
                return;
            if (mListener != null)
                bmp = mListener.getDisplayerBitmap(photoToLoad.imageView, bmp);
            BitmapDisplayer bd = new BitmapDisplayer(bmp, photoToLoad,
                    loadListener);
            // 更新的操作放在UI线程中
            Activity a = (Activity) photoToLoad.imageView.getContext();
            a.runOnUiThread(bd);
        }
    }

    /**
     * 防止图片错位
     *
     * @param photoToLoad
     * @return
     */
    boolean imageViewReused(PhotoToLoad photoToLoad) {
        String tag = imageViews.get(photoToLoad.imageView);
        if (tag == null || !tag.equals(photoToLoad.url))
            return true;
        return false;
    }

    // 用于在UI线程中更新界面
    class BitmapDisplayer implements Runnable {
        Bitmap bitmap;
        PhotoToLoad photoToLoad;
        onloadListener listener;

        public BitmapDisplayer(Bitmap b, PhotoToLoad p, onloadListener listener) {
            bitmap = b;
            photoToLoad = p;
            this.listener = listener;
        }

        @Override
        public void run() {
            if (imageViewReused(photoToLoad))
                return;
            if (bitmap != null) {
                boolean isDispose = false;
                if (listener != null)
                    isDispose = listener.onloaded(photoToLoad.imageView, bitmap);
                if (!isDispose)
                    if (isFadeIn)
                        fadeInDisplay(photoToLoad.imageView, bitmap);
                    else
                        // photoToLoad.imageView.setImageDrawable(new
                        // BitmapDrawable(photoToLoad.imageView.getResources(),
                        // bitmap));
                        photoToLoad.imageView.setImageBitmap(bitmap);
            }
        }
    }

    @SuppressWarnings("deprecation")
    private void fadeInDisplay(View imageView, Bitmap bitmap) {
        final TransitionDrawable td = new TransitionDrawable(new Drawable[]{
                new ColorDrawable(android.R.color.transparent),
                new BitmapDrawable(imageView.getResources(), bitmap)});
        if (imageView instanceof ImageView) {
            ((ImageView) imageView).setImageDrawable(td);
        } else {
            imageView.setBackgroundDrawable(td);
        }
        td.startTransition(500);
    }

    public void configFilePath(FileManager.onConfigSaveFilePath callback) {
        FileManager.callBack = callback;
    }

    public void clearCache() {
        memoryCache.clear();
        fileCache.clear();
    }

    public void clearMemoryCache() {
        memoryCache.clear();
    }

    public void clearMemoryCache(String key) {
        memoryCache.clear(key);
    }

    public void clearFileCache() {
        fileCache.clear();
    }

    public static boolean CopyStream(InputStream is, OutputStream os) {
        final int buffer_size = 1024;
        try {
            byte[] bytes = new byte[buffer_size];
            for (; ; ) {
                int count = is.read(bytes, 0, buffer_size);
                if (count == -1)
                    break;
                os.write(bytes, 0, count);
            }
            return true;
        } catch (Exception ex) {
            Log.e("", "CopyStream catch Exception...");
            return false;
        }
    }

    public static interface obtainBitmapListener {
        public Bitmap getDisplayerBitmap(ImageView imageView, Bitmap bitmap);
    }

    public static interface onloadListener {
        public boolean onloaded(ImageView imageView, Bitmap bitmap);
    }


    public ListLoadImageOnScrollListener getListLoadImageOnScrollListener() {
        return new ListLoadImageOnScrollListener();
    }

//    public static interface LoadImageOnScrollStop {
//        public void onScrollStop();
//    }

    public class ListLoadImageOnScrollListener implements AbsListView.OnScrollListener {

        private AbsListView.OnScrollListener onScrollListener;
        private LoadImageOnScrollStop imageOnScrollStop;
//        private int mFirstVisibleItem = -1;
//        private int mLastFirstVisibleItem = -1;

        public ListLoadImageOnScrollListener() {
        }

        public AbsListView.OnScrollListener setLoadImageOnScrollListener(
                AbsListView.OnScrollListener onScrollListener, LoadImageOnScrollStop imageOnScrollStop) {
            this.onScrollListener = onScrollListener;
            this.imageOnScrollStop = imageOnScrollStop;
            return this;
        }

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            if (onScrollListener != null)
                onScrollListener.onScrollStateChanged(view, scrollState);
            mScrollState = scrollState;
            if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                if (imageOnScrollStop != null /*&& mFirstVisibleItem != mLastFirstVisibleItem*/) {
                    imageOnScrollStop.onScrollStop();
//                    mLastFirstVisibleItem = mFirstVisibleItem;
//                    ToastUtil.toast2_bottom(view.getContext(), "!!!");
                }
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if (onScrollListener != null)
                onScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
//            mFirstVisibleItem = firstVisibleItem;
        }
    }
}