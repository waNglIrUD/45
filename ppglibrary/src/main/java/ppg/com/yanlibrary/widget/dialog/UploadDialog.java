package ppg.com.yanlibrary.widget.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.File;

import ppg.com.yanlibrary.R;
import ppg.com.yanlibrary.activity.BaseActvity;
import ppg.com.yanlibrary.utils.AppSDUtil;
import ppg.com.yanlibrary.utils.DensityUtil;

/**
 * @author jie.yang
 */
public class UploadDialog extends EffectDialog {

    /**
     * 选择照片请求
     */
    public static final int REQUEST_RESULT_SELECT_PICTURE = 10001;
    /**
     * 拍照请求
     */
    public static final int REQUEST_RESULT_TAKE_PICTURE = 10002;

    Fragment mFragment;

    public UploadDialog(BaseActvity context) {
        super(context);
    }

    public UploadDialog(BaseActvity context, Fragment fragment) {
        super(context);
        mFragment = fragment;
    }

    @Override
    protected void onPaddingFrame(FrameLayout frame) {
        //拍照
        frame.addView(LayoutInflater.from(baseActvity).inflate(R.layout.dialog_upload_pic, null));
        ImageButton takeImage = (ImageButton) frame.findViewById(R.id.dialog_upload_take);

        takeImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                intentPicturesTake();
            }
        });
        //相册
        ImageButton seletImage = (ImageButton) frame.findViewById(R.id.dialog_upload_selet);
        seletImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                intentPictureSelect();
            }
        });
    }

    protected void intentPicturesTake() {
        Activity activity = baseActvity;
        String iRootPath;
        try {
            iRootPath = AppSDUtil.getImageFolder();
        } catch (IllegalAccessException e) {
            Toast.makeText(activity, "获取SD卡不成功", Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        File file = new File(iRootPath, System.currentTimeMillis() + ".jpg");
        Uri mOutPutFileUri = Uri.fromFile(file);
        // pictureBox.setFilePach(mOutPutFileUri.getPath());
        // pictureImage.setTag(mOutPutFileUri);//保存路径

        SharedPreferences preferences = activity.getSharedPreferences(
                "PicturesUri", Context.MODE_PRIVATE);
        Editor editor = preferences.edit();
        editor.putString("uri", mOutPutFileUri.toString());
        editor.putString("rootPath", file.toString());
        editor.commit();

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mOutPutFileUri);// 添加照片存储路径
        if (mFragment != null)
            mFragment.startActivityForResult(intent, REQUEST_RESULT_TAKE_PICTURE);
        else
            activity.startActivityForResult(intent, REQUEST_RESULT_TAKE_PICTURE);
    }

    /**
     * 调用系统应用函数  Intent.createChooser
     */
    protected void intentPictureSelect() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		intent.setType("image/*");
        /*Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image*//*");

         intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);*/
       if (mFragment != null) {
           mFragment.startActivityForResult(Intent.createChooser(intent, "选择图片"), REQUEST_RESULT_SELECT_PICTURE);
//            mFragment.startActivityForResult(intent, REQUEST_RESULT_SELECT_PICTURE);
        }else {
            baseActvity.startActivityForResult(Intent.createChooser(intent, "选择图片"), REQUEST_RESULT_SELECT_PICTURE);
//            baseActvity.startActivityForResult(intent, REQUEST_RESULT_SELECT_PICTURE);
        }
    }

    @Override
    protected int getDipH() {
        return DensityUtil.dip2px(getContext(), 200);
    }

}
