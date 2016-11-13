package david.demos.menus;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.MenuPopupWindow;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import david.demos.R;
import david.support.ext.debug.Logger;
import david.support.ext.menus.DavidMenu;
import david.support.ext.menus.DavidMenuPopupWindow;
import david.support.ext.menus.views.MenusContainerLayout;

/**
 * Created by chendingwei on 16/11/11.
 */

public class Demos_Menus extends Activity implements MenusContainerLayout.OnMenuItemClickListener {

    private DavidMenuPopupWindow menuPopupWindow = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.demo_menus);
        menuPopupWindow = new DavidMenuPopupWindow(this,R.menu.test_menu);
        menuPopupWindow.setOnMenuItemClickListener(this);
    }

    public void testShowMenu(View view) {
        menuPopupWindow.show(view);
    }

    private void toast(String msg) {
        Toast.makeText(this,"["+msg+"]",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMenuItemClick(DavidMenu menu, DavidMenu.DavidMenuItem item) {
        toast(menu +":"+item.title);
    }
}
