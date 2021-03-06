package david.support.ext.menus.views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Property;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

import david.support.ext.debug.Logger;
import david.support.ext.menus.DavidMenu;

/**
 * Created by chendingwei on 16/11/12.
 */

public class MenuPanelLayout extends ViewGroup {

    private DavidMenu mMenu = null;
    private Paint mPaint = new Paint();
    private OnClickImpl mOnClickImpl = new OnClickImpl();
    private DavidMenu.DavidMenuItem mSelectItem = null;
    private MenuPanelLayout mChildPanelLayout = null;
    private IMenusPanelContainer mContainer = null;
    private static final Logger LOG = new Logger(MenuPanelLayout.class);
    private ObjectAnimator mObjectAnimator = null;
    private ObjectAnimator mDismissAnimator = null;
    private static final int ANIMATION_TIME = 250;
    private float mAnimatorValue;
    private AnimatorProperty mProperty = new AnimatorProperty() ;
    public MenuPanelLayout(Context context) {
        super(context);
    }

    public void setContainer(IMenusPanelContainer container) {
        mContainer = container;
    }

    @Override
    protected void onLayout(boolean changed,int l, int t, int r, int b) {
        t = this.getPaddingTop();
        l = this.getPaddingLeft();
        int index = 0;
        View view = null;
        final int count = this.getChildCount();
        while (index < count) {
            view = this.getChildAt(index);
            view.layout(l,t,l + view.getMeasuredWidth(),t + view.getMeasuredHeight());
            t = view.getBottom()+mMenu.getDividerHeight();
            index ++;
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {

        if (mSelectItem != null && mSelectItem.subMenu != null && mSelectItem.getView() != null) {
            canvas.save();
            View view = mSelectItem.getView();
            Rect temp = new Rect();
            mPaint.setColor(mMenu.getSelectColor());
            temp.left = 0;
            temp.right = getWidth();
            temp.top = view.getTop();
            temp.bottom = view.getBottom();
            canvas.drawRect(temp,mPaint);
            canvas.restore();
        }

        super.dispatchDraw(canvas);
        if (this.mMenu != null && this.getChildCount() > 0) {
            int h = this.mMenu.getDividerHeight();
            canvas.save();
            if (h > 0) {
                Rect temp = new Rect();
                temp.left = this.getPaddingLeft();
                temp.right = temp.right + mMenu.getItemWidth();
                View view = null;
                final int count = this.getChildCount();
                for (int index = 0; index < count-1; index ++) {
                    view = this.getChildAt(index);
                    temp.top = view.getBottom();
                    temp.bottom = temp.top + h;
                    canvas.drawRect(temp,mPaint);

                }
            }
            canvas.restore();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int paddingWidth =this.getPaddingLeft() + this.getPaddingRight();
        int paddingHeight = this.getPaddingTop() + this.getPaddingBottom();
        int itemWidth = this.mMenu.getItemWidth();
        int itemHeight = this.mMenu.getItemHeight();
        int dividerHeight = this.mMenu.getDividerHeight();
        int width = itemWidth + paddingWidth;
        final int count = this.getChildCount();
        int height = (itemHeight * count) + paddingHeight + (count - 1)*dividerHeight;
        this.setMeasuredDimension(width,height);
        int index = count - 1;
        View view = null;
        while (index >= 0) {
            view = this.getChildAt(index);
            view.measure(MeasureSpec.EXACTLY|itemWidth,MeasureSpec.EXACTLY|itemHeight);
            index -- ;
        }
    }




    public void setMenu(DavidMenu menu) {
        this.removeAllViewsInLayout();
        mMenu = menu;
        mPaint.setColor(mMenu.getDividerColor());
        this.prepareSelf();
        this.prepareChildren();
    }

    private void prepareSelf() {
        if (this.mMenu.getBackgroundId() != -1) {
            this.setBackgroundResource(this.mMenu.getBackgroundId());
        } else {
            this.setBackgroundColor(this.mMenu.getBackgroundColor());
        }
        this.setPadding(this.mMenu.getPaddingLeft(),this.mMenu.getPaddingTop(),this.mMenu.getPaddingRight(),this.mMenu.getPaddingBottom());
    }


    private void prepareChildren() {
        final int count = mMenu.size();
        DavidMenu.DavidMenuItem item = null;
        MenuItemLayout layout = null;
        for (int index = 0; index < count ;index ++) {
            item = this.mMenu.getItem(index);
            layout = new MenuItemLayout(this.getContext());
            layout.setItem(item,this.mMenu);
            layout.setOnClickListener(mOnClickImpl);
            this.addView(layout);
        }
    }


    private class OnClickImpl implements OnClickListener {
        @Override
        public void onClick(View view) {
            if (mSelectItem != null) {
                if (mSelectItem.getView() == view) {
                    return;
                } else {
                    performUnSelect();
                    performSelect(view);
                }
            } else {
                performSelect(view);
            }
        }
    }

    public void performUnSelect() {
        if (mSelectItem != null) {
            mSelectItem.getView().setSelected(false);
        }
        if (mChildPanelLayout != null) {
            mChildPanelLayout.dismiss();
        }
        mChildPanelLayout = null;
        mSelectItem = null;
    }

    private void performSelect(View view) {
        view.setSelected(true);
        int index = getViewIndex(view);
        DavidMenu.DavidMenuItem item = this.mMenu.getItem(index);
        if (item.subMenu != null && mContainer != null) {
            MenuPanelLayout layout = mContainer.addPanel(this,item.subMenu,item);
            this.mChildPanelLayout = layout;
        }
        mContainer.performItemClick(this.mMenu,item);
        mSelectItem = item;
        this.invalidate();
    }

    private int getViewIndex(View view) {
        int index = this.getChildCount() - 1;
        while (index >= 0) {
            if (view == getChildAt(index)) {
                return index;
            }
            index --;
        }
        return -1;
    }

    public void dismiss() {
        this.dismiss(mChildPanelLayout == null);
    }

    public void dismiss(boolean startAnimator) {
        if (mObjectAnimator != null) {
            mObjectAnimator.cancel();
        }
        this.performUnSelect();
        if (!isExitAnimator() || !startAnimator) {
            if (mContainer != null) {
                this.mContainer.removePanel(this);
            }
        } else {
            dismissAnimation();
        }

    }

    private void dismissAnimation() {
        if (mDismissAnimator == null) {
            mDismissAnimator = new ObjectAnimator();
            mDismissAnimator.setProperty(mProperty);
            mDismissAnimator.setTarget(this);
            mDismissAnimator.setDuration(ANIMATION_TIME);
            mDismissAnimator.addListener(new DismissListenerImpl());
        }
        mDismissAnimator.cancel();
        mDismissAnimator.setFloatValues(mAnimatorValue,0);
        mDismissAnimator.start();
    }

    private class DismissListenerImpl extends AnimatorListenerAdapter {
        @Override
        public void onAnimationEnd(Animator animation) {
            if (mContainer != null) {
                mContainer.removePanel(MenuPanelLayout.this);
            }
        }
    }


    private void cancelDismiss() {
        if (mDismissAnimator != null) {
            mDismissAnimator.cancel();
        }
    }


    public void showAnimation() {
        cancelDismiss();
        if (mObjectAnimator == null) {
            mObjectAnimator = new ObjectAnimator();
            mObjectAnimator.setFloatValues(0,1);
            mObjectAnimator.setTarget(this);
            mObjectAnimator.setDuration(ANIMATION_TIME);
            mObjectAnimator.setStartDelay(30);
            mObjectAnimator.setTarget(this);
            mObjectAnimator.setProperty(mProperty);
            mObjectAnimator.setInterpolator(new DecelerateInterpolator());
        }
        mObjectAnimator.cancel();
        if (isEnterAnimator()) {
            mAnimatorValue = 0;
            beginEnterAnimator();
        } else {
            mAnimatorValue = 1;
        }
    }


    private void beginEnterAnimator() {
        int enterAnim = mMenu.getEnterAnim();
        if (isMask(enterAnim,DavidMenu.ANIM_SCALE)) {
            setScale(0);
        }
        if (isMask(enterAnim,DavidMenu.ANIM_TRANSLATE)) {
            setTranslate(0);
        }
        if (isMask(enterAnim,DavidMenu.ANIM_FADE)) {
            setFade(0);
        }
        mObjectAnimator.start();
    }

    private class AnimatorProperty extends Property<MenuPanelLayout,Float> {

        public AnimatorProperty() {
            super(Float.class, "MenuPanelLayout");
        }

        @Override
        public Float get(MenuPanelLayout object) {
            return 1f;
        }

        @Override
        public void set(MenuPanelLayout object, Float value) {
            setAnimator(value);
        }
    }

    private boolean isEnterAnimator() {
        return this.mMenu.getEnterAnim() != DavidMenu.ANIM_NONE;
    }

    private boolean isExitAnimator() {
        return this.mMenu.getExitAnim() != DavidMenu.ANIM_NONE;
    }

    private void setAnimator(float v) {
        int enterAnim = mMenu.getEnterAnim();
        if (isMask(enterAnim,DavidMenu.ANIM_SCALE)) {
            setScale(v);
        }
        if (isMask(enterAnim,DavidMenu.ANIM_TRANSLATE)) {
            setTranslate(v);
        }
        if (isMask(enterAnim,DavidMenu.ANIM_FADE)) {
            setFade(v);
        }
     }

    private boolean isMask(int m,int mask) {
        return (m & mask) != 0;
    }

    private void setFade(float v) {
        this.setAlpha(v);
    }

    private void setTranslate(float v) {
        v = v - 1;
        int offsetX = (int)(this.getWidth() * v);
        this.setTranslationX(offsetX);
    }

    private void setScale(float v) {
        this.setScaleY(v);
        this.setScaleX(v);
        mAnimatorValue = v;
    }



}
