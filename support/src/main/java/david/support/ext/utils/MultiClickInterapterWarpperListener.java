package david.support.ext.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by chendingwei on 16/11/9.
 */

public class MultiClickInterapterWarpperListener implements View.OnClickListener {


    private static final long CLICK_OVER_TIME = 500l;
    public long mOverTime = CLICK_OVER_TIME;
    private View.OnClickListener mListener;
    private long mPreTime = -1;


    public MultiClickInterapterWarpperListener(long overTime,View.OnClickListener listener) {
        mOverTime = overTime;
        mListener = listener;
    }
    public MultiClickInterapterWarpperListener(View.OnClickListener listener) {
        mListener = listener;
    }


    @Override
    public void onClick(View view) {
        if (mListener != null) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - mPreTime > mOverTime) {
                mPreTime = currentTime;
                mListener.onClick(view);
            }
        }
    }
}
