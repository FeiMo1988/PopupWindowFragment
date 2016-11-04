package david.demos.popupwindow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import david.demos.R;
import david.support.ext.app.PopupWindowFragment;

/**
 * Created by chendingwei on 16/11/4.
 */

public class DemoPopupWindowFragment extends PopupWindowFragment {
    @Override
    protected View onCreateView(LayoutInflater inflater, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.demo_pop_window,null);
    }
}
