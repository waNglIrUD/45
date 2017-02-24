package ppg.com.yanlibrary.fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

import ppg.com.yanlibrary.R;
import ppg.com.yanlibrary.utils.DensityUtil;
import ppg.com.yanlibrary.utils.StringUtil;
import ppg.com.yanlibrary.widget.SweetAlert.SweetAlertDialog;
import ppg.com.yanlibrary.widget.progressbar.RotateLoading;
import xprogressdialog.XProgressDialog;

/**
 * 提供异步加载功能
 *
 * @author jie.yang
 */
@SuppressLint("ParcelCreator")
public abstract class LoadingFragment extends Fragment {
    protected XProgressDialog xProgressDialog = null;
    protected RotateLoading mProgressBar = null;
    protected View mContent = null;
    protected FrameLayout mRoot = null;
    //	private final String TAG_INDEX = "tag";
//	private final String TAG_TRANSMITDATA = "TransmitData";
    private volatile boolean isStop = false;
    private boolean launchAnimation = false;

    LayoutInflater mInflater;
    ViewGroup mContainer;
    Bundle mSavedInstanceState;

    CreateViewCallBack mCreateViewCallBack;

    private boolean mMomentInit = true;
    private boolean mDelayAsyn = false;
    private SweetAlertDialog dialog;

//	private boolean isHide = false;

    public View getRoot() {
        return mRoot;
    }

    public LoadingFragment(boolean momentInit) {
        mMomentInit = momentInit;
    }

    public void setDelayAsyn(boolean delayAsyn) {
        mDelayAsyn = delayAsyn;
    }

    public void setLaunchAnimation(boolean launchAnimation) {
        this.launchAnimation = launchAnimation;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (xProgressDialog == null) {
            xProgressDialog = new XProgressDialog(getActivity(), 2);
        }
    }

//	@Override
//	public void onSaveInstanceState(Bundle outState) {
//		outState.putBoolean(key, value)
//	}

    // 防止重叠
//	@Override
//    public void onDetach() {
//        super.onDetach();
//        try {
//            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
//            childFragmentManager.setAccessible(true);
//            childFragmentManager.set(this, null);
//        } catch (NoSuchFieldException e) {
//            throw new RuntimeException(e);
//        } catch (IllegalAccessException e) {
//            throw new RuntimeException(e);
//        }
//    }

    public void setCreateViewCallBack(CreateViewCallBack createViewCallBack) {
        mCreateViewCallBack = createViewCallBack;
    }

    public static interface CreateViewCallBack {
        public void onCreateViewCallBack(View root);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mInflater = inflater;
        mContainer = container;
        mSavedInstanceState = savedInstanceState;
        // 方案一，此方案在系统4.0以上正确，但是2.3出现BUG，估计是不能重写layout
        // 添加加载条 , 强制把加载条设置到容器中间
        /* View root = mRoot = onLoadingCreateView(inflater, container, savedInstanceState);
           ProgressBar bar = mProgressBar = new ProgressBar(getActivity()) {
			@Override
			public void layout(int l, int t, int r, int b) {
				int rw = mRoot.getMeasuredWidth() >> 1;
				int size = DensityUtil.dip2px(getActivity(), 20);
				int rh = mRoot.getMeasuredHeight() >> 1;
				super.layout(rw - size, rh - size, 
						rw + size, rh + size);
			}
		};
		
		if(root instanceof ViewGroup) {
			ViewGroup group = (ViewGroup) root;
			group.addView(bar);
		}*/


        // 方案二，兼容各个版本
        FrameLayout root = mRoot = new FrameLayout(getActivity()) {
            //这段代码不会立马执行
            protected void onLayout(boolean changed, int left, int top,
                                    int right, int bottom) {
                super.onLayout(changed, left, top, right, bottom);
                View v = getChildAt(getChildCount() - 1);
                int rw = getMeasuredWidth() >> 1;
                //得到当前
                int size = DensityUtil.dip2px(getActivity(), 30);
                //>>移位 移动一位就是减半  也就是得到当前laoout的一般宽度
                int rh = getMeasuredHeight() >> 1;
                //定制 进度条的位置
                v.layout(rw - size, rh - size,
                        rw + size, rh + size);
            }
        };
        root.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        root.setBackgroundColor(Color.parseColor("#F5F5F5"));
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { // 如果是Android 5.0 就加入水波纹过场特效
//            mProgressBar = new RoundProgressBarWidthNumber(getActivity());
//        }else {
//            第三方支持包  然后在低版本实现5.0效果
        mProgressBar = new RotateLoading(getActivity());
        //设置进度条颜色
//        mProgressBar.setLoadingColor();
        mProgressBar.start();
//        }
        // 立刻初始化内容数据
        if (mMomentInit) {
            //子类实现onLoadingCreateView
            View layout = null;
            try {
                layout = mContent = onLoadingCreateView(inflater, container, savedInstanceState);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            root.addView(layout);
            mProgressBar.setVisibility(View.GONE);
            if (launchAnimation) {
                mContent.setVisibility(View.INVISIBLE);
                mContent.setAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.view_show));
                new Handler() {
                    public void dispatchMessage(Message msg) {
                        if (!isStop) {
                            loadingAnimation(mContent);
                        }
                    }

                    ;
                }.sendMessage(new Message());
            }
        }
        //加载网络错误时的emptyDialog
        initEmptyDialog();
        //加载网络错误时的emptyPage
        frameLayout = new FrameLayout(getActivity());
        FrameLayout.LayoutParams f = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        frameLayout.setVisibility(View.GONE);
        //加载按钮的布局
        View bt_layout = mInflater.inflate(R.layout.load_button, null);
        //加入布局后才能使用
        frameLayout.addView(bt_layout);
        //找到按钮
        View bt = frameLayout.findViewById(R.id.tv_load_more);
//        FrameLayout.LayoutParams btf = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
//                FrameLayout.LayoutParams.WRAP_CONTENT);
//        btf.gravity = Gravity.CENTER;
//        bt.setLayoutParams(btf);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEmptyPageClick(v);
            }
        });
        dialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                onEmptyDialogCancelClick();
            }
        });
        dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                onEmptyDialogConfirmClick();
            }
        });
        root.addView(frameLayout, f);
        root.addView(mProgressBar);
        //requestData();
        if (!mDelayAsyn)
            //默认调用这个
            onCreateViewRequestData();
        if (mCreateViewCallBack != null)
            mCreateViewCallBack.onCreateViewCallBack(root);
        return root;
    }

    public void executeDelayAsyn() {
        if (mDelayAsyn) {
            onCreateViewRequestData();
            mDelayAsyn = false;
        }
    }

    FrameLayout frameLayout;
//    @Override
//    public void onHiddenChanged(boolean hidden) {
//        super.onHiddenChanged(hidden);
//        if(hidden) {
//            ImageLoader.createImageLoader(getActivity()).clearMemoryCache();
//            ToastUtil.toast2_bottom(getActivity(), "!!");
//        }
//    }

    public boolean isDestroyView() {
        return isStop;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isStop = true;
        mContent = null;
        mInflater = null;
        mContainer = null;
        mSavedInstanceState = null;
        mRoot = null;
    }

    public boolean isLoadingContent() {
        return mContent != null ? true : false;
    }

    protected void onEmptyPageClick(View view) {
        emptyPageHide();
        //   ToastUtil.toast2_bottom(getActivity(),"sssssssss");
    }

    protected void onEmptyDialogCancelClick() {
        emptyDialogHide();
        //   ToastUtil.toast2_bottom(getActivity(),"sssssssss");
    }

    protected void onEmptyDialogConfirmClick() {
        emptyDialogHide();
        //   ToastUtil.toast2_bottom(getActivity(),"sssssssss");
    }

    /**
     * 网络错误时的页面
     */
    public void emptyPageShow() {
        frameLayout.setVisibility(View.VISIBLE);

        mProgressBar.setVisibility(View.GONE);

    }

    public void emptyPageHide() {
        frameLayout.setVisibility(View.GONE);
        frameLayout.setAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.view_hide));
        mProgressBar.setVisibility(View.GONE);
        mProgressBar.setAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.view_show));
    }

    public void initEmptyDialog() {
        dialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.CUSTOM_IMAGE_TYPE, false);
        dialog.getProgressHelper().setBarColor(getResources().getColor(R.color.backgroud_theme));
        dialog.setCustomImage(R.drawable.wifi);
        dialog.setCancelText("取消");
        dialog.setTitleText("网络连接异常");
        dialog.setContentText("是否重试?");
        dialog.setConfirmText("是的");
        dialog.setCanceledOnTouchOutside(true);
    }

    /**
     * 网络错误时的对话框
     */
    public void emptyDialogShow() {
        dialog.show();

        mProgressBar.setVisibility(View.GONE);
        mProgressBar.setAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.view_hide));
    }

    public void emptyDialogHide() {
        dialog.dismiss();
        mProgressBar.setVisibility(View.GONE);
        mProgressBar.setAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.view_hide));
    }

    public boolean loadingContent() {
        if (!isStop) {
            if (mContent == null) {
                try {
                    mContent = onLoadingCreateView(mInflater, mContainer, mSavedInstanceState);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                mRoot.addView(mContent, 0);
            }
            mContent.setVisibility(View.VISIBLE);
            mContent.setAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.view_show));
            mProgressBar.setVisibility(View.GONE);
            return true;
        } else
            return false;
    }

    public void showXProgerssDialog(String msg) {
        if (!StringUtil.isEmpty(msg)) {
            xProgressDialog.setMessage(msg);
        }
        xProgressDialog.show();
    }

    public void showXProgerssDialog() {
        xProgressDialog.show();
    }

    public void showXProgerssNoMissDialog() {
        xProgressDialog.setCancelableAble(false);


        xProgressDialog.show();
    }

    public void dismissXProgerssDialog() {
        xProgressDialog.dismiss();
    }

    public boolean loadingWithAnimationContent() {
        Log.v("abc", "mContent");
        if (!isStop) {
            if (mContent == null) {
                Log.v("abc", "mContent");
                try {
                    mContent = onLoadingCreateView(mInflater, mContainer, mSavedInstanceState);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                mRoot.addView(mContent, 0);
                new Handler() {
                    public void dispatchMessage(Message msg) {
                        if (!isStop) {
                            loadingAnimation(mContent);
                        }
                    }

                    ;
                }.sendMessage(new Message());
            }
            mContent.setVisibility(View.INVISIBLE);
            mProgressBar.setVisibility(View.GONE);
            return true;
        } else
            return false;
    }

    protected void loadingAnimation(View content) {
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { // 如果是Android 5.0 就加入水波纹过场特效
//            content.setVisibility(View.VISIBLE);
//            Animator animator = ViewAnimationUtils.createCircularReveal(content, content.getWidth() / 2, content.getHeight() / 2,
//                    0, content.getWidth()/ 2);
//            animator.setInterpolator(new AccelerateInterpolator());
//            animator.setDuration(200);
//            animator.start();
//        }else {
        content.setVisibility(View.VISIBLE);
        AlphaAnimation alpha = new AlphaAnimation(0.2f, 1f);
        alpha.setDuration(200);
        content.startAnimation(alpha);
//        }
    }

    public boolean restartLoadingContent() {
        if (!isStop) {
            if (mContent != null)
                mRoot.removeView(mContent);
            try {
                mContent = onLoadingCreateView(mInflater, mContainer, mSavedInstanceState);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            mRoot.addView(mContent, 0);
            mContent.setVisibility(View.VISIBLE);
            mContent.setAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.view_show));
            mProgressBar.setVisibility(View.GONE);
            return true;
        } else
            return false;
    }

    public void showProgressBar() {
        if (mContent != null && mProgressBar != null) {
            mContent.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.VISIBLE);
        }
    }

    private ProgressDialog progressDialog;

    /**
     * 简单的进度启动与关闭
     *
     * @param message
     */
    protected void progressStart(String message) {
        if (null == progressDialog) {
            progressDialog = new ProgressDialog(getActivity());
        }
        progressDialog.setMessage(message);
        if (!progressDialog.isShowing()) {
            progressDialog.show();
            //((Activity) progressDialog.getContext()).getWindow().setLayout(50, 50);
        }
    }

    protected void progressClose() {
        if (null == progressDialog)
            return;
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }

    public abstract View onLoadingCreateView(LayoutInflater inflater, ViewGroup container,
                                             Bundle savedInstanceState) throws PackageManager.NameNotFoundException;

    /**
     * 可以在这个方法请求网络数据，这个方法会在fragment第一次创建调用
     */
    protected void onCreateViewRequestData() {
    }


//	public void requestData(int tag, List<?> datas) {
//		mTask.doTask(tag, datas, false, this);
//	}
//
//	public void requestData(int tag, List<?> datas, String progressDialogMsg) {
//		mTask.doTask(tag, datas, progressDialogMsg, this);
//	}

    @Override
    public void onDestroy() {
        super.onDestroy();
//		ImageLoader.createImageLoader(getActivity()).clearMemoryCache();
    }
}
