package ppg.com.yanlibrary.utils.json;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author jie.yang
 *
 * @param <T>
 */
public class JsonArrayBean<T extends JsonArrayBean.ItemData> extends JsonBaseBean {
	
	private List<T> mDatas = new ArrayList<T>();
//	private ItemDataFactory dataFactory;
	
	public JsonArrayBean() {
		super(DATA_TYPE_ARRAY);
	}
	
	public List<T> getDatas() {
		return mDatas;
	}
	
	@Override
	protected void onPaddingDataArray(JSONArray array) {
		int size = array.length();
		for(int i=0;i<size;++i) {
			T t = createItem();
			t.paddingData(array, i);
			mDatas.add(t);
		}
	}
	
	protected T createItem() {
		return (T) new ItemData();
	}
	
//	public static interface ItemDataFactory {
//		public <T extends JsonArrayBean.ItemData> T create();
//	}
	
	public static class ItemData {
		public void paddingData(JSONArray array, int pos) {}
	}
}
