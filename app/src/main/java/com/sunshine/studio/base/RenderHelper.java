package com.sunshine.studio.base;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

import com.sunshine.engine.base.Entity;
import com.sunshine.engine.bone.logic.Bone;
import com.sunshine.studio.R;

/** Created by Jack on 2018/4/12. */
public abstract class RenderHelper {
  private Paint paint = new Paint();
  public boolean load = false;

  {
    paint.setStyle(Paint.Style.STROKE);
    paint.setStrokeWidth(5);
  }

  public void onDraw(Canvas can, Entity entity, View v) {
    if (entity != null && entity.bmp != null) {
      if (!load) {
        load = true;
        onLoad();
      }
      paint.setColor(v.getResources().getColor(R.color.btn_bg));
      can.drawRect(
          entity.viewArea.l,
          entity.viewArea.t,
          entity.viewArea.l + entity.viewArea.w,
          entity.viewArea.t + entity.viewArea.h,
          paint);
      paint.setColor(v.getResources().getColor(R.color.btn_bg2));
      can.drawRect(
          entity.drawArea.l,
          entity.drawArea.t,
          entity.drawArea.l + entity.drawArea.w,
          entity.drawArea.t + entity.drawArea.h,
          paint);
      onDraw(can, entity);
      v.invalidate();
    } else {
      load = false;
    }
  }

  public abstract void onLoad();

  public abstract void onDraw(Canvas can, Entity entity);
}
