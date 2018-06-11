package com.sunshine.studio.bone.logic;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

import com.sunshine.engine.base.Entity;
import com.sunshine.engine.base.Tool;
import com.sunshine.studio.base.StudioTool;

/** Created by songxiaoguang on 2017/12/5. */
public class StudioRender2D {
  public static Paint paint = new Paint();

  static {
    paint.setAntiAlias(true);
    paint.setStrokeWidth(5);
  }

  public static void draw(Entity entity, Canvas can, int color) {
    int cs = can.save();
    if (!Tool.equalsZero(entity.drawInfo.rt)) {
      can.rotate(entity.drawInfo.rt, entity.drawInfo.ptDst.x, entity.drawInfo.ptDst.y);
    }
    paint.setColor(color);
    // area
    paint.setAlpha(100);
    paint.setStyle(Paint.Style.FILL);
    can.drawRect(entity.drawInfo.rcDst, paint);
    // line
    paint.setAlpha(255);
    paint.setStyle(Paint.Style.STROKE);
    can.drawRect(entity.drawInfo.rcDst, paint);
    can.restoreToCount(cs);
  }

  public static void draw(Canvas can, Point pt, int color) {
    paint.setColor(color);
    paint.setAlpha(255);
    int len = StudioTool.getBtnHeight() / 2;
    can.drawLine(pt.x, pt.y - len, pt.x, pt.y + len, paint);
    can.drawLine(pt.x - len, pt.y, pt.x + len, pt.y, paint);
  }

  public static void draw(Canvas can, float x, float h, int color) {
    paint.setColor(color);
    paint.setAlpha(255);
    can.drawLine(x, 0, x, h, paint);
  }
}
