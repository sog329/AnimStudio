package com.sunshine.engine.base;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.DrawFilter;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

/** Created by songxiaoguang on 2017/12/1. */
public abstract class ViewHelper<T extends Entity> {

  public static final Handler handler = new Handler(Looper.getMainLooper());
  public static final PaintFlagsDrawFilter DRAW_FILTER =
      new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);

  protected View view = null;
  protected Area viewArea = new Area();
  public T entity = null;

  public boolean play(View view, String configPath, String picPath, String soundPath) {
    this.view = view;
    boolean play = false;
    if (entity == null) {
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

  public void stop() {
    if (entity != null) {
      entity.destroy();
      entity = null;
    }
    view = null;
  }

  public void invalidate() {
    if (view != null) {
      view.invalidate();
    }
  }

  public void draw(Canvas can) {
    if (entity != null && view != null) {
      DrawFilter drawFilter = can.getDrawFilter();
      can.setDrawFilter(DRAW_FILTER);
      long drawTime = Tool.getTime();
      boolean needRender = entity.draw(can, drawTime);
      if (needRender) {
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
