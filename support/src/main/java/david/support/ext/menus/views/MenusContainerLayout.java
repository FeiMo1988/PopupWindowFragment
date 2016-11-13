package david.support.ext.menus.views;

import android.content.Context;
import android.util.Property;
import android.view.View;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import david.support.ext.debug.Logger;
import david.support.ext.menus.DavidMenu;
import david.support.ext.menus.DavidMenuInflater;
import david.support.ext.menus.DavidMenuPopupWindow;

/**
 * Created by dingwei.chen1988@gmail.com on 16/11/11.
 */

public class MenusContainerLayout extends ViewGroup implements IMenusPanelContainer {


    private Context mContext = null;
    private DavidMenu mMenu = null;
    private DavidMenuInflater mMenuInflater = null;
    private int mMenuResourceId = -1;
    private static final Logger LOG = new Logger(MenusContainerLayout.class);
    private boolean mIsLayout = false;
    private MenuPanelLayout mRootPanelLayout;
    private Set<WeakReference<OnMenuItemClickListener>> mListeners = new HashSet<>();


    public MenusContainerLayout(Context context) {
        super(context);
        this.init(context);
    }

    public final void setMenuResource(int id) {
        if (mMenuResourceId != id) {
            this.mMenuResourceId = id;
            this.mMenuInflater.inflate(this.mMenuResourceId,this.mMenu);
            this.addPanel(null,this.mMenu,null);
        }
    }

    private void init(Context context) {
        this.setWillNotDraw(false);
        this.mContext = context;
        this.mMenuInflater = new DavidMenuInflater(context);
        this.mMenu = new DavidMenu();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        this.setMeasuredDimension(width,height);
    }

    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (!mIsLayout) {
            int index = 0;
            final int count = this.getChildCount();
            l = this.getPaddingLeft();
            View view = null;
            while (index < count) {
                view = this.getChildAt(index);
                view.layout(l,getHeight() - view.getMeasuredHeight(),l + view.getMeasuredWidth(),getHeight());
                l = view.getRight();
                index ++;
            }
        }
        mIsLayout = true;

    }

    @Override
    public void removePanel(MenuPanelLayout view) {
        view.setContainer(null);
        this.removeViewInLayout(view);
    }

    @Override
    public MenuPanelLayout addPanel(MenuPanelLayout parent,DavidMenu menu, DavidMenu.DavidMenuItem item) {
        MenuPanelLayout view = (MenuPanelLayout)menu.getView();
        if (view == null) {
            view = new MenuPanelLayout(this.getContext());
            view.setMenu(menu);
        }
        view.setContainer(this);
        View relativeView = item != null?item.getView():null;
        this.addAndMeasureLayout(parent,view,relativeView);
        if (mRootPanelLayout == null) {
            this.mRootPanelLayout = view;
        }
        return view;
    }

    private void addAndMeasureLayout(MenuPanelLayout parent,MenuPanelLayout view,View relativeView) {
        this.addView(view);
        view.measure(MeasureSpec.UNSPECIFIED,MeasureSpec.UNSPECIFIED);
        if (!mIsLayout) {
            return;
        }
        int l = 0;
        if (parent != null) {
            l = parent.getRight();
        } else {
            l = this.getPaddingLeft();
        }
        int tOffset = 0;
        if (parent != null) {
            tOffset = parent.getTop();
        }
        int t = this.getHeight() - view.getMeasuredHeight();
        if (relativeView != null) {
            t = relativeView.getTop();
        }
        t += tOffset;
        view.setPivotX(0);
        view.setPivotY(view.getMeasuredHeight()>>1);
        if (t + view.getMeasuredHeight() > this.getHeight()) {
            t = this.getHeight() - view.getMeasuredHeight();
        }
        view.layout(l,t,l+view.getMeasuredWidth(),t+view.getMeasuredHeight());
        view.showAnimation();
    }

    @Override
    public void performItemClick(DavidMenu menu, DavidMenu.DavidMenuItem item) {
        performMenuItemClick(menu,item);
    }

    public void dismiss() {
        if (mRootPanelLayout != null) {
            mRootPanelLayout.performUnSelect();
        }
    }

    public boolean pop() {
        if (this.getChildCount() <= 1) {
            return false;
        }
        MenuPanelLayout layout = (MenuPanelLayout)this.getChildAt(this.getChildCount() - 2);
        layout.performUnSelect();
        return true;
    }

    public static interface OnMenuItemClickListener {
        public void onMenuItemClick(DavidMenu menu, DavidMenu.DavidMenuItem item);
    }

    private void performMenuItemClick(DavidMenu menu, DavidMenu.DavidMenuItem item) {
        Iterator<WeakReference<OnMenuItemClickListener>> iter = mListeners.iterator();
        OnMenuItemClickListener temp = null;
        while (iter.hasNext()) {
            temp = iter.next().get();
            if (temp == null) {
                iter.remove();
            } else {
                temp.onMenuItemClick(menu,item);
            }
        }
    }

    public void addOnMenuItemClickListener(OnMenuItemClickListener listener) {
        if (listener != null && !isContainListener(listener)) {
            mListeners.add(new WeakReference<OnMenuItemClickListener>(listener));
        }
    }

    public void removeOnMenuItemClickListener(OnMenuItemClickListener listener) {
        if (listener == null) {
            return;
        }
        Iterator<WeakReference<OnMenuItemClickListener>> iter = mListeners.iterator();
        OnMenuItemClickListener temp = null;
        while (iter.hasNext()) {
            temp = iter.next().get();
            if (temp == null) {
                iter.remove();
            } else {
                if (temp == listener) {
                    iter.remove();
                    break;
                }
            }
        }
    }

    private boolean isContainListener(OnMenuItemClickListener listener) {
        if (listener == null) {
            return false;
        }
        Iterator<WeakReference<OnMenuItemClickListener>> iter = mListeners.iterator();
        OnMenuItemClickListener temp = null;
        while (iter.hasNext()) {
            temp = iter.next().get();
            if (temp == null) {
                iter.remove();
            } else {
                if (temp == listener) {
                    return true;
                }
            }
        }
        return false;
    }

}
