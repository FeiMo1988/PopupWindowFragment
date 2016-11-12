package david.support.ext.debug;

import android.util.Log;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by chendingwei on 16/11/11.
 */

public final class Logger {


    private Set<String> mLogNames = new HashSet<>();

    public Logger(Class<?>...clazzes) {
        for (Class<?> clz : clazzes) {
            mLogNames.add(clz.getSimpleName());
        }
    }

    public void log(Object msg) {
        for(String str:mLogNames) {
            Log.v(str,">>"+msg);
        }
    }


}
