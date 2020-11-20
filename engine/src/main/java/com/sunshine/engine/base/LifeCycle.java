package com.sunshine.engine.base;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

/** Created by Jack on 2019-09-11. */
public abstract class LifeCycle implements Application.ActivityLifecycleCallbacks {

  protected boolean isResume = true;
  protected long resumeTime = 0;

  protected abstract Activity getActivity();

  private boolean isCurrentAct(Activity act) {
    return act == getActivity();
  }

  public abstract void invalidate();

  public abstract void stop();

  protected void register(boolean register) {
    Activity act = getActivity();
    if (act != null) {
      act.getApplication().unregisterActivityLifecycleCallbacks(this);
      if (register) {
        act.getApplication().registerActivityLifecycleCallbacks(this);
        // AnimView仅在播放中监听声明周期变化，估复用时默认为resume状态
        isResume = true;
      }
    }
  }

  @Override
  public void onActivityCreated(Activity activity, Bundle savedInstanceState) {}

  @Override
  public void onActivityStarted(Activity activity) {}

  @Override
  public void onActivityResumed(Activity activity) {
    if (isCurrentAct(activity)) {
      isResume = true;
      resumeTime = Tool.getTime();
      invalidate();
    }
  }

  @Override
  public void onActivityPaused(Activity activity) {
    if (isCurrentAct(activity)) {
      isResume = false;
    }
  }

  @Override
  public void onActivityStopped(Activity activity) {}

  @Override
  public void onActivitySaveInstanceState(Activity activity, Bundle outState) {}

  @Override
  public void onActivityDestroyed(Activity activity) {
    if (isCurrentAct(activity)) {
      stop();
    }
  }
}
