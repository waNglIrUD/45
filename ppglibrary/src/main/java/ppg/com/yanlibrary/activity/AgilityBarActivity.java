package ppg.com.yanlibrary.activity;//package org.hahayj.library_main.activity;
//
//import com.webapps.library_main.R;
//import org.hahayj.library_main.widget.TopBarLayout;
//import org.yangjie.utils.common.DensityUtil;
//import com.handmark.pulltorefresh.library.PullToRefreshBase;
//import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
//import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
//import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
//import android.app.Activity;
//import android.graphics.Color;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentActivity;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentTransaction;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.animation.AlphaAnimation;
//import android.view.animation.Animation;
//import android.view.animation.Animation.AnimationListener;
//import android.widget.ScrollView;
//import android.widget.TextView;
//
///**
// * 会自动透明的顶部条
// * @author jie.yang
// *
// */
//public class AgilityBarActivity extends FragmentActivity {
//
//	public static final String INTENT_TITLE_KEY = "title";
//	public static final String INTENT_SHOW_MENU_KEY = "show_menu";
//	public static final String INTENT_FRAGMENT_INDEX_KEY = "fragment_index";
//
////	public final static int FRAGMENT_NEARBY_DETAIL = 2; // NearbyDetailFragment标记。
////	public final static int FRAGMENT_JOB_DETAILS = 1; // JobDetailsFragment标记。
////	public final static int INTENT_FRAGMENT_CONTENT_LAYOUT = 1; // NearbyDetailFragment标记。
//
//	private TopBarLayout topBar;
//	private View topBarRoot;
//	private PullToRefreshScrollView mScrollView;
//	private View topBarBG;
//	private TextView mTitle;
//
//	private int colorFrom = Color.parseColor("#ffba1e");
//	private int colorTo = Color.parseColor("#00000000");
//	private int colorDefault = Color.parseColor("#00000000");
//
//	private int goldScale;
//
//	protected boolean goHide = false;
//	protected boolean goShow = true;
//
//	@Override
//	protected void onCreate(Bundle arg0) {
//		super.onCreate(arg0);
//		setContentView(R.layout.activity_agility_bar);
//		initView();
//		initAgilityScrollView();
//		installFragment(getIntent().getIntExtra(INTENT_FRAGMENT_INDEX_KEY, -1));
//	}
//
//	public TopBarLayout getTopBar() {
//		return topBar;
//	}
//
//	private void initView() {
//		// 初始化顶部条
//		topBarRoot = findViewById(R.id.agility_top_bar);
//		topBar = new TopBarLayout(topBarRoot);
//		topBar.setBackgroundColor(colorDefault);
//
//		topBar.showOnlyBackJoint(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				AgilityBarActivity.this.setResult(Activity.RESULT_CANCELED);
//				AgilityBarActivity.this.finish();
//			}
//		});
//
//		TextView title = (TextView) topBarRoot.findViewById(R.id.NavigateTitle);
//		title.setText(getIntent().getStringExtra(INTENT_TITLE_KEY));
//
//		topBarBG = findViewById(R.id.agility_top_bar_bg);
//		mScrollView = (PullToRefreshScrollView) findViewById(R.id.agility_scrollView);
//		mScrollView.setMode(Mode.PULL_FROM_END);
//
//		mScrollView.setOnRefreshListener(new OnRefreshListener<ScrollView>() {
//
//			@Override
//			public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
//				if(mListener != null)
//					mListener.onRefresh(refreshView);
//			}
//		});
//
//		mTitle = topBar.getTopBarTitle();
//		mTitle.setVisibility(View.GONE);
//	}
//
//	/**
//	 * 初始化ScrollView事件监听
//	 */
//	private void initAgilityScrollView() {
//		goldScale = DensityUtil.dip2px(AgilityBarActivity.this, 135);
////		topBar.hideTopBarTitle();
//		mScrollView.setScrollViewListener(new PullToRefreshScrollView.ScrollViewListener() {
//
//			@Override
//			public void onScrollChanged(int l, int t, int oldl, int oldt) {
////				Log.e("!!!!!", String.valueOf(l) + "  " + String.valueOf(t) + "  "
////						+ String.valueOf(oldl) + "  " + String.valueOf(oldt));
//				if(t < goldScale) {
////					topBar.setBackgroundColor(Color.parseColor("#ffba1e"));
//					if(goHide) {
//						Animation alphaAnimation = new AlphaAnimation(1f, 0f);
//						alphaAnimation.setDuration(300);
//						alphaAnimation.setAnimationListener(new AnimationListener() {
//
//							@Override
//							public void onAnimationStart(Animation animation) {
//								topBarBG.setBackgroundColor(colorFrom);
//								goShow = true;
//								goHide = false;
//							}
//
//							@Override
//							public void onAnimationRepeat(Animation animation) {
//
//							}
//
//							@Override
//							public void onAnimationEnd(Animation animation) {
//								topBarBG.setBackgroundColor(colorTo);
////								topBar.hideTopBarTitle();
//							}
//						});
//						topBarBG.startAnimation(alphaAnimation);
//
//						Animation alphaAnimation1 = new AlphaAnimation(1f, 0f);
//						alphaAnimation1.setDuration(300);
//						alphaAnimation1.setAnimationListener(new AnimationListener() {
//
//							@Override
//							public void onAnimationStart(Animation animation) {
//								mTitle.setVisibility(View.VISIBLE);
//							}
//
//							@Override
//							public void onAnimationRepeat(Animation animation) {
//
//							}
//
//							@Override
//							public void onAnimationEnd(Animation animation) {
//								mTitle.setVisibility(View.GONE);
//							}
//						});
//						mTitle.startAnimation(alphaAnimation1);
//					}
//				}
//				else {
////					topBar.setBackgroundColor(Color.parseColor("#00000000"));
//					if(goShow) {
//						Animation alphaAnimation = new AlphaAnimation(0f, 1f);
//						alphaAnimation.setDuration(300);
//						alphaAnimation.setAnimationListener(new AnimationListener() {
//
//							@Override
//							public void onAnimationStart(Animation animation) {
//								topBarBG.setBackgroundColor(colorFrom);
//								goShow = false;
//								goHide = true;
//							}
//
//							@Override
//							public void onAnimationRepeat(Animation animation) {
//
//							}
//
//							@Override
//							public void onAnimationEnd(Animation animation) {
//								topBarBG.setBackgroundColor(colorFrom);
////								topBar.showTopBarTitle();
//							}
//						});
//						topBarBG.startAnimation(alphaAnimation);
//
//						Animation alphaAnimation1 = new AlphaAnimation(0f, 1f);
//						alphaAnimation1.setDuration(300);
//						alphaAnimation1.setAnimationListener(new AnimationListener() {
//
//							@Override
//							public void onAnimationStart(Animation animation) {
//								mTitle.setVisibility(View.VISIBLE);
//							}
//
//							@Override
//							public void onAnimationRepeat(Animation animation) {
//
//							}
//
//							@Override
//							public void onAnimationEnd(Animation animation) {
//								mTitle.setVisibility(View.VISIBLE);
//							}
//						});
//						mTitle.startAnimation(alphaAnimation1);
//					}
//				}
//			}
//		});
//	}
//
//	protected void installFragment(int intdex) {
//	}
//
//	public TopBarLayout getTopBarLayout() {
//		return topBar;
//	}
//
//	protected void replaceFragment(Fragment fragment) {
//		FragmentManager manager = AgilityBarActivity.this.getSupportFragmentManager();
//		FragmentTransaction ft = manager.beginTransaction();
//		ft.add(R.id.agility_container, fragment);
//		ft.commitAllowingStateLoss();
//		manager.executePendingTransactions();
//	}
//
//	private RefreshListener mListener;
//
//	public static interface RefreshListener {
//		public void onRefresh(PullToRefreshBase<ScrollView> refreshView);
//	}
//}
