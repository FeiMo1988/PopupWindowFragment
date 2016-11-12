package david.demos.menus;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import david.demos.R;
import david.support.ext.debug.Logger;
import david.support.ext.menus.DavidMenu;
import david.support.ext.menus.DavidMenuInflater;
import david.support.ext.menus.views.MenuItemLayout;
import david.support.ext.menus.views.MenuPanelLayout;
import david.support.ext.menus.views.MenusContainerLayout;

/**
 * Created by chendingwei on 16/11/11.
 */

public class Demos_Menus extends Activity {

    private MenuInflater inflater = null;
    private static final Logger LOG = new Logger(Demos_Menus.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MenusContainerLayout layout = new MenusContainerLayout(this);
        layout.setMenuResource(R.menu.test_menu);
        FrameLayout f = new FrameLayout(this);
        f.addView(layout,new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        this.setContentView(f);
    }
}
