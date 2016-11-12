package david.support.ext.menus;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.util.Property;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import david.support.ext.debug.Logger;
import david.support.ext.menus.views.MenusContainerLayout;
import david.support.ext.widget.RelativePopupWindow;

/**
 * Created by chendingwei on 16/11/12.
 */

public class DavidMenuPopupWindow extends RelativePopupWindow {

    private MenusContainerLayout mContainerLayout = null;
    private int mMenuResourceId = -1;
    private static final int ANIM_TIME = 300;
    private ObjectAnimator mObjectAnimator = null;
    private static final Logger  LOG = new Logger(DavidMenuPopupWindow.class);
    private boolean mIsBlankClick = false;

    private class ScaleProperty extends Property<DavidMenuPopupWindow,Float> {

        public ScaleProperty() {
            super(Float.class, "WaiterPopupWindow");
        }

        @Override
        public Float get(DavidMenuPopupWindow object) {
            return 1f;
        }

        @Override
        public void set(DavidMenuPopupWindow object, Float value) {
            setScale(value);
        }
    }

    private void setScale(float v) {
        if (this.mContainerLayout != null) {
            this.mContainerLayout.setScaleX(v);
            this.mContainerLayout.setScaleY(v);
        }
    }

    public DavidMenuPopupWindow(Activity context,int menuResourceId) {
        super(context);
        this.mMenuResourceId = menuResourceId;
    }


    @Override
    protected void onShow(View view, int viewLeft, int viewTop, int width, int height) {
        if (mContainerLayout == null) {
            this.mContainerLayout = new MenusContainerLayout(this.getContext());
            this.mContainerLayout.setMenuResource(this.mMenuResourceId);
            this.addViewToContent(this.mContainerLayout);
            mObjectAnimator = new ObjectAnimator();
            mObjectAnimator.setTarget(this);
            mObjectAnimator.setDuration(ANIM_TIME);
            mObjectAnimator.setFloatValues(0,1);
            mObjectAnimator.setProperty(new ScaleProperty());
            mObjectAnimator.setInterpolator(new DecelerateInterpolator());
            mObjectAnimator.setStartDelay(50);
        }
        this.setScale(0);
        mObjectAnimator.cancel();
        mObjectAnimator.start();
        this.mContainerLayout.setPivotX(0);
        this.mContainerLayout.setPivotY(viewTop + height);
        //int viewLeft, int viewTop, int width, int height
        super.onShow(view, viewLeft+width, 0, getScreenWidth() - viewLeft - width, viewTop + height);
    }

    @Override
    public void dismiss() {
        if (!mIsBlankClick) {
            if (mContainerLayout != null) {
                if (!mContainerLayout.pop()) {
                    super.dismiss();
                }
            } else {
                super.dismiss();
            }
        } else {
            super.dismiss();
        }
        mIsBlankClick = false;
    }

    @Override
    protected void onBlankClick() {
        mIsBlankClick = true;
        if (mContainerLayout != null) {
            mObjectAnimator.cancel();
            mContainerLayout.dismiss();
        }
        super.onBlankClick();
    }
}
