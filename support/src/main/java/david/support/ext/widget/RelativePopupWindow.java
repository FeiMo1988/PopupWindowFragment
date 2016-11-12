package david.support.ext.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.PopupWindow;

/**
 * Created by chendingwei on 16/11/3.
 */
public abstract class RelativePopupWindow extends PopupWindow {


    private WindowManager mWindowManager = null;
    private Activity mContext;
    private Rect mTmpRect = new Rect();
    private static final int GRAVITY = Gravity.LEFT| Gravity.TOP;
    private DisplayMetrics mMetrics = new DisplayMetrics();
    private PreHolderFrameLayout mContentLayout ;
    private int mStatusHeight;



    public RelativePopupWindow(Activity context) {
        mContext = context;
        this.init(mContext);
    }

    protected final Context getContext() {
        return this.mContext;
    }

    private void init(Context context) {
        mWindowManager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        this.setFocusable(true);
        this.setBackgroundDrawable(null);
        this.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        this.setHeight(WindowManager.LayoutParams.MATCH_PARENT);
    }


    private PreHolderFrameLayout getContentLayout() {
        if (mContentLayout == null) {
            mContentLayout = new PreHolderFrameLayout(this.getContext());
            mContentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                onBlankClick();
                }
            });
            super.setContentView(mContentLayout);
        }
        return mContentLayout;
    }

    protected void onBlankClick() {
        RelativePopupWindow.this.dismiss();
    }

    public void addViewToContent(View contentView) {
        getContentLayout().setContentView(contentView);
    }


    public View addViewToContent(int contentId) {
        return getContentLayout().setContentView(contentId);
    }

    public final void show(View view) {
        if (view == null) {
            return ;
        }
        mContext.getWindow().getDecorView().getWindowVisibleDisplayFrame(mTmpRect);
        mStatusHeight = mTmpRect.top;
        mWindowManager.getDefaultDisplay().getMetrics(mMetrics);
        view.getGlobalVisibleRect(mTmpRect);
        this.onShow(view, mTmpRect.left, mTmpRect.top,mTmpRect.width(),mTmpRect.height());
    }

    public final void showFromWindow(View view) {
        if (view == null) {
            return ;
        }
        //mContext.getWindow().getDecorView().getWindowVisibleDisplayFrame(mTmpRect);
        mStatusHeight = 0;
        mWindowManager.getDefaultDisplay().getMetrics(mMetrics);
        view.getGlobalVisibleRect(mTmpRect);
        this.onShow(view, mTmpRect.left, mTmpRect.top,mTmpRect.width(),mTmpRect.height());
    }

    protected final int getScreenWidth() {
        return mMetrics.widthPixels;
    }

    protected final int getScreenHeight() {
        return mMetrics.heightPixels;
    }

    protected void recalcSize() {
        getContentLayout().recalcSize(this.mMetrics);
    }

    protected void onShow(View view, int viewLeft, int viewTop, int width, int height) {
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)getContentLayout().mInnerView.getLayoutParams();
        params.leftMargin = viewLeft;
        params.topMargin = viewTop - mStatusHeight;
        params.width = width;
        params.height = height;
        getContentLayout().mInnerView.setLayoutParams(params);
        showAtLocation(this.mContext.getWindow().getDecorView(), GRAVITY,0,0);
    }

    protected int getContentHeight() {
        return mContentLayout.mInnerHeight;
    }

    protected int getContentWidth() {
        return mContentLayout.mInnerWidth;
    }

    protected static final class PreHolderFrameLayout extends FrameLayout {


        private View mInnerView = null;
        private int mInnerWidth = 0;
        private int mInnerHeight = 0;
        private boolean mCalcMeasuring = false;
        private int mPreMeasureWidth = MeasureSpec.UNSPECIFIED;
        private int mPreMeasureHeight = MeasureSpec.UNSPECIFIED;

        public PreHolderFrameLayout(Context context) {
            super(context);
            //this.setBackgroundColor(Color.RED);
        }

        public void setContentView(View view) {
            if (this.mInnerView != view && view != null) {
                this.mInnerView = view;
                this.removeAllViewsInLayout();
                this.addView(mInnerView);
            }
        }

        public View setContentView(int id) {
            this.removeAllViewsInLayout();
            View.inflate(getContext(),id,this);
            mInnerView = this.getChildAt(0);
            return mInnerView;
        }

        public void recalcSize(DisplayMetrics metrics) {
            mCalcMeasuring = true;
            this.measure(metrics.widthPixels| MeasureSpec.EXACTLY, metrics.heightPixels| MeasureSpec.EXACTLY);
            mCalcMeasuring = false;
            this.mInnerWidth = mInnerView.getMeasuredWidth();
            this.mInnerHeight = mInnerView.getMeasuredHeight();
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            int width = MeasureSpec.getSize(widthMeasureSpec);
            int height = MeasureSpec.getSize(heightMeasureSpec);
            if (mCalcMeasuring || (mPreMeasureWidth != widthMeasureSpec || mPreMeasureHeight != heightMeasureSpec)) {
                mPreMeasureHeight = heightMeasureSpec;
                mPreMeasureWidth = widthMeasureSpec;
                if (mInnerView != null) {
                    LayoutParams params = (LayoutParams) mInnerView.getLayoutParams();
                    int w = MeasureSpec.UNSPECIFIED;
                    int h = MeasureSpec.UNSPECIFIED;
                    if (params.width == ViewGroup.LayoutParams.MATCH_PARENT) {
                        w = width|MeasureSpec.EXACTLY;
                    } else if (params.width != ViewGroup.LayoutParams.WRAP_CONTENT) {
                        w = params.width|MeasureSpec.EXACTLY;
                    }
                    if (params.height == ViewGroup.LayoutParams.MATCH_PARENT) {
                        h = height|MeasureSpec.EXACTLY;
                    } else if (params.height != ViewGroup.LayoutParams.WRAP_CONTENT) {
                        h = params.height|MeasureSpec.EXACTLY;
                    }

                    mInnerView.measure(w, h);
                }
            }
            this.setMeasuredDimension(width,height);
        }
    }


}
