package david.support.ext.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by chendingwei on 16/11/9.
 */

public final class MultiClickInterapterFactory implements LayoutInflater.Factory2 {


    public long mOverTime = 500L;


    public MultiClickInterapterFactory(long overTime) {
        mOverTime = overTime;
    }

    public final void setOverTime(long overTime) {
        if (overTime < 0) {
            overTime = 0;
        }
        this.mOverTime = overTime;
    }

    @Override
    public View onCreateView(View view, String name, Context context, AttributeSet attributeSet) {
        if ("ViewStub".equals(name)) {
            ViewStub stub = new ViewStub(context,attributeSet);
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            inflater = inflater.cloneInContext(context);
            inflater.setFactory2(this);
            stub.setLayoutInflater(inflater);
            return stub;
        }

        if ("Button".equals(name)) {
            Button button = new Button(context,attributeSet) {
                long startTime = -1L;

                @Override
                public boolean performClick() {
                    long currentTime = System.currentTimeMillis();
                    if (currentTime - startTime > mOverTime) {
                        startTime = currentTime;
                        return super.performClick();
                    } else {
                        return true;
                    }
                }
            };
            return button;
        }

        if ("TextView".equals(name)) {
            TextView tv = new TextView(context,attributeSet) {
                long startTime = -1L;
                @Override
                public boolean performClick() {
                    long currentTime = System.currentTimeMillis();
                    if (currentTime - startTime > mOverTime) {
                        startTime = currentTime;
                        return super.performClick();
                    } else {
                        return true;
                    }
                }
            };
            return tv;
        }

        return null;
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attributeSet) {
        return this.onCreateView(null,name,context,attributeSet);
    }
}
