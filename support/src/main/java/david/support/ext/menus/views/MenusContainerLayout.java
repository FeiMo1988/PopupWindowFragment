package david.support.ext.menus.views;

import android.content.Context;
import android.view.ViewGroup;

import david.support.ext.debug.Logger;
import david.support.ext.menus.DavidMenu;
import david.support.ext.menus.DavidMenuInflater;

/**
 * Created by chendingwei on 16/11/11.
 */

public class MenusContainerLayout extends ViewGroup implements IMenusPanelContainer {


    private Context mContext = null;
    private DavidMenu mMenu = null;
    private DavidMenuInflater mMenuInflater = null;
    private int mMenuResourceId = -1;
    private static final Logger LOG = new Logger(MenusContainerLayout.class);

    public MenusContainerLayout(Context context) {
        super(context);
        this.init(context);
    }

    public final void setMenuResource(int id) {
        if (mMenuResourceId != id) {
            this.mMenuResourceId = id;
            this.mMenuInflater.inflate(this.mMenuResourceId,this.mMenu);
            this.addPanel(this.mMenu);
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
        final int count = this.getChildCount();
        int index = 0;
        MenuPanelLayout view = null;

        while (index < count) {
            view = (MenuPanelLayout)this.getChildAt(index);
            view.layout(l,b - view.getMeasuredHeight(),l+view.getMeasuredWidth(),b);
            l = view.getRight();
            index ++;
        }
    }

    @Override
    public void removePanel(MenuPanelLayout view) {
        view.setContainer(null);
        this.removeViewInLayout(view);
    }

    @Override
    public MenuPanelLayout addPanel(DavidMenu menu) {
        MenuPanelLayout view = (MenuPanelLayout)menu.getView();
        if (view == null) {
            view = new MenuPanelLayout(this.getContext());
            view.setMenu(menu);
        }
        view.setContainer(this);
        view.measure(MeasureSpec.UNSPECIFIED,MeasureSpec.UNSPECIFIED);
        this.addView(view);
        return view;
    }
}
