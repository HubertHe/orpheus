package com.hippo.orpheus;


import android.util.Log;

import java.util.concurrent.BlockingQueue;

/**
 * @author Hubert He
 * @version V1.0
 * @Description
 * @Createdate 15/1/9 17:27
 */
public abstract class OperationHandler<T> extends Thread {

    private static final String TAG = "Orpheus";

    private volatile boolean mQuit = false;

    protected BlockingQueue<T> mQueue;

    private volatile boolean mStart = false;

    public OperationHandler(BlockingQueue<T> queue) {
        mQueue = queue;
    }

    public void quit() {
        mQuit = true;
        mStart = false;
        interrupt();
    }

    @Override
    public synchronized void start() {
        if (mStart) {
            return;
        }
        super.start();
        mStart = true;
    }

    final protected boolean isQuit() {
        return mQuit;
    }

    @Override
    final public void run() {
        while (true) {
            T data;
            try {
                data = mQueue.take();
            } catch (InterruptedException e) {
                // We may have been interrupted because it was time to quit.
                if (isQuit()) {
                    return;
                }
                continue;
            }
            try {
                dispatch(data);
            } catch (Exception ignored) {
                Log.e(TAG, ignored.getMessage(), ignored);
            }
        }
    }

    abstract protected void dispatch(T data);
}
