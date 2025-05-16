package com.example.headachediary.data.source.util;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import java.lang.ref.WeakReference;

public class HeadacheApplication extends Application {
    private static WeakReference<Activity> currentActivity = new WeakReference<>(null);
    private static HeadacheApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {}

            @Override
            public void onActivityStarted(Activity activity) {}

            @Override
            public void onActivityResumed(Activity activity) {
                currentActivity = new WeakReference<>(activity); // Обновляем текущую Activity
            }

            @Override
            public void onActivityPaused(Activity activity) {
                currentActivity.clear();
            }

            @Override
            public void onActivityStopped(Activity activity) {}

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {}

            @Override
            public void onActivityDestroyed(Activity activity) {}
        });
    }

    public static Activity getCurrentActivity() {
        return currentActivity.get();
    }

    public static HeadacheApplication getInstance() {
        return instance;
    }
}
