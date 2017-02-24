package ppg.com.yanlibrary.fragment;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

import ppg.com.yanlibrary.utils.AppSDUtil;
import ppg.com.yanlibrary.utils.KitKatPhoto;


public abstract class UpLoadingBitmapFragment extends LoadingFragment {
    /**
     * 选择照片请求
     */
    public static final int REQUEST_RESULT_SELECT_PICTURE = 10001;
    /**
     * 拍照请求
     */
    public static final int REQUEST_RESULT_TAKE_PICTURE = 10002;
    protected Bitmap prictureBitmap;
    protected ImageView headPortrait;

    public UpLoadingBitmapFragment(boolean momentInit) {
        super(momentInit);
    }

    protected void intentPictureSelect() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivityForResult(Intent.createChooser(intent, "选择图片"),
                REQUEST_RESULT_SELECT_PICTURE);
    }

    protected Uri getURI(Intent data) {
        if (prictureBitmap != null)// 如果不释放的话，不断取图片，将会内存不够
        {
            prictureBitmap.recycle();
        }
        Uri uri;
        if (null != data) {
            uri = data.getData();
        } else {
            uri = (Uri) headPortrait.getTag();
        }
        if (uri == null) {
            uri = Uri.parse(MediaStore.Images.Media.insertImage(getActivity()
                    .getContentResolver(), (Bitmap) data.getExtras()
                    .get("data"), null, null));
        }
        return uri;
    }

    protected void intentPicturesTake() {
        String iRootPath;
        try {
            iRootPath = AppSDUtil.getImageFolder();
        } catch (IllegalAccessException e) {
            Toast.makeText(getActivity(), "获取SD卡不成功", Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        File file = new File(iRootPath, System.currentTimeMillis() + ".jpg");
        Uri mOutPutFileUri = Uri.fromFile(file);
        // pictureBox.setFilePach(mOutPutFileUri.getPath());
        // pictureImage.setTag(mOutPutFileUri);//保存路径

        SharedPreferences preferences = getActivity().getSharedPreferences(
                "PicturesUri", Context.MODE_PRIVATE);
        Editor editor = preferences.edit();
        editor.putString("uri", mOutPutFileUri.toString());
        editor.putString("rootPath", file.toString());
        editor.commit();

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mOutPutFileUri);// 添加照片存储路径
        startActivityForResult(intent, REQUEST_RESULT_TAKE_PICTURE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                //选图片
                case REQUEST_RESULT_SELECT_PICTURE:
                    /**
                     * 获取图片的路径和名字
                     */
                    Uri uri = getURI(data);
                    String[] proj = {MediaColumns.DATA};

                    Cursor cursor = getActivity().getContentResolver().query(uri, proj, null, null, null);
                    String path = "";

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        path = KitKatPhoto.getPath(getActivity(), uri);
                    } else {
                        int column_index = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
                        // 将光标移至开头 ，这个很重要，不小心很容易引起越界
                        cursor.moveToFirst();
                        // 最后根据索引值获取图片路径
                        path = cursor.getString(column_index);
                    }
                    doAfterGetBitPath(path);
                    break;
                //拍照
                case REQUEST_RESULT_TAKE_PICTURE:
                    SharedPreferences preferences = getActivity()
                            .getSharedPreferences("PicturesUri",
                                    Context.MODE_PRIVATE);
                    String uriStr = preferences.getString("uri", null);
                    String rootPath = preferences.getString("rootPath",
                            null);
                    if (uriStr != null) {
                        if (prictureBitmap != null)// 如果不释放的话，不断取图片，将会内存不够
                        {
                            prictureBitmap.recycle();
                        }
                        doAfterGetBitPath(rootPath);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    public abstract void doAfterGetBitPath(String pathstr);

    /**
     * 图片压缩
     */
    public Bitmap getDecodeBitmap(String srcPath) {
        System.gc();// 强制回收内存
        // 压缩
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(srcPath, opt);
        int w = opt.outWidth;
        int h = opt.outHeight;
//		int wh = w * h;
        int decodesize = 1;
//		if (wh > 204800) {
//			decodesize = wh / 204800;
//		}

        if (w > h && w > 480) {//如果宽度大的话根据宽度固定大小缩放
            decodesize = (int) (w / 480);
        } else if (w < h && h > 800) {//如果高度高的话根据宽度固定大小缩放  
            decodesize = (int) (h / 800);
        }
        opt.inSampleSize = decodesize;

        opt.inPreferredConfig = Bitmap.Config.ARGB_8888;
        opt.inJustDecodeBounds = false;
        Bitmap srcbitmap = BitmapFactory.decodeFile(srcPath, opt);
        return srcbitmap;
    }

}
