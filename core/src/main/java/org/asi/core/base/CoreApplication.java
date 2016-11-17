package org.asi.core.base;

import android.app.Application;

import org.asi.core.utils.ToastUtils;


/**
 * Created by _SOLID
 * Date:2016/3/30
 * Time:20:59
 */
public class CoreApplication extends Application {
    private static CoreApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        ToastUtils.init(this);
    }

    public static CoreApplication getInstance() {
        return mInstance;
    }
}
