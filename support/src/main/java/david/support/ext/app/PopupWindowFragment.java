package david.support.ext.app;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.PopupWindow;


/**
 * @author dingwei.chen
 * @mail dingwei.chen1988@gmail.com
 */
public abstract class PopupWindowFragment extends Fragment {


    private static final String SAVED_ANIM_STYLE = "android:anim_style";
    private static final String SAVED_GRAVITY = "android:gravity";
    private static final String SAVED_BACK_STACK_ID = "android:backStackId";
    private static final String SAVED_ISSHOW = "android:isshowing";
    private static final String SAVED_WIDTH = "android:width";
    private static final String SAVED_HEIGHT = "android:height";
    private static final String SAVED_CONTENT_WIDTH = "android:content_width";
    private static final String SAVED_CONTENT_HEIGHT = "android:content_height";
    private static final String SAVED_BACKGROUND_RESOURCE_ID = "android:background_id";
    private static final String SAVED_BACKGROUND_COLOR = "android:background_color";
    private static final String SAVED_WINDOW_GRAVITY = "android:window_gravity";


    private boolean mIsShowing = false;

    private PopupWindow mPopupWindow = null;
    private View mInnerView = null;
    private int mAnimStyle = ANIM_INPUTMETHOD;
    private int mGravity = Gravity.BOTTOM;
    private int mWindowGravity = Gravity.CENTER;
    private int mBackStackId = 1;
    private int mBackgroundResourceId = -1;
    private int mBackgroundColor = -1;
    private int mWidth = WindowManager.LayoutParams.MATCH_PARENT;
    private int mHeight = WindowManager.LayoutParams.MATCH_PARENT;
    private int mContentWidth = WindowManager.LayoutParams.MATCH_PARENT;
    private int mContentHeight = WindowManager.LayoutParams.WRAP_CONTENT;
    private ShowPopWindowAction mShowPopWindowAction = new ShowPopWindowAction();
    public static final int ANIM_INPUTMETHOD = android.R.style.Animation_InputMethod;
    public static final int ANIM_TOAST = android.R.style.Animation_Toast;
    public static final int ANIM_ACTIVITY = android.R.style.Animation_Activity;
    public static final int ANIM_DIALOG = android.R.style.Animation_Dialog;
    private FrameLayout.LayoutParams mInnerViewLayoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    private Drawable mBackgroundDrawable = null;
    private PopupWindowContainer mPopupWindowContainer = null;
    private ColorDrawable mBackgroundColorDrawable = null;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mAnimStyle = savedInstanceState.getInt(SAVED_ANIM_STYLE, ANIM_INPUTMETHOD);
            mGravity = savedInstanceState.getInt(SAVED_GRAVITY, Gravity.BOTTOM);
            mWindowGravity = savedInstanceState.getInt(SAVED_WINDOW_GRAVITY,Gravity.CENTER);
            mBackStackId = savedInstanceState.getInt(SAVED_BACK_STACK_ID, -1);
            mIsShowing = savedInstanceState.getBoolean(SAVED_ISSHOW, false);
            mWidth = savedInstanceState.getInt(SAVED_WIDTH, WindowManager.LayoutParams.MATCH_PARENT);
            mHeight = savedInstanceState.getInt(SAVED_HEIGHT, WindowManager.LayoutParams.WRAP_CONTENT);
            mBackgroundResourceId = savedInstanceState.getInt(SAVED_BACKGROUND_RESOURCE_ID,-1);
            mBackgroundColor = savedInstanceState.getInt(SAVED_BACKGROUND_COLOR,-1);
            mContentHeight = savedInstanceState.getInt(SAVED_CONTENT_HEIGHT, WindowManager.LayoutParams.WRAP_CONTENT);
            mContentWidth = savedInstanceState.getInt(SAVED_CONTENT_WIDTH, WindowManager.LayoutParams.MATCH_PARENT);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (outState != null && mPopupWindow != null) {
            outState.putInt(SAVED_BACK_STACK_ID, this.mBackStackId);
            outState.putInt(SAVED_GRAVITY, this.mGravity);
            outState.putInt(SAVED_ANIM_STYLE, this.mAnimStyle);
            outState.putBoolean(SAVED_ISSHOW, this.mIsShowing);
            outState.putInt(SAVED_HEIGHT, this.mHeight);
            outState.putInt(SAVED_WIDTH,this.mWidth);

            outState.putInt(SAVED_BACKGROUND_RESOURCE_ID,this.mBackgroundResourceId);
            outState.putInt(SAVED_BACKGROUND_COLOR,this.mBackgroundColor);
            outState.putInt(SAVED_CONTENT_WIDTH,this.mContentWidth);
            outState.putInt(SAVED_CONTENT_HEIGHT,this.mContentHeight);
            outState.putInt(SAVED_WINDOW_GRAVITY,this.mWindowGravity);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        dismissInternal(true);
        mPopupWindow = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mPopupWindow != null && !mPopupWindow.isShowing()) {
             if (this.getActivity().getWindow().getDecorView().getWindowToken() == null) {
                mShowPopWindowAction.start();
            } else {
                mShowPopWindowAction.run();
            }
        }
    }

    private class ShowPopWindowAction implements Runnable {


        public Handler handler = new Handler(Looper.getMainLooper());

        public void start() {
            handler.removeCallbacks(this);
            handler.postDelayed(this,50);
        }

        public void cancel() {
            handler.removeCallbacks(this);
        }

        @Override
        public void run() {
            if (mPopupWindow != null && mIsShowing && !mPopupWindow.isShowing()) {
                preparePopup();
                mPopupWindow.showAtLocation(getActivity().getWindow().getDecorView(),
                        (mBackgroundDrawable == null?mGravity:mWindowGravity), 0, 0);
            }
        }
    }


    protected PopupWindow onCreatePopupWindow() {
        return new PopupWindow() {
            @Override
            public void dismiss() {
                super.dismiss();
                dismissInternal(true);
            }
        };
    }


    public final PopupWindow getPopupWindow() {
        return this.mPopupWindow;
    }


    public  final PopupWindowFragment requestGravity(int gravity) {
        if (this.mGravity != gravity) {
            this.mGravity = gravity;
            mInnerViewLayoutParams.gravity = this.mGravity;
            if (mInnerView != null) {
                mInnerView.setLayoutParams(mInnerViewLayoutParams);
            }
        }
        return this;
    }


    private PopupWindow getOrCreatePopupWindow() {
        if (mPopupWindow == null) {
            mPopupWindow = this.onCreatePopupWindow();
            mPopupWindow.setOutsideTouchable(false);
            mPopupWindow.setFocusable(true);
            mPopupWindow.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
            mPopupWindow.setHeight(WindowManager.LayoutParams.MATCH_PARENT);
        }
        return mPopupWindow;
    }

    private void preparePopup() {
        if(mPopupWindowContainer != null) {
            requestBackgroundColor(mBackgroundColor);
            requestBackgroundResource(mBackgroundResourceId);
            mPopupWindowContainer.setBackground(mBackgroundDrawable);
            requestPopupAnimationStyle(mAnimStyle);
            mInnerViewLayoutParams.gravity = mGravity;
            requestPopupWindowWidth(WindowManager.LayoutParams.MATCH_PARENT);
            if(mBackgroundDrawable != null) {
                requestPopupWindowHeight(WindowManager.LayoutParams.MATCH_PARENT);
            } else {
                requestPopupWindowHeight(WindowManager.LayoutParams.WRAP_CONTENT);
            }
            mInnerViewLayoutParams.width = mContentWidth;
            mInnerViewLayoutParams.height = mContentHeight;
            mPopupWindowContainer.removeAllViewsInLayout();
            mPopupWindowContainer.addView(mInnerView,mInnerViewLayoutParams);
        }
    }

    public PopupWindowFragment requestWindowGravity(int windowGravity) {
        this.mWindowGravity = windowGravity;
        return this;
    }

    public PopupWindowFragment requestBackground(Drawable background) {
        this.mBackgroundDrawable = background;
        if (mPopupWindowContainer != null) {
            mPopupWindowContainer.setBackground(background);
        }
        return this;
    }

    public final PopupWindowFragment requestBackgroundResource(int backgroundID) {
        this.mBackgroundResourceId = backgroundID;
        if (this.mBackgroundResourceId < 0) {
            if (this.mBackgroundDrawable == null) {
                this.mBackgroundDrawable = mBackgroundColorDrawable;
            }
        } else {
            final Resources r = mPopupWindowContainer.getResources();
            this.mBackgroundDrawable = r.getDrawable(backgroundID);
        }
        if (mPopupWindowContainer != null) {
            mPopupWindowContainer.setBackground(mBackgroundDrawable);
        }
        return this;
    }

    public final PopupWindowFragment requestBackgroundColor(int color) {
        if (this.mBackgroundColor == color) {
            return this;
        }
        this.mBackgroundColor = color;
        if (this.mBackgroundColor != - 1) {
            this.mBackgroundColorDrawable = new ColorDrawable(color);
            if (mBackgroundDrawable == null) {
                mBackgroundDrawable = this.mBackgroundColorDrawable;
                if (mPopupWindowContainer != null) {
                    mPopupWindowContainer.setBackground(mBackgroundDrawable);
                }
            }
        } else {
            if (this.mBackgroundDrawable == this.mBackgroundColorDrawable) {
                this.mBackgroundDrawable = null;
                if (mPopupWindowContainer != null) {
                    mPopupWindowContainer.setBackground(null);
                }
            }
            mBackgroundColorDrawable = null;

        }
        return this;
    }


    @Nullable
    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final PopupWindow window = this.getOrCreatePopupWindow();
        if (mPopupWindowContainer == null) {
            mPopupWindowContainer = new PopupWindowContainer(getActivity());
        }
        if (mInnerView == null) {
            mInnerView = this.onCreateView(inflater, savedInstanceState);
        }
        window.setContentView(mPopupWindowContainer);
        mPopupWindow.setContentView(mPopupWindowContainer);
        return mPopupWindowContainer;
    }

    public PopupWindowFragment requestPopupAnimationStyle(int style) {
        this.mAnimStyle = style;
        if (mPopupWindow != null) {
            mPopupWindow.setAnimationStyle(mAnimStyle);
        }
        return this;
    }

    protected final PopupWindowFragment requestPopupWindowWidth(int width) {
        this.mWidth = width;
        if (mPopupWindow != null) {
            mPopupWindow.setWidth(mWidth);
        }
        return this;
    }

    protected final PopupWindowFragment requestPopupWindowHeight(int height) {
        this.mHeight = height;
        if (mPopupWindow != null) {
            mPopupWindow.setHeight(height);
        }
        return this;
    }

    @Nullable
    @Override
    public View getView() {
        return mInnerView;
    }

    protected abstract View onCreateView(LayoutInflater inflater, Bundle savedInstanceState);


    public final void show(FragmentManager fm,String tag) {
        if (mIsShowing) {
            return ;
        }
        mIsShowing = true;
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(this,tag);
        mBackStackId = ft.commitAllowingStateLoss();
        fm.executePendingTransactions();
    }

    public final void show(FragmentTransaction ft,String tag) {
        if (mIsShowing) {
            return;
        }
        mIsShowing = true;
        ft.add(this, tag);
        mBackStackId = ft.commitAllowingStateLoss();
        this.getFragmentManager().executePendingTransactions();
    }


    public final void dismiss() {
        dismissInternal(false);
    }


    public final void dismissAllowingStateLoss() {
        dismissInternal(true);
    }

    void dismissInternal(boolean allowStateLoss) {
        if (!mIsShowing) {
            return;
        }
        mIsShowing = false;
        mShowPopWindowAction.cancel();
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }

        if (this.getActivity() == null) {
            return;
        }

        if (getActivity().isFinishing()) {
            return;
        }

        if (getActivity().isDestroyed()) {
            return;
        }

        if (mBackStackId >= 0) {
            getFragmentManager().popBackStackImmediate(mBackStackId,
                    FragmentManager.POP_BACK_STACK_INCLUSIVE);
            mBackStackId = -1;
        } else {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.remove(this);
            if (allowStateLoss) {
                ft.commitAllowingStateLoss();
            } else {
                ft.commit();
            }
            getFragmentManager().executePendingTransactions();
        }
    }


    private class PopupWindowContainer extends FrameLayout {

        public PopupWindowContainer(Context context) {
            super(context);
        }

        @Override
        public LayoutParams generateLayoutParams(AttributeSet attrs) {
            return mInnerViewLayoutParams;
        }

        @Override
        protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
            return mInnerViewLayoutParams;
        }


        @Override
        public boolean onTouchEvent(MotionEvent event) {
            dismissAllowingStateLoss();
            return true;
        }
    }

}
