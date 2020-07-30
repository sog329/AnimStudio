package com.sunshine.studio.base;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import com.sunshine.engine.base.Entity;

/** Created by Jack on 2018/4/12. */
public abstract class RenderHelper<T extends Entity> {
  private Paint paint = new Paint();
  public boolean load = false;
  public Callback cb = null;
  private static final Byte LEN = 5;

  public void setCallback(Callback cb) {
    this.cb = cb;
  }

  public void preOnDraw(Canvas can, T entity, View v) {
    if (entity != null && entity.bmp != null) {
      if (!load) {
        load = true;
        onLoad();
      }
      // 控件区域
      drawRect(
          can,
          true,
          entity.viewArea.l,
          entity.viewArea.t,
          entity.viewArea.l + entity.viewArea.w,
          entity.viewArea.t + entity.viewArea.h,
          cb.getAnimBgColor());
      // 控件线框
      drawRect(
          can,
          false,
          entity.viewArea.l,
          entity.viewArea.t,
          entity.viewArea.l + entity.viewArea.w,
          entity.viewArea.t + entity.viewArea.h,
          Color.argb(90, 255, 255, 255));
    } else {
      load = false;
    }
  }

  public void afterOnDraw(Canvas can, T entity, View v) {
    if (entity != null && entity.bmp != null) {
      if (!load) {
        load = true;
        onLoad();
      }
      // 动画区域
      drawRect(
          can,
          false,
          entity.drawArea.l,
          entity.drawArea.t,
          entity.drawArea.l + entity.drawArea.w,
          entity.drawArea.t + entity.drawArea.h,
          Color.argb(
              102,
              Color.red(cb.getLightColor()),
              Color.green(cb.getLightColor()),
              Color.blue(cb.getLightColor())));
      onDraw(can, entity);
      v.invalidate();
    } else {
      load = false;
    }
  }

  public void drawRect(Canvas can, boolean fill, float l, float t, float r, float b, int color) {
    if (fill) {
      paint.setStyle(Paint.Style.FILL);
    } else {
      paint.setStyle(Paint.Style.STROKE);
    }
    paint.setStrokeWidth(LEN);
    paint.setColor(color);
    can.drawRect(l, t, r, b, paint);
  }

  public abstract void onLoad();

  public abstract void onDraw(Canvas can, T entity);

  public interface Callback {
    int getDarkColor();

    int getLightColor();

    int getAnimBgColor();
  }
}
