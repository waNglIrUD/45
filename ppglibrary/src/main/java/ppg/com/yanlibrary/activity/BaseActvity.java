package ppg.com.yanlibrary.activity;

import android.support.v4.app.FragmentActivity;

/**
 * hahaYJ
 */
public class BaseActvity extends FragmentActivity {

	protected KeyDownProxy keyDownProxy;

    private boolean isDestroy = false;

    public void setKeyDownProxy(KeyDownProxy keyDownProxy) {
		this.keyDownProxy = keyDownProxy;
	}

    public boolean isDestroy() {
        return isDestroy;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isDestroy = true;
    }
}
