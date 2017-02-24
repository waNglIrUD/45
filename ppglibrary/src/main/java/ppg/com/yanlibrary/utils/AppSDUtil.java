package ppg.com.yanlibrary.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.util.Log;

import ppg.com.yanlibrary.GlobalVar;

/**
 * 应用ＳＤ卡操作
 * @author java1009
 *
 */
public class AppSDUtil {
	
	public static final String TAG = AppSDUtil.class.getName();
	
	//日志文件路径（项目中需要替换）
	public static String logPath = GlobalVar.APP_LOG_FLODER;
	//图片文件根路径（项目中需要替换）
	public static String imageRootPath = GlobalVar.APP_IMAGE_FLODER;
	
	public static String apkRootPath = GlobalVar.APP_FLODER;
	/**
	 * 在SD卡中创建一个普通目录
	 * @param folderPathName
	 * @return String
	 */
	public static String createFolder(String folderPathName,boolean isNomedia) {
		try
		{
			PhoneSDUtil phoneSDUtil = PhoneSDUtil.getInstance();
			// 得到SD卡路径			
			String sdpath = phoneSDUtil.getSDpath();
			String folderPath = sdpath + "\\" + folderPathName;
			//统一分隔符
			folderPath = folderPath.replace("\\","/");
			File file = new File(folderPath);
			//如果目录存在则不需要再创建,否则逐级创建目录
			if(!file.exists())
			{
				//拆分路径,创建未创建的目录
				String[] folderPathArray = folderPath.split("/");
				StringBuffer pathBuf = new StringBuffer();
				
				for(int i =0;i<folderPathArray.length;i++)
				{
					if(!folderPathArray[i].equals(""))
					{
						if(i==0)
						{
							pathBuf.append(folderPathArray[i]);
						}else
						{
							pathBuf.append("/").append(folderPathArray[i]);
						}
						file = new File(pathBuf.toString());
						if(!file.exists())
						{
							boolean result = file.mkdir();
							if(!result)return null;
						}
					}
				}
			}
			//如果是隐藏目录
			if(isNomedia)
			{
				if(file.exists())
				{
					//创建.nomedia文件
					File nFiel = new File(folderPath+"/"+".nomedia");
					if(!nFiel.exists())
					{
						BufferedWriter bw = 
								new BufferedWriter(new OutputStreamWriter(new FileOutputStream(nFiel)));
						bw.write("\n\n");
//						bw.close();
					}
				}				
			}else
			{
				if(file.exists())
				{
					//删除.nomedia文件
					File nFiel = new File(folderPath+"/"+".nomedia");
					if(nFiel.exists())
					{
						nFiel.delete();
					}
				}
			}
			return folderPath;
		}catch(Exception e)
		{
			Log.e("createFolder", e.getMessage(),e);
		}
		return null;
	}
	
	 /**  
     *  复制单个文件  
     *  @param  oldPath  String  原文件路径  如：c:/fqf.txt  
     *  @param  newPath  String  复制后路径  如：f:/fqf.txt  
     *  @return  boolean  
     */  
   public  static void  copyFile(String  oldPath,  String  newPath)  {  
       try  {  
//           int  bytesum  =  0;  
           int  byteread  =  0;  
           File  oldfile  =  new  File(oldPath);  
           if  (oldfile.exists())  {  //文件存在时  
               InputStream  inStream  =  new  FileInputStream(oldPath);  //读入原文件  
               FileOutputStream  fs  =  new  FileOutputStream(newPath);  
               byte[]  buffer  =  new  byte[1444];  
//               int  length;  
               while  (  (byteread  =  inStream.read(buffer))  !=  -1)  {  
//                   bytesum  +=  byteread;  //字节数  文件大小  
//                   System.out.println(bytesum);  
                   fs.write(buffer,  0,  byteread);  
               }  
               inStream.close();  
           }  
       }  
       catch  (Exception  e)  {  
           System.out.println("复制单个文件操作出错");  
           e.printStackTrace();  
 
       }  
   }  

	/**
	 * 判断url路径的文件是否在root路径中存在 
	 * @param root
	 * @param url
	 * @return
	 */
	public static String getLocalpathByUrl(String root,String url,boolean isUpdateTime)
	{
		String filename = ToolsUtil.getFilenameByURL(url);
		String sdPath = PhoneSDUtil.getInstance().getSDpath();
		//这里需要加一个判断是否root是否已经包含SD卡的路径
		String rootPath = sdPath+"\\"+root;		
		String filePath = rootPath+"\\"+filename;
		filePath = filePath.replace("\\","/");
		File file = new File(filePath);
		boolean result = file.exists();
		if(result)
		{
			if (isUpdateTime) {
				// 修改本地文件的最后修改时间
				long newModifiedTime = System.currentTimeMillis();
				file.setLastModified(newModifiedTime);
			}
		}else
		{
			filePath = null;
		}
		return filePath;
	}
	/**
	 * 获取没有本地的列表
	 * 
	 * @return List
	 */
//	public static List getNotLocalPathList(List list,String root,String urlField,boolean isUpdateTime) {
//		if (list == null||urlField == null)
//			return null;
//		Object[] listArray = list.toArray();
//		Object item;
//		String filelocalPath;
//		Object urlObj;
//		List result = new ArrayList();
//		for (int i = 0; i < listArray.length; i++) {
//			item =  listArray[i];
//			if (item != null) {
//				urlObj = AppBeanUtil.getObjfieldVal(item, urlField);
//				if ( urlObj!= null) {
//					filelocalPath = getLocalpathByUrl(root,String.valueOf(urlObj), isUpdateTime);
//					if (filelocalPath == null) {
//						result.add(item);
//					}
//				}
//			}
//		}
//		return result;
//	}
	/**
	 * 在SD卡中创建图片文件夹
	 * @return String
	 * @throws IllegalAccessException
	 */
	public static String getImageFolder() throws IllegalAccessException
	{
		String tchpath = imageRootPath;
		String result = createFolder(tchpath,true);		
		if(result==null)
			throw new IllegalAccessException("创建图片文件夹失败");
		return result;
	}
	
	/**
	 * 获取有本地图片的列表
	 * 
	 * @return List
	 */
//	public static List getLocalPathList(List list,String root,String urlField,boolean isUpdateTime) {
//		if (list == null||urlField == null)
//			return null;
//		Object[] listArray = list.toArray();
//		Object item;
//		String filelocalPath;
//		Object urlObj;
//		List result = new ArrayList();
//		for (int i = 0; i < listArray.length; i++) {
//			item =  listArray[i];
//			if (item != null) {
//				urlObj = AppBeanUtil.getObjfieldVal(item, urlField);
//				if ( urlObj!= null) {
//					filelocalPath = getLocalpathByUrl(root,String.valueOf(urlObj), isUpdateTime);
//					if (filelocalPath != null) {
//						result.add(item);
//					}
//				}
//			}
//		}
//		return result;
//	}
	
	public static String getLogFolder()throws IllegalAccessException
	{
		String tchpath = logPath;
		String result = createFolder(tchpath,false);		
		if(result==null)
			throw new IllegalAccessException("创建日志文件夹失败");
		return result;
	}
	
	public static String getAPKFolder()throws IllegalAccessException
	{
		String tchpath = apkRootPath;
		String result = createFolder(tchpath,false);		
		if(result==null)
			throw new IllegalAccessException("创建apk文件夹失败");
		return result;
	}
		
	/**
	 * 初始化数据源的本地路径
	 */
//	public static List listItemGetLocalPathByURL(List list,String urlField,String localField,boolean isUpdateTime) {
//		if (list == null||localField==null||urlField==null)
//			return list;
//		String root = imageRootPath;
//		Object[] listArray = list.toArray();
//		Object item;
//		String filelocalPath;
//		String url;
//		if (list != null) {
//			for (int i = 0; i < listArray.length; i++) {
//				item = listArray[i];
//				if (item != null) {
//					url = (String) AppBeanUtil.getObjfieldVal(item,urlField);
//					if ( url != null) {
//						filelocalPath = AppSDUtil.getLocalpathByUrl(root,url,true);
//						if (filelocalPath != null||AppBeanUtil.getObjfieldVal(item,localField)==null) {
//							AppBeanUtil.setObjfieldVal(item, localField, filelocalPath);
//							// 替换原来的imageBean
//							list.remove(i);
//							list.add(i, item);
//						}
//					}
//				}
//			}
//		}
//		return list;
//	}
	/**
	 * 在SD卡中创建图形文件
	 * @param bm
	 * @param path
	 * @param fileName
	 * @return String
	 */
	public static String createBitmapFile(Bitmap bm, String path,
			String fileName,boolean isReplace) {
		String allfile = path + "\\" + fileName;
		//统一分隔符
		allfile = allfile.replace("\\","/");
		File file = new File(allfile);
		Bitmap.CompressFormat fileType = ToolsUtil.getBitmapTypeByFilename(fileName);
		if(isReplace)
		{//是替换
			if(file.exists())
				file.delete();//删除
		}
		if (!file.exists()&&bm!=null) {
			try {
				file.createNewFile();
				OutputStream outStream = new FileOutputStream(file);
				if(fileType!=null)
				{
					bm.compress(fileType, 100, outStream);
					outStream.flush();
					outStream.close();
					//释放图片内存
					bm.recycle();
					bm = null;
					Log.d("createBitmapFile","保存图像文件成功!");
				}else
				{
					Log.e("createBitmapFile","无法识别类型的图片，保存失败");
				}
			} catch (IOException e) {
				Log.d("createBitmapFile","保存图像文件失败!", e);
			}
		}
		return allfile;
	}
	
	
}
