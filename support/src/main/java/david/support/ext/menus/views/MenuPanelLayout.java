package david.support.ext.menus.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

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
            LOG.log("onclick "+mSelectItem);
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

    private void performUnSelect() {
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
            MenuPanelLayout layout = mContainer.addPanel(item.subMenu);
            this.mChildPanelLayout = layout;
        }
        mSelectItem = item;
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
        this.performUnSelect();
        if (mContainer != null) {
            this.mContainer.removePanel(this);
        }
    }

}
