package com.osmond.study;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

public class DevLog {
    static private final String DEFAULT_TAG_NAME = "Nil";
    static private boolean TURN_ON = true;
    private String TAG = DEFAULT_TAG_NAME;

    public DevLog(@Nullable String TAG) {
        if (TAG == null || TAG.isEmpty()) {
            this.TAG = DEFAULT_TAG_NAME;
        } else {
            this.TAG = TAG;
        }
    }

    static void setTurnOn(boolean flag) {
        TURN_ON = flag;
    }

    public void v(@NonNull String msg) {
        if (!TURN_ON) {
            return;
        }
        try {
            Log.v(TAG, msg);

        } catch (Exception ignore) {
        }
    }

    public void d(@NonNull String msg) {
        if (!TURN_ON) {
            return;
        }
        try {
            Log.d(TAG, msg);
        } catch (Exception ignore) {
        }
    }

    public void i(@NonNull String msg) {
        if (!TURN_ON) {
            return;
        }
        try {
            Log.i(TAG, msg);
        } catch (Exception ignore) {
        }
    }

    public void w(@NonNull String msg) {
        if (!TURN_ON) {
            return;
        }
        try {
            Log.w(TAG, msg);
        } catch (Exception ignore) {
        }
    }

    public void e(@NonNull String msg) {
        if (!TURN_ON) {
            return;
        }
        try {
            Log.e(TAG, msg);
        } catch (Exception ignore) {
        }
    }

    public void e(@NonNull String msg, @NonNull Throwable e) {
        if (!TURN_ON) {
            return;
        }
        try {
            Log.e(TAG, msg);
            e.printStackTrace();
        } catch (Exception ignore) {
        }
    }

    public void e(@NonNull Throwable e) {
        if (!TURN_ON) {
            return;
        }
        try {
            e.printStackTrace();

        } catch (Exception ignore) {
        }
    }

}
