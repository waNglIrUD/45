package ppg.com.yanlibrary.widget.dialog;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;

import ppg.com.yanlibrary.R;
import ppg.com.yanlibrary.activity.BaseActvity;
import ppg.com.yanlibrary.activity.KeyDownProxy;
import ppg.com.yanlibrary.utils.DensityUtil;


/**
 * 
 * @author jie.yang
 *
 */
public class EffectDialog extends FrameLayout implements KeyDownProxy {

	private ViewGroup root;
	private int mW;
	private int mH;
	private FrameLayout mFrameBG;
	private FrameLayout mFrame;
	
	private boolean isShow = false;
	private int mDuration = 200;
	protected BaseActvity baseActvity;
	
	public EffectDialog(BaseActvity context) {
		super(context);
		baseActvity = context;
		baseActvity.setKeyDownProxy(this);
		root = ((ViewGroup)((Activity) context).getWindow().getDecorView());
	}
	
	public EffectDialog(Context context, AttributeSet attrs) {
		super(context, attrs);
		root = ((ViewGroup)((Activity) context).getWindow().getDecorView());
	}
	
	private void initFrame() {
		// 设置背景框架
		final FrameLayout frameBG = mFrameBG = new FrameLayout(getContext());
		LayoutParams flpBG= new LayoutParams
				(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		frameBG.setBackgroundColor(Color.parseColor("#44000000"));
		addView(frameBG, flpBG);
		frameBG.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				exitAnimation();
                frameBG.setClickable(false);
			}
		});
		onPaddingBackground(frameBG);
		
		// 设置中间框架
		mW = getDipW();
		mH = getDipH();
		FrameLayout frame = mFrame = new FrameLayout(getContext());
		LayoutParams flp = new LayoutParams(mW, mH);
		frame.setBackgroundResource(R.drawable.dialog_background);
		flp.gravity = Gravity.CENTER;
		addView(frame, flp);
		onPaddingFrame(frame);
		
		root.addView(this);
	}
	
	public void show() {
		isShow = true;
		initFrame();
		enterAnimation();
	}
	
	public void close() {
		isShow = false;
		exitAnimation();
	}
	
	protected void destroy() {
		mFrame.setVisibility(View.GONE);
		mFrameBG.clearAnimation();
		root.removeView(EffectDialog.this);
		baseActvity.setKeyDownProxy(null);
	}
	
	protected void onPaddingFrame(FrameLayout frame) {
		
	}
	
	protected void onPaddingBackground(FrameLayout frame) {
		
	}
	
	protected void enterAnimation() {
		AlphaAnimation alpha = new AlphaAnimation(0, 1);
		alpha.setDuration(mDuration);
		mFrameBG.startAnimation(alpha);
		alpha.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				mFrameBG.clearAnimation();
				mFrame.clearAnimation();
			}
		});
		
//		TranslateAnimation translate = new TranslateAnimation(0, 0, -GlobalVar.screenHeight, 0);
//		translate.setDuration(mDuration);
//		mFrame.startAnimation(translate);

        ScaleAnimation scale = new ScaleAnimation(0, 1, 0, 1,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scale.setDuration(mDuration);
        mFrame.startAnimation(scale);
	}
	
	protected void exitAnimation() {
		AlphaAnimation alpha = new AlphaAnimation(1, 0);
		alpha.setDuration(mDuration * 2);
		mFrameBG.startAnimation(alpha);
		alpha.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				destroy();
			}
		});
		
//		TranslateAnimation translate = new TranslateAnimation(0, 0, 0, GlobalVar.screenHeight);
//		translate.setDuration(mDuration * 2);
//		mFrame.startAnimation(translate);

        ScaleAnimation scale = new ScaleAnimation(1, 0, 1, 0,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scale.setDuration(mDuration);
        scale.setFillAfter(true);
        mFrame.startAnimation(scale);
	}
	
	protected int getDipW() {
		return DensityUtil.dip2px(getContext(), 270);
	}
	
	protected int getDipH() {
		return DensityUtil.dip2px(getContext(), 250);
	}

//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		if(isShow)
//			close();
//		return true;
//	}
}
