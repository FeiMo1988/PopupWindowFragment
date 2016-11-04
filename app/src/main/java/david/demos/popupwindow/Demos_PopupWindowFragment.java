package david.demos.popupwindow;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.Spinner;

import david.demos.R;
import david.support.ext.app.PopupWindowFragment;

/**
 * Created by david.chen on 16/11/4.
 * @mail: dingwei.chen1988@gmail.com
 */

public class Demos_PopupWindowFragment extends Activity {


    private PopupWindowFragment mPopupWindowFragment = null;
    private String mPopTag = "PopupWindowFragment";
    private Spinner mGravitySpinner ;
    private Spinner mAnimSpinner;
    private int[] mGravitys =  {Gravity.BOTTOM,Gravity.CENTER,Gravity.TOP};
    private CheckBox mCheckBox = null;
/**
  <string-array name="window_anim">
         <item>Like Activity</item>
         <item>Like Toast</item>
         <item>Like Dialog</item>
         <item>Like InputMethod</item>
         </string-array>
 * */
    private int[] mWindowAnims =  {
            PopupWindowFragment.ANIM_ACTIVITY,
            PopupWindowFragment.ANIM_TOAST,
            PopupWindowFragment.ANIM_DIALOG,
            PopupWindowFragment.ANIM_INPUTMETHOD};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fragment fragment = this.getFragmentManager().findFragmentByTag(mPopTag);
        if (fragment != null) {
            mPopupWindowFragment = (PopupWindowFragment) fragment;
        } else {
            mPopupWindowFragment = new DemoPopupWindowFragment();
        }
        this.setContentView(R.layout.activity_demos_popupwindowfragment);
        mGravitySpinner = (Spinner) this.findViewById(R.id.gravity_spinner);
        mAnimSpinner = (Spinner)this.findViewById(R.id.anim_spinner);
        mCheckBox = (CheckBox) this.findViewById(R.id.hasBackground);
        mPopupWindowFragment.requestGravity(mGravitys[0]).requestPopupAnimationStyle(mWindowAnims[0]);
        mGravitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mPopupWindowFragment.requestGravity(mGravitys[i]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        mAnimSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mPopupWindowFragment.requestPopupAnimationStyle(mWindowAnims[i]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    public void callPopupWindowShow(View view) {
        if (mCheckBox.isChecked()) {
            mPopupWindowFragment.requestBackgroundColor(0x5500FF55);
        } else {
            mPopupWindowFragment.requestBackgroundColor(-1);
        }

        mPopupWindowFragment.show(getFragmentManager(),mPopTag);
    }

}
