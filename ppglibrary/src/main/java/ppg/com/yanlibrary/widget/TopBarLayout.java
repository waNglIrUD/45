package ppg.com.yanlibrary.widget;

import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ppg.com.yanlibrary.R;
import ppg.com.yanlibrary.utils.SupportUtil_SDK21;


/**
 * 
 * @author jie.yang
 *
 */
public class TopBarLayout {

//	private RelativeLayout mContainer;
	private View mRoot;
	private View mSelectFrame1;
	private View mSelectFrame2;
	private View mPrePanel;
	
	View viewR1;
	View viewR2;
	View viewL1;
	View viewL2;
	View viewR3;
    View viewR4;
	View viewSearch;
    View Select;
public View getR1()
{

    return viewR1;
}
    public View getviewSearch()
    {

        return viewSearch;
    }
    public View getR4()
    {

        return viewR4;
    }
    public View getR3()
    {

        return viewR3;
    }
    public void hideBack()
    {
        viewL1.setVisibility(View.GONE);

    }
	public View  getRoot()
	{
		return mRoot;
	}
	public TopBarLayout(View root) {
		mRoot = root;

                viewSearch = mRoot.findViewById(R.id.search_detail_edittext);
		viewR1 = mRoot.findViewById(R.id.NavigateOperation_R1);
		viewR2 = mRoot.findViewById(R.id.NavigateOperation_R2);
        viewR4 = mRoot.findViewById(R.id.NavigateOperation_R4);

		viewL1 = mRoot.findViewById(R.id.NavigateOperation_L1);
		viewL2 = mRoot.findViewById(R.id.NavigateOperation_L2);
		viewR3 = mRoot.findViewById(R.id.NavigateOperation_R3);
		Select = mRoot.findViewById(R.id.select_frame);
	}

	public View getViewL2() {
		return viewL2;
	}

	public void setBackgroundColor(int color) {
		mRoot.setBackgroundColor(color);
	}
	
	public void setBackground(int rid) {
		mRoot.setBackgroundResource(rid);
	}
	
	public RelativeLayout getContainer() {
		return (RelativeLayout) mRoot;
	}
	
	/**
	 * 只显示回退按钮
	 * @return
	 */
	public View showOnlyBack(OnClickListener listener) {
		View view = mRoot.findViewById(R.id.NavigateBackLinearLayout);
		view.setVisibility(View.VISIBLE);
		//view.setOnClickListener(listener);
        SupportUtil_SDK21.SupporTouchFeedback(view.getContext(), view, false, R.drawable.top_bar_button, listener);
		return view;
	}
	
	/**
	 * 只显示回退按钮
	 * @return
	 */
	public View showOnlyBackJoint(OnClickListener listener) {
		View view = mRoot.findViewById(R.id.NavigateBackLinearLayout_joint);
		view.setVisibility(View.VISIBLE);
		view.setOnClickListener(listener);
		SupportUtil_SDK21.SupporTouchFeedback(view.getContext(), view, false, R.drawable.top_bar_button, listener);
		return view;
	}
	
//	public EditText showTopEditText() {
//		View view = mRoot.findViewById(R.id.Navigate_edittext);
//		View viewb = mRoot.findViewById(R.id.NavigateTitle);
//		viewb.setVisibility(View.GONE);
//		view.setVisibility(View.VISIBLE);
//		return (EditText) view;
//	}
	
	public View showSelectFrame(OnClickListener listener1, OnClickListener listener2) {
		View view = mRoot.findViewById(R.id.select_frame);
		view.setVisibility(View.VISIBLE);
		View frame1 = mRoot.findViewById(R.id.select_frame_1);
		View frame2 = mRoot.findViewById(R.id.select_frame_2);
		frame1.setOnClickListener(listener1);
		frame2.setOnClickListener(listener2);
		
		TextView title = (TextView) mRoot.findViewById(R.id.NavigateTitle);
		title.setVisibility(View.GONE);
		return view;
	}
	
	public View showSelectFrame(OnClickListener listener1, String txt1, OnClickListener listener2, String txt2) {
		return showSelectFrame(listener1, txt1, Color.WHITE, listener2, txt2, Color.WHITE);
	}
	
	public void hideTopBar() {
		mRoot.setVisibility(View.GONE);
//		TranslateAnimation translate = new TranslateAnimation(0, 0, 0, -DensityUtil.dip2px(mRoot.getContext(), 45));
//		translate.setDuration(1000);
//		mRoot.startAnimation(translate);
	}
	
	public void showTopBar() {
		mRoot.setVisibility(View.VISIBLE);
	}
	
	public View showSelectFrame(OnClickListener listener1, String txt1, int txtColor1, OnClickListener listener2, String txt2, int txtColor2) {
		View view = mRoot.findViewById(R.id.select_frame);

		view.setVisibility(View.VISIBLE);
		View frame1 = mRoot.findViewById(R.id.select_frame_1);
		View frame2 = mRoot.findViewById(R.id.select_frame_2);
		TextView txtV1 = (TextView) frame1.findViewById(R.id.select_frame_1_txt);
		txtV1.setText(txt1);
		txtV1.setTextColor(txtColor1);
		TextView txtV2 = (TextView) frame2.findViewById(R.id.select_frame_2_txt);
		txtV2.setText(txt2);
		txtV2.setTextColor(txtColor2);
		frame1.setOnClickListener(listener1);
		frame2.setOnClickListener(listener2);
		
		TextView title = (TextView) mRoot.findViewById(R.id.NavigateTitle);
		title.setVisibility(View.GONE);
		return view;
	}
	
	public void showSelectFrame1() {
		View frame1 = mRoot.findViewById(R.id.select_frame_1);
		View frame2 = mRoot.findViewById(R.id.select_frame_2);
		frame1.setBackgroundResource(R.color.menu_txt);
		frame2.setBackgroundResource(R.color.transparenthalf);
	}
	
	public View getSelectFrame1() {
		if(mSelectFrame1 == null)
			mSelectFrame1 = mRoot.findViewById(R.id.select_frame_1);
		return mSelectFrame1;
	}
	
	public TextView getSelectFrameTxt1() {
		return (TextView) mRoot.findViewById(R.id.select_frame_1_txt);
	}
	
	public TextView getSelectFrameTxt2() {
		return (TextView) mRoot.findViewById(R.id.select_frame_2_txt);
	}
	
	public View getSelectFrame2() {
		if(mSelectFrame2 == null)
			mSelectFrame2 = mRoot.findViewById(R.id.select_frame_2);
		return mSelectFrame2;
	}
	
	public void showSelectFrame2() {
		View frame1 = mRoot.findViewById(R.id.select_frame_1);
		View frame2 = mRoot.findViewById(R.id.select_frame_2);
		frame2.setBackgroundResource(R.color.menu_txt);
		frame1.setBackgroundResource(R.color.transparenthalf);
	}
	
	public void HideOperation_LR() {
//		View viewR1 = mRoot.findViewById(R.id.NavigateOperation_R1);
//		View viewR2 = mRoot.findViewById(R.id.NavigateOperation_R2);
//		View viewL1 = mRoot.findViewById(R.id.NavigateOperation_L1);
//		View viewL2 = mRoot.findViewById(R.id.NavigateOperation_L2);
//		View viewR3 = mRoot.findViewById(R.id.NavigateOperation_R3);
//		View Select = mRoot.findViewById(R.id.select_frame);
		mRoot.setVisibility(View.VISIBLE);
		viewR3.setVisibility(View.GONE);
		viewR1.setVisibility(View.GONE);
		viewR2.setVisibility(View.GONE);
		viewL1.setVisibility(View.GONE);
		viewL2.setVisibility(View.GONE);
		Select.setVisibility(View.GONE);
		removePrePanel();
	}
	
	public void hideTopBarTitle() {
		TextView view = (TextView) mRoot.findViewById(R.id.NavigateTitle);
		view.setVisibility(View.GONE);
	}
	
	public void showTopBarTitle() {
		TextView view = (TextView) mRoot.findViewById(R.id.NavigateTitle);
		view.setVisibility(View.VISIBLE);
	}
	
	public void setTopBarTitle(String title) {
		TextView view = (TextView) mRoot.findViewById(R.id.NavigateTitle);
		view.setVisibility(View.VISIBLE);
		view.setText(title);
	}
	
	public TextView getTopBarTitle() {
		TextView view = (TextView) mRoot.findViewById(R.id.NavigateTitle);
		return view;
	}
	
	public void setTopBarTitle(int rid) {
		TextView view = (TextView) mRoot.findViewById(R.id.NavigateTitle);
		view.setVisibility(View.VISIBLE);
		view.setText(rid);
	}
	
	/**
	 * 
	 * @return
	 */
	public ImageButton getOperationRightView1(int RID, OnClickListener listener) {
		ImageButton view = (ImageButton) mRoot.findViewById(R.id.NavigateOperation_R1);
		view.setVisibility(View.VISIBLE);
        view.setImageResource(RID);
        SupportUtil_SDK21.SupporTouchFeedback(view.getContext(), view, true, R.drawable.top_bar_button, listener);
		return view;
	}

	/**
	 *
	 * @return
	 */
	public LinearLayout getOperationRightView3( OnClickListener listener) {
		LinearLayout view = (LinearLayout) mRoot.findViewById(R.id.NavigateOperation_R6);
		view.setVisibility(View.VISIBLE);
		//view.setImageResource(RID);
		SupportUtil_SDK21.SupporTouchFeedback(view.getContext(), view, true, R.drawable.top_bar_button, listener);
		return view;
	}


	
	public TextView getOperationLeftView2(String text,OnClickListener listener ) {
		TextView view = (TextView) mRoot.findViewById(R.id.NavigateOperation_L2);
		view.setText(text);
		view.setVisibility(View.VISIBLE);
		SupportUtil_SDK21.SupporTouchFeedback(view.getContext(), view, true, R.drawable.top_bar_button, listener);
		return view;
	}

	public TextView getOperationLeftView2(String text ) {
		TextView view = (TextView) mRoot.findViewById(R.id.NavigateOperation_L2);
		view.setText(text);
		view.setVisibility(View.VISIBLE);
		return view;
	}
	
	public void setTopNavigateBackgroundColor(int color) {
		mRoot.setBackgroundColor(color);
	}
	
	public View getTopNavigateBack() {
		return mRoot;
	}
	
	public ImageView getOperationBack() {
		ImageView view = (ImageView) mRoot.findViewById(R.id.NavigateOperation_L1);
		view.setVisibility(View.VISIBLE);
		return view;
	}

	public ImageView hideOperationBack() {
		ImageView view = (ImageView) mRoot.findViewById(R.id.NavigateOperation_L1);
		view.setVisibility(View.GONE);
		return view;
	}
	
	public ImageButton getOperationRightView2(int RID, OnClickListener listener) {
		ImageButton view = (ImageButton) mRoot.findViewById(R.id.NavigateOperation_R2);
		view.setVisibility(View.VISIBLE);
        view.setImageResource(RID);
        SupportUtil_SDK21.SupporTouchFeedback(view.getContext(), view, true, R.drawable.top_bar_button, listener);
		return view;
	}

	public LinearLayout getOperationRightView5(int RID,String str, OnClickListener listener) {
		LinearLayout linearLayout = (LinearLayout) mRoot.findViewById(R.id.NavigateOperation_R5);
		ImageButton iv_view=(ImageButton)linearLayout.findViewById(R.id.NavigateOperation_R5_Iv);
		iv_view.setVisibility(View.VISIBLE);
		iv_view.setImageResource(RID);
		TextView tv_view=(TextView)linearLayout.findViewById(R.id.NavigateOperation_R5_Tv);
		tv_view.setVisibility(View.VISIBLE);
		tv_view.setText(str);
		SupportUtil_SDK21.SupporTouchFeedback(linearLayout.getContext(), linearLayout, true, R.drawable.top_bar_button, listener);
		return linearLayout;
	}
	
	public TextView getOperationRightView3(String title, OnClickListener listener) {
		TextView view = (TextView) mRoot.findViewById(R.id.NavigateOperation_R3);
		view.setVisibility(View.VISIBLE);
        view.setText(title);
		//水波纹特效,需要的时候去掉注释
        SupportUtil_SDK21.SupporTouchFeedback(view.getContext(), view, true, R.drawable.top_bar_button, listener);
		return view;
	}
	
	public void addPrePanel(View child) {
		if(child != null) {
			mPrePanel = child;
			((RelativeLayout) mRoot).addView(child, new RelativeLayout
					.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT
							, RelativeLayout.LayoutParams.MATCH_PARENT));
		}
	}
	
	public void removePrePanel() {
		if(mPrePanel != null)
			((RelativeLayout) mRoot).removeView(mPrePanel);
	}
}
