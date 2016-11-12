package david.support.ext.menus.views;

import david.support.ext.menus.DavidMenu;

/**
 * Created by chendingwei on 16/11/12.
 */

public interface IMenusPanelContainer {


    public void removePanel(MenuPanelLayout view);

    public MenuPanelLayout addPanel(DavidMenu menu);

}
