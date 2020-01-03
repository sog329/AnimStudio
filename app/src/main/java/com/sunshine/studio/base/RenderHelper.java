package com.sunshine.studio.base;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

import com.sunshine.engine.base.Entity;
import com.sunshine.studio.R;

/** Created by Jack on 2018/4/12. */
public abstract class RenderHelper<T extends Entity> {
  private Paint paint = new Paint();
  public boolean load = false;

  {
    paint.setStyle(Paint.Style.STROKE);
    paint.setStrokeWidth(5);
  }

  public void onDraw(Canvas can, T entity, View v) {
    if (entity != null && entity.bmp != null) {
      if (!load) {
        load = true;
        onLoad();
      }
      drawRect(
          can,
          false,
          entity.viewArea.l,
          entity.viewArea.t,
          entity.viewArea.l + entity.viewArea.w,
          entity.viewArea.t + entity.viewArea.h,
          v.getResources().getColor(R.color.btn_bg));
      drawRect(
          can,
          false,
          entity.drawArea.l,
          entity.drawArea.t,
          entity.drawArea.l + entity.drawArea.w,
          entity.drawArea.t + entity.drawArea.h,
          v.getResources().getColor(R.color.btn_bg2));
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
    paint.setColor(color);
    can.drawRect(l, t, r, b, paint);
  }

  public abstract void onLoad();

  public abstract void onDraw(Canvas can, T entity);
}
