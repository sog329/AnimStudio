package com.sunshine.studio.base;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

import com.sunshine.engine.base.Entity;
import com.sunshine.engine.bone.logic.Bone;
import com.sunshine.studio.R;

/** Created by Jack on 2018/4/12. */
public class RenderHelper {
  private Paint paint = new Paint();
	public boolean load = false;
  public Callback callback = null;

  {
    paint.setStyle(Paint.Style.STROKE);
    paint.setStrokeWidth(5);
  }

  public void onDraw(Canvas can, Entity entity, View v) {
    if (entity != null && entity.bmp != null) {
      if (!load) {
        load = true;
        if (callback != null) {
          callback.onLoad();
        }
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
      if (callback != null) {
        callback.draw(can);
        callback.onPercent(entity.getPercent());
      }
      v.invalidate();
    } else {
      load = false;
    }
  }

  public interface Callback {
    boolean onMove(int x, int y);

    void draw(Canvas can);

    void onLoad();

    void onPercent(float percent);

    void onClickBone(Bone bone);
  }
}
