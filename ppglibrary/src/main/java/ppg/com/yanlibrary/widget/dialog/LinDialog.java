package ppg.com.yanlibrary.widget.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import ppg.com.yanlibrary.R;
import ppg.com.yanlibrary.utils.DensityUtil;


/**
 * Created by yy on 2015/5/19.
 */
public class LinDialog {
    private String str = "";
    private EditText text;
    private Context mContext;
    private onClickListener onClickListener;
    private RadioButton radioButton1;
    private RadioButton radioButton2;

    private LinDialog(Context context) {

        mContext = context;
    }

    public static Dialog showDialog(Context context, int lay, DialogListener dialogListener) {
        AlertDialog dlg = new AlertDialog.Builder(context).create();
        dlg.show();
        Window window = dlg.getWindow();

        window.setContentView(lay);


        dialogListener.onLayout(window, dlg);

        return dlg;
    }

    public static LinDialog showDialog(String hint, String title, Context context) {
        LinDialog dialog = new LinDialog(context);

        dialog.showEditDialog(hint, title);
        return dialog;
    }



    public String getStr() {
        return str;
    }

    public void showEditDialog(String title, String hint) {
        LinearLayout linearLayout = new LinearLayout(mContext);
        LinearLayout.LayoutParams layou = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        linearLayout.setLayoutParams(layou);
        text = new EditText(mContext);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(mContext, 50));

        text.setLayoutParams(layoutParams);
        text.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        text.setBackgroundColor(mContext.getResources().getColor(R.color.white1));
        text.setHint(hint);
        linearLayout.addView(text);
        new AlertDialog.Builder(mContext).setTitle(title).setIcon(android.R.drawable.ic_dialog_info).
                setView(linearLayout).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                str = text.getText().toString();

                onClickListener.onClick(str);


            }
        }).setNegativeButton("取消", null).show();

    }

    public void setOnClickListener(onClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }



    public void showCheckedDialog(String title, String hint) {
        text = new EditText(mContext);

        text.setHint(hint);
        new AlertDialog.Builder(mContext).setTitle(title).setIcon(android.R.drawable.ic_dialog_info).
                setView(text).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                str = text.getText().toString();


            }
        }).setNegativeButton("取消", null).show();

    }

    public interface DialogListener {
        void onLayout(Window window, Dialog dialog);

    }

    public interface onClickListener {
        void onClick(String str);
    }


}



