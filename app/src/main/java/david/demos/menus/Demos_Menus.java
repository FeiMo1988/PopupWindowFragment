package david.demos.menus;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.MenuPopupWindow;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import david.demos.R;
import david.support.ext.debug.Logger;
import david.support.ext.menus.DavidMenuPopupWindow;
import david.support.ext.menus.views.MenusContainerLayout;

/**
 * Created by chendingwei on 16/11/11.
 */

public class Demos_Menus extends Activity {

    private MenuInflater inflater = null;
    private static final Logger LOG = new Logger(Demos_Menus.class);
    private DavidMenuPopupWindow menuPopupWindow = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.demo_menus);
        menuPopupWindow = new DavidMenuPopupWindow(this,R.menu.test_menu);
    }

    public void testShowMenu(View view) {
        menuPopupWindow.show(view);
    }
}
