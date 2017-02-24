package ppg.com.yanlibrary.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ppg.com.yanlibrary.R;
import ppg.com.yanlibrary.fragment.LoadingFragment;
import ppg.com.yanlibrary.utils.SystemBarTintManager;
import ppg.com.yanlibrary.widget.TopBarLayout;
import ppg.com.yanlibrary.widget.menu.SimpleMenuLayout;


/**
 * 详细界面
 *
 * @author jie.yang
 */
public abstract class TopBarActvity extends BaseActvity implements SimpleMenuLayout.Action {

    public static final String INTENT_TITLE_KEY = "title";
    public static final String INTENT_SHOW_MENU_KEY = "show_menu";
    public static final String INTENT_FRAGMENT_INDEX_KEY = "fragment_index";
    protected SimpleMenuLayout mSimpleMenuLayout;
    private long timeDValue = 0; // 计算时间差值，判断是否需要退出
    private KeyDownListener listener;
    private TopBarLayout topBar;
    private FrameLayout viewRoot = null;
    private View mTopBarRoot = null;
    private FunctionType mFunctionType = FunctionType.single;

    private List<String> fragmentTag = new ArrayList<String>();

    //	private Toolbar toolbar;
    public void setTopBarTitle(String title) {
        topBar.setTopBarTitle(title);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) { // 恢复信息
            onCreateSavedInstanceState(savedInstanceState);
        }

        setContentView(R.layout.actvity_top_bar);

//        Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            // 激活状态栏设置
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.backgroud_theme);
            SystemBarTintManager.SystemBarConfig config = tintManager.getConfig();
            findViewById(R.id.top_bar_root).setPadding(0, config.getStatusBarHeight(), 0, 0);

        }

//        toolbar = (Toolbar) findViewById(R.id.toolbar);
        initToolbar();

        if (mFunctionType == FunctionType.single) {
            installSimpleFragment(getIntent().getIntExtra(INTENT_FRAGMENT_INDEX_KEY, -1));
            defaultFragmentInited(getSupportFragmentManager().findFragmentById(R.id.detail_container), 0);
        } else if (mFunctionType == FunctionType.multilevel) {
            int index = getDefaultMultilevelFragmentIndex();
            replaceFragmentCache(index, "fragment_key_" + index);
            defaultFragmentInited(getSupportFragmentManager().findFragmentById(R.id.detail_container), index);
        }
    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    public void SettingTopHide(boolean ishide) {
        findViewById(R.id.detail_top_bar).setVisibility(View.GONE);
//        findViewById(R.id.detail_top_bar_shadow).setVisibility(View.GONE);
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.detail_container);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) frameLayout.getLayoutParams();
        layoutParams.setMargins(0, 0, 0, 0);
        frameLayout.setLayoutParams(layoutParams);


        //这里的操作是因为在隐藏掉top后  在主页的时候因为生成底部的Menu导航 占用了高度，所以要重新设置回去
        if (ishide) {
            int mH = getResources().getDimensionPixelSize(R.dimen.widget_size_main_bottom_menu_height);
            layoutParams.setMargins(0, (int) getResources().getDimension(R.dimen.top_bar_h), 0, 0);

            frameLayout.setLayoutParams(layoutParams);
            FrameLayout container = (FrameLayout) findViewById(R.id.detail_container);
            FrameLayout.LayoutParams ffl = (FrameLayout.LayoutParams) container.getLayoutParams();
            // ffl.topMargin = getResources().getDimensionPixelSize(R.dimen.top_bar_h);
            ffl.topMargin = 0;
            ffl.bottomMargin = mH;
            container.setLayoutParams(ffl);
        }

    }

    public void SettingTopShow() {
        findViewById(R.id.detail_top_bar).setVisibility(View.VISIBLE);
//        findViewById(R.id.detail_top_bar_shadow).setVisibility(View.VISIBLE);
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.detail_container);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) frameLayout.getLayoutParams();

        int mH = getResources().getDimensionPixelSize(R.dimen.widget_size_main_bottom_menu_height);
        layoutParams.setMargins(0, (int) getResources().getDimension(R.dimen.top_bar_h), 0, 0);

        frameLayout.setLayoutParams(layoutParams);
        FrameLayout container = (FrameLayout) findViewById(R.id.detail_container);
        FrameLayout.LayoutParams ffl = (FrameLayout.LayoutParams) container.getLayoutParams();
        ffl.topMargin = getResources().getDimensionPixelSize(R.dimen.top_bar_h);
//            ffl.topMargin = 0;
        ffl.bottomMargin = mH;
        container.setLayoutParams(ffl);

    }

    protected void defaultFragmentInited(Fragment fragment, int index) {
    }

    private void initToolbar(/*Toolbar toolbar*/) {
        // 初始化顶部条
//        View topBarRoot = mTopBarRoot = getLayoutInflater().inflate(R.layout.widget_activity_header, null);
        View topBarRoot = mTopBarRoot = findViewById(R.id.detail_top_bar);
        topBar = new TopBarLayout(topBarRoot);
//        toolbar.getWrapper().setCustomView(mTopBarRoot);
//        toolbar.getWrapper().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
//        getSupportActionBar().setCustomView(mTopBarRoot);
//        getSupportActionBar().setDisplayShowCustomEnabled(true);
//        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
//        if (getSupportActionBar() != null) {
//            getSupportActionBar().setTitle("");
//        }

        // 设置标题
        TextView title = (TextView) topBarRoot.findViewById(R.id.NavigateTitle);
        title.setText(getIntent().getStringExtra(INTENT_TITLE_KEY));
        onCreateView(topBar);
    }

    protected void onCreateSavedInstanceState(Bundle savedInstanceState) {
        int size = savedInstanceState.getInt("fragmentTag.size");
        for (int i = 0; i < size; ++i) {
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction ft = manager.beginTransaction();
            ft.remove(manager.findFragmentByTag(savedInstanceState.getString(String.valueOf(i))));
            ft.commitAllowingStateLoss();
            manager.executePendingTransactions();
        }
    }

    public Fragment getContextFragment() {
        FragmentManager manager = getSupportFragmentManager();
        return manager.findFragmentById(R.id.detail_container);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState); // 保存信息
        int size = fragmentTag.size();
        outState.putInt("fragmentTag.size", size);
        for (int i = 0; i < size; ++i)
            outState.putString(String.valueOf(i), fragmentTag.get(i));
    }

    public View getTopBarRoot() {
        return mTopBarRoot;
    }

    public EditText showSearchEditText() {
        EditText editText = (EditText) findViewById(R.id.search_detail_edittext);
        editText.setVisibility(View.VISIBLE);
        return editText;
    }

    public TopBarLayout getTopBar() {
        return topBar;
    }

    public FrameLayout getViewRoot() {
        if (viewRoot == null)
            viewRoot = (FrameLayout) findViewById(R.id.top_bar_root);
        return viewRoot;
    }

    public void setFunctionModel(FunctionType functionModel) {
        mFunctionType = functionModel;
    }

    protected abstract void onCreateView(TopBarLayout topBar);

    protected void onInstallMenuLayout(SimpleMenuLayout simpleMenuLayout) {
    }

    protected int getDefaultMultilevelFragmentIndex() {
        return 0;
    }

    public void invisibleButtomMenu() {
        if (mSimpleMenuLayout != null) {
            mSimpleMenuLayout.setVisibility(View.INVISIBLE);
        }
    }

    public void goneButtonMenuANDTop() {
        findViewById(R.id.detail_top_bar).setVisibility(View.GONE);
//        findViewById(R.id.detail_top_bar_shadow).setVisibility(View.GONE);
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.detail_container);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) frameLayout.getLayoutParams();
        layoutParams.setMargins(0, 0, 0, 0);
        frameLayout.setLayoutParams(layoutParams);
        if (mSimpleMenuLayout != null) {
            mSimpleMenuLayout.setVisibility(View.GONE);
            Resources resources = getResources();
            FrameLayout container = (FrameLayout) findViewById(R.id.detail_container);
            FrameLayout.LayoutParams ffl = (FrameLayout.LayoutParams) container.getLayoutParams();
            ffl.topMargin = resources.getDimensionPixelSize(R.dimen.top_bar_h);
//            ffl.topMargin = 0;
            ffl.bottomMargin = 0;
            container.setLayoutParams(ffl);
        }
    }

    public void goneButtomMenu() {

        if (mSimpleMenuLayout != null) {
            mSimpleMenuLayout.setVisibility(View.GONE);
            Resources resources = getResources();
            FrameLayout container = (FrameLayout) findViewById(R.id.detail_container);
            FrameLayout.LayoutParams ffl = (FrameLayout.LayoutParams) container.getLayoutParams();
            ffl.topMargin = resources.getDimensionPixelSize(R.dimen.top_bar_h);
//            ffl.topMargin = 0;
            ffl.bottomMargin = 0;
            container.setLayoutParams(ffl);
        }
    }

    public void showButtomMenu() {
        if (mSimpleMenuLayout != null) {
            mSimpleMenuLayout.setVisibility(View.VISIBLE);
        }
    }


    public void installButtomMenu() {
        if (mSimpleMenuLayout == null) {
            Resources resources = getResources();
            FrameLayout root = getViewRoot();

            int mH = resources.getDimensionPixelSize(R.dimen.widget_size_main_bottom_menu_height);

            FrameLayout container = (FrameLayout) findViewById(R.id.detail_container);
            FrameLayout.LayoutParams ffl = (FrameLayout.LayoutParams) container.getLayoutParams();
            ffl.topMargin = resources.getDimensionPixelSize(R.dimen.top_bar_h);
//            ffl.topMargin = 0;
            ffl.bottomMargin = mH;

            container.setLayoutParams(ffl);

            mSimpleMenuLayout = new SimpleMenuLayout(this);
            FrameLayout.LayoutParams ffl1 = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, mH);
            ffl1.gravity = Gravity.BOTTOM;
            mSimpleMenuLayout.setBackgroundResource(R.drawable.border_white_thin);
            root.addView(mSimpleMenuLayout, ffl1);
            setButtomBar(root);
            //绑定事件
            mSimpleMenuLayout.setAction(this);
        }
        mFunctionType = FunctionType.multilevel;
        onInstallMenuLayout(mSimpleMenuLayout);
    }

    /**
     * 用于生成特殊bar   见项目晒书法
     *
     * @param root
     */
    protected void setButtomBar(View root) {

    }

    public void showTopBarOnlyBack() {
        topBar.showOnlyBack(new OnClickListener() {

            @Override
            public void onClick(View v) {
                TopBarActvity.this.setResult(Activity.RESULT_CANCELED);
                TopBarActvity.this.finish();
            }
        });
    }

    public void HideOperation_LR() {
        topBar.HideOperation_LR();
    }

    public void showTopBarOnlyBack(OnClickListener backClickListener) {
        topBar.showOnlyBack(backClickListener);
    }

    public void setKeyDownListener(KeyDownListener Listener) {
        this.listener = Listener;
    }

    protected void installSimpleFragment(int index) {
    }

//	public void intentMainActivity(int index) {
//		Context context = this;
//		Intent intent = new Intent(context, MainActivity.class);
//		intent.putExtra(MainActivity.INTENT_SHOW_FUNCTION_INDEX_KEY, index);
//		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//		startActivity(intent);
//	}

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mFunctionType == FunctionType.single) {
            boolean isGo = false;
            if (keyDownProxy != null) {
                isGo = keyDownProxy.onKeyDown(keyCode, event);
                if (isGo)
                    return true;
            }

            boolean ret = false;
            if (listener != null)
                ret = listener.onKeyDown(keyCode, event);
            if (ret)
                return true;
            else
                return super.onKeyDown(keyCode, event);
        } else if (mFunctionType == FunctionType.multilevel) {
            boolean isGo = false;
            if (keyDownProxy != null) {
                isGo = keyDownProxy.onKeyDown(keyCode, event);
                if (isGo)
                    return true;
            }

            if (keyCode == KeyEvent.KEYCODE_BACK) {

                moveTaskToBack(false);  //拦截back键，执行home键的动作
                return true;
/*
                // 判断两次返回键退出
                if (timeDValue == 0) {
                    ToastUtil.toast2_bottom(this,
                            getResources().getString(R.string.exit_hint), 1000);
                    timeDValue = System.currentTimeMillis();
                    return true;
                } else {
                    timeDValue = System.currentTimeMillis() - timeDValue;
                    if (timeDValue >= 1500) { // 大于1.5秒不处理。
                        timeDValue = 0;
                        return true;
                    } else {// 退出应用
                        // 完成第一次启动
                        // FirstLaunchUtils.firstLaunch(this);
                        ImageLoader.createImageLoader(this).clearMemoryCache();
                        android.os.Process.killProcess(android.os.Process.myPid());
                        System.exit(0);
                    }
                }*/

            }

            return super.onKeyDown(keyCode, event);
        } else
            return super.onKeyDown(keyCode, event);
    }

    private void clearAllFragment() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        int size = fragmentTag.size();
        for (int i = 0; i < size; ++i)
            ft.remove(manager.findFragmentByTag(fragmentTag.get(i)));
        ft.commitAllowingStateLoss();
    }

    public void replaceFragmentCache(final int index, String tag) {
//		tag = tag + "-MainActivity";
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();

        // 找到原来的
        Fragment fTemp = manager.findFragmentByTag(tag);
        if (fTemp != null) {
            int size = fragmentTag.size();
            for (int i = 0; i < size; ++i) {
                ft.hide(manager.findFragmentByTag(fragmentTag.get(i)));
            }
            ft.show(fTemp);
        } else {
            fTemp = holdAtObtainFragment(index);
            if (fTemp != null) {
                int size = fragmentTag.size();
                for (int i = 0; i < size; ++i)
                    ft.hide(manager.findFragmentByTag(fragmentTag.get(i)));
                ft.add(R.id.detail_container, fTemp, tag);
                fragmentTag.add(tag);
            }
        }
        if (fTemp != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { // 如果是Android 5.0 就加入水波纹过场特效
            View root = fTemp.getView();
            ripple(root, index);
            if (fTemp instanceof LoadingFragment) { // 第一次进入页面
                ((LoadingFragment) fTemp).setCreateViewCallBack(new LoadingFragment.CreateViewCallBack() {
                    @Override
                    public void onCreateViewCallBack(final View root) {
                        root.setVisibility(View.INVISIBLE);
                        root.post(new Runnable() {
                            @Override
                            public void run() {
                                root.setVisibility(View.VISIBLE);
                                ripple(root, index);
                            }
                        });
                    }
                });
            }
        } else
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

        ft.commitAllowingStateLoss();
        manager.executePendingTransactions();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void ripple(View root, int index) {
//        if (root != null && mSimpleMenuLayout != null) {
//            int size = mSimpleMenuLayout.getItemCount();
//            int dW = root.getWidth() / size;
//            int w = (dW * (index + 1)) - dW / 2;
//            Animator animator = ViewAnimationUtils.createCircularReveal(root, w, root.getHeight(),
//                    0, root.getWidth());
//            animator.setInterpolator(new AccelerateInterpolator());
//            animator.setDuration(200);
//            animator.start();
//        }
    }

    protected void replaceFragment(Fragment fragment) {
        String tag = fragment.toString();
        FragmentManager manager = TopBarActvity.this.getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.add(R.id.detail_container, fragment, tag);
        ft.commitAllowingStateLoss();
        fragmentTag.add(tag);
        //必须在最后调用commit()
//如果在同一个container中添加了多个fragments，添加的顺序决定了他们在view层级中显示的顺序
//
//如果你在执行一个移除fragment操作的事务时不调用addToBackStack()。那么当这个transaction被提交后fragment会被销毁，并且用户不可能回退回去。
//相反，如果当移除fragment时，你调用addToBackStack()，那么这个fragment会stopped，并且如果用户导航回去它会resumed
//小提示：对于每一个fragment的事务，在commit()之前通过调用setTransition()，你可以使用一个过渡动画
//
//调用commit()并不是马上就执行这次事务，恰恰相反，一旦activity的UI线程有能力去完成，FragmentTransaction就把这次提交列入计划到activity的UI线程运行
//
//如果必要，不管怎样，你可以从你的UI线程调用executePendingTransactions()来通过commit()立即执行提交了的transaction。通常这样做并不是必须的，除非transaction是其他线程工作的依赖
//
//警告：只有在activity之前（当用户离开这个activity时）你可以用commit()提交一个transaction保存他的状态
//如果你尝试在这个时间点之后commit，将会收到一个异常。这是因为如果activity需要恢复，在commit之后的state可能会丢失。在你觉得可以丢失这次commit的情况下，可以使用commitAllowingStateLoss()
        manager.executePendingTransactions();
    }

    protected Fragment holdAtObtainFragment(int index) {
        return null;
    }

    @Override
    public boolean onMenuClick(View v, int index) {
//        ToastUtil.toast1_center(this, index + "");
        replaceFragmentCache(index, "fragment_key_" + index);
//        if (index == 0) {
//            return true;
//        }
        return false;
    }

    /**
     * 用于设置页面模式
     */
    public enum FunctionType {
        single,
        multilevel
    }

    public static interface KeyDownListener {
        public boolean onKeyDown(int keyCode, KeyEvent event);
    }
}
