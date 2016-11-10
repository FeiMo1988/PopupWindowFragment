package david.demos.multiclick;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import david.demos.R;
import david.demos.popupwindow.DemoPopupWindowFragment;
import david.support.ext.app.PopupWindowFragment;
import david.support.ext.utils.MultiClickInterapterFactory;

/**
 * Created by david.chen on 16/11/4.
 * @mail: dingwei.chen1988@gmail.com
 */

public class Demos_MultiClick extends Activity {


    private static final long CLICK_OVER_TIME = 500l;

    private MultiClickInterapterFactory mFactory = new MultiClickInterapterFactory(CLICK_OVER_TIME);
    private TextView tvClickNumber = null;
    private TextView btnClickNumber = null;
    private TextView stubClickNumber = null;
    private int tvClick = 0;
    private int btnClick = 0;
    private int stubClick = 0;
    private EditText editText;
    private ViewStub stub = null;


    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        //TO interapter
        View view = mFactory.onCreateView(name,context,attrs);
        if (view != null) {
            return view;
        }
        return super.onCreateView(parent, name, context, attrs);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_multiclick);
        tvClickNumber = (TextView) this.findViewById(R.id.textClickNum);
        btnClickNumber = (TextView) this.findViewById(R.id.buttonClickNum);
        stubClickNumber = (TextView) this.findViewById(R.id.stubButtonNumber);
        editText = (EditText)this.findViewById(R.id.inputOverTime);
        stub = (ViewStub)this.findViewById(R.id.stub);
        stub.setVisibility(View.VISIBLE);
    }

    private void resetOverTime() {
        try {
            String time = editText.getText().toString();
            int overtime = Integer.parseInt(time);
            mFactory.setOverTime(overtime);
        }catch (Exception e) {

        }

    }


    public void callTextView(View view) {
        resetOverTime();
        tvClick ++;
        tvClickNumber.setText("TextView clickNumber:"+tvClick);
    }

    public void callButton(View view) {
        resetOverTime();
        btnClick ++;
        btnClickNumber.setText("Button click number:"+btnClick);
    }

    public void callStubButton(View view) {
        resetOverTime();
        stubClick ++;
        stubClickNumber.setText("StubButton click number:"+stubClick);
    }

}
