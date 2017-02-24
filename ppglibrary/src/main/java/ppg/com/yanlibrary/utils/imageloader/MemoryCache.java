package ppg.com.yanlibrary.utils.imageloader;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import android.graphics.Bitmap;
import android.util.Log;

public class MemoryCache {

	private static final String TAG = "MemoryCache";
	// 放入缓存时是个同步操作
	// LinkedHashMap构造方法的最后一个参数true代表这个map里的元素将按照最近使用次数由少到多排列，即LRU 这就是所谓的LRU算法（Least Recently Used）：
	// 最近最少使用算法。
	// 这样的好处是如果要将缓存中的元素替换，则先遍历出最近最少使用的元素来替换以提高效率
	//Collections.synchronizedMap 每个方法都是synchronized 方法
	//加载因子是已存和的数据容量与总容量的比率，采用小数表示，默认为 0.75，即表示当 Map 中的数据量达到总容量的 75% 时，其容量空间自动扩张至原容量的一倍。
//	可能类似于Hashtable中的loadFactor
//
//	Hashtable类中包含一个私有成员变量loadFactor，它指定了哈希表中元素个数与表位置总数之间的最大比例。例如：
//	loadFactor等于0.5，则说明哈希表中只有一半的空间存放了元素值，其余一半皆为空。
//
//	哈希表的构造函数以重载的方式，允许用户指定loadFactor值，定义范围为0.1到1.0。要注意的是，不管你提供的值是多少，
//	范围都不超过72%。即使你传递的值为1.0，Hashtable类的loadFactor值还是0.72。微软认为loadFactor的最佳值为0.72，
//	因此虽然默认的loadFactor为1.0，但系统内部却自动地将其改变为0.72。所以，建议你使用缺省值1.0
	private Map<String, Bitmap> cache = Collections
			.synchronizedMap(new LinkedHashMap<String, Bitmap>(20, 1.5f, true));
	// 缓存中图片所占用的字节，初始0，将通过此变量严格控制缓存所占用的堆内存
	private volatile long size = 0;// current allocated size
	// 缓存只能占用的最大堆内存
	private long limit = 1000000;// max memory in bytes

	public MemoryCache() {
		// use 25% of available heap size
		setLimit(Runtime.getRuntime().maxMemory() / 8);

	}

	public void setLimit(long new_limit) {
		limit = new_limit;
		Log.i(TAG, "MemoryCache will use up to " + limit / 1024. / 1024. + "MB");
	}

	public Bitmap get(String id) {
//		synchronized (this) {
		try {
			if (!cache.containsKey(id))
				return null;
			return cache.get(id);
		} catch (NullPointerException ex) {
			return null;
		}
//		}
	}

	public void put(String id, Bitmap bitmap) {
//		synchronized (this) {
		try {
			if (cache.containsKey(id))
				size -= getSizeInBytes(cache.get(id));
			cache.put(id, bitmap);
			size += getSizeInBytes(bitmap);
			checkSize();
		} catch (Throwable th) {
			th.printStackTrace();
		}
//		}
	}

	/**
	 * 严格控制堆内存，如果超过将首先替换最近最少使用的那个图片缓存
	 *
	 */
	private void checkSize() {
		Log.i(TAG, "cache size=" + size + " length=" + cache.size());
		if (size > limit) {
			int skip = (int) (limit / 3 * 2);
			// 先遍历最近最少使用的元素
			Iterator<Entry<String, Bitmap>> iter = cache.entrySet().iterator();
			while (iter.hasNext()) {
				Entry<String, Bitmap> entry = iter.next();
				size -= getSizeInBytes(entry.getValue());
//				entry.getValue().recycle();
				iter.remove();
				if (size <= skip)
					break;
			}
			Log.i(TAG, "cache size=" + size + " length=" + cache.size());
		}
	}

	public void clear() {
		Iterator<Entry<String, Bitmap>> iter = cache.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<String, Bitmap> entry = iter.next();
			size -= getSizeInBytes(entry.getValue());
			iter.remove();
		}
		Runtime.getRuntime().gc();
		System.gc();
	}

	public void clearAndRecycle() {
		Iterator<Entry<String, Bitmap>> iter = cache.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<String, Bitmap> entry = iter.next();
			size -= getSizeInBytes(entry.getValue());
			Bitmap bitmap = entry.getValue();
			if(bitmap != null)
				bitmap.recycle();
		}
		cache.clear();
		Runtime.getRuntime().gc();
		System.gc();
	}

	public void clear(String key) {
		Bitmap bitmap = cache.get(key);
		if(bitmap != null) {
			size -= getSizeInBytes(bitmap);
			cache.remove(key);
			bitmap = null;
		}
		cache.remove(key);
		Runtime.getRuntime().gc();
		System.gc();
	}

	public void clearAndRecycle(String key) {
		Bitmap bitmap = cache.get(key);
		if(bitmap != null) {
			size -= getSizeInBytes(bitmap);
			cache.remove(key);
			bitmap.recycle();
			bitmap = null;
			Runtime.getRuntime().gc();
			System.gc();
		}
		cache.remove(key);
	}

	/**
	 * 图片占用的内存
	 *
	 * [url=home.php?mod=space&uid=2768922]@Param[/url] bitmap
	 *
	 * @return
	 */
	long getSizeInBytes(Bitmap bitmap) {
		if (bitmap == null)
			return 0;
		return bitmap.getRowBytes() * bitmap.getHeight();
	}
}
