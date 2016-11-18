package david.support.ext.net;

import android.os.Handler;
import android.os.HandlerThread;
import android.text.TextUtils;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by chendingwei on 16/11/17.
 * Poll is ugly design !!!! ------ David Chen
 */

public class PollService {

    private ReentrantLock mLock = new ReentrantLock();
    private Map<String,PollHandler> mPollMap = new HashMap<>();
    public static final long TIME_OUT_NEVER = Integer.MAX_VALUE;//the thread never be destoryed
    public static final long TIME_OUT_DESTORY_IF_CALLBACK_NULL = Integer.MIN_VALUE;
    private static PollService sInstance = new PollService();
    private LogPrinter mLogPrinter = null;

    private PollService(){}

    public static PollService getService() {
        return sInstance;
    }

    /**
     * @param  pollName the poll thread name
     * @param  pollTime the poll time
     * @param timeOut if your callback is recycled. after timeout，the thread should be destory
     *                if you want not to destory the thread .you can input
     *                {@link PollService#TIME_OUT_NEVER}
     *                if you want to destory the thread imme.you can input
     *                {@link PollService#TIME_OUT_DESTORY_IF_CALLBACK_NULL}
     * @param callback the poll thread callback interface
     * */
    public final void subscribe(String pollName, long pollTime, long timeOut, PollCallback callback) {
        if (TextUtils.isEmpty(pollName)) {
            throw new IllegalArgumentException(" you must asign a name to you poll");
        }
        PollHandler handler = getOrCreatePollThreadLock(pollName,pollTime,timeOut);
        handler.replaceCallback(callback);

    }

    public static abstract class PollCallback {

        private PollHandler mHandler = null;

        public abstract  void onExecute();

        public  void onExecuteException(Exception e){
            //TODO
        }

        public void unSubscribe() {
            if (mHandler != null) {
                mHandler.unRegistor(this);
                mHandler = null;
            }
        }

    }


    private class ExitAction implements Runnable {

        private HandlerThread thread;

        public ExitAction(HandlerThread thread) {
            this.thread = thread;
        }

        @Override
        public void run() {
            printLog("call exit thread!");
            this.thread.quit();
        }
    }

    private class PollHandler extends Handler implements Runnable {

        private String pollName;
        private long timeOut;
        private long pollTime;
        private AtomicReference<WeakReference<PollCallback>> mCallback = new AtomicReference<WeakReference<PollCallback>>();
        private AtomicBoolean isCheckTimeOut = new AtomicBoolean(false);
        private HandlerThread thread = null;
        private ExitAction exitAction = null;
        public PollHandler(String name, HandlerThread thread) {
            super(thread.getLooper());
            this.pollName = name;
            this.thread = thread;
            this.exitAction = new ExitAction(this.thread);
        }

        @Override
        public void run() {
            if (timeOut != TIME_OUT_NEVER) {
                boolean isExit = false;
                mLock.lock();
                if (isCheckTimeOut.getAndSet(false)) {
                    WeakReference<PollCallback> wr = mCallback.get();
                    if (wr == null || wr.get() == null) {
                        mPollMap.remove(this.pollName);
                        isExit = true;
                        this.post(exitAction);
                    }
                }
                mLock.unlock();
                if (isExit) {
                    return;
                }
            }
            WeakReference<PollCallback> wr = mCallback.get();
            PollCallback callback = null;
            try {
                callback = wr.get();
                printLog("getCallback is "+callback);
                if (callback == null) {
                    throw new NullPointerException();
                }
                try {
                    callback.onExecute();
                } catch (Exception e) {
                    callback.onExecuteException(e);
                }
                this.repost();
                return;
            } catch (NullPointerException e) {
                if(startTimeOutCheck()) {
                    return ;
                } else {
                    if (callback == null) {//this.timeOut == TIME_OUT_NEVER and callback isrecycle
                        printLog("waiting callback");
                        return;
                    }
                }
            }
            this.repost();

        }

        private void repost() {
            this.removeCallbacks(this);
            this.postDelayed(this,this.pollTime);
        }

        public void replaceCallback(PollCallback callback) {
            callback.mHandler = this;
            mCallback.set(new WeakReference<PollCallback>(callback));
            isCheckTimeOut.set(false);
            this.removeCallbacks(this);
            this.post(this);
        }

        public boolean startTimeOutCheck() {
            printLog("start timeout check");
            if (this.timeOut == TIME_OUT_NEVER) {
                return false;
            }
            if (this.timeOut == TIME_OUT_DESTORY_IF_CALLBACK_NULL) {
                this.post(exitAction);
                return true;
            }
            isCheckTimeOut.set(true);
            this.postDelayed(this,timeOut);
            return true;
        }

        public void unRegistor(PollCallback callback) {
            WeakReference<PollCallback> wr = mCallback.get();
            if (wr != null) {
                PollCallback prec = wr.get();
                if (prec == callback) {
                    mCallback.set(null);
                    startTimeOutCheck();
                }
            }
        }
    }

    private HandlerThread obtainThread(String name) {
        HandlerThread thread = new HandlerThread("POLL #["+name+"]");
        thread.start();
        return thread;
    }

    private PollHandler getOrCreatePollThreadLock(String name, long pollTime, long timeOut) {
        PollHandler handler ;
        mLock.lock();
        handler = mPollMap.get(name);
        if (handler == null) {
            printLog("create handler");
            HandlerThread thread = obtainThread(name);
            handler = new PollHandler(name,thread);
            handler.timeOut = timeOut;
            handler.pollTime = pollTime;
            mPollMap.put(name,handler);
        } else {
            printLog("use cache handler");
        }
        mLock.unlock();
        return handler;
    }


    public static interface LogPrinter {
        public void printLog(String msg);
    }

    public final void setLogPrinter(LogPrinter printer) {
        mLogPrinter = printer;
    }

    private void printLog(String msg) {
        if (this.mLogPrinter != null) {
            mLogPrinter.printLog(msg);
        }
    }

}
