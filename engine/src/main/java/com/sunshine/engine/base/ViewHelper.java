package com.sunshine.engine.base;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.DrawFilter;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

/** Created by songxiaoguang on 2017/12/1. */
public abstract class ViewHelper<T extends Entity> extends LifeCycle {

  public static final Handler handler = new Handler(Looper.getMainLooper());

  protected View view = null;
  protected Area viewArea = new Area();
  public T entity = null;

  public boolean play(View view, String configPath, String picPath, String soundPath) {
    this.view = view;
    boolean play = false;
    if (entity == null) {
      register(true);
      entity = buildEntity(this, configPath, picPath, soundPath);
      resize();
      play = true;
    }
    return play;
  }

  protected abstract T buildEntity(
      ViewHelper helper, String configPath, String picPath, String soundPath);

  public Context getContext() {
    Context context = null;
    if (view != null) {
      context = view.getContext();
    }
    return context;
  }

  @Override
  protected Activity getActivity() {
    Context context = getContext();
    while (context instanceof ContextWrapper) {
      if (context instanceof Activity) {
        return (Activity) context;
      }
      context = ((ContextWrapper) context).getBaseContext();
    }
    return null;
  }

  public void stopAsync(final Entity obj) {
    handler.post(
        () -> {
          if (obj != null && obj.equals(entity)) {
            stop();
          }
        });
  }

  public void resize(int left, int top, int r, int b) {
    viewArea.l = left;
    viewArea.t = top;
    viewArea.w = r - viewArea.l;
    viewArea.h = b - viewArea.t;
    resize();
  }

  private void resize() {
    if (entity != null) {
      entity.viewArea.w = viewArea.w;
      entity.viewArea.h = viewArea.h;
      entity.viewArea.l = viewArea.l;
      entity.viewArea.t = viewArea.t;
      LayoutHelper.resize(entity);
    }
  }

  @Override
  public void stop() {
    if (entity != null) {
      entity.destroy();
      entity = null;
      invalidate();
    }
    register(false);
    view = null;
  }

  @Override
  public void invalidate() {
    if (view != null) {
      long now = Tool.getTime();
      if (now - resumeTime < 250) {
        view.postInvalidateDelayed(40);
      } else {
        view.postInvalidate();
      }
    }
  }

  public void draw(Canvas can) {
    if (entity != null && view != null) {
      DrawFilter drawFilter = can.getDrawFilter();
      Render2D.setDrawFilter(can);
      long drawTime = Tool.getTime();
      boolean needRender = entity.draw(can, drawTime);
      if (needRender && isResume) {
        invalidate();
      }
      can.setDrawFilter(drawFilter);
    }
  }

  public boolean setExternalBmp(String id, Bitmap bmp) {
    if (entity != null) {
      if (bmp == null || bmp.isRecycled()) {
        entity.mapBmp.remove(id);
      } else {
        entity.mapBmp.put(id, bmp);
      }
      return true;
    } else {
      return false;
    }
  }
}
