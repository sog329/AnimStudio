package com.sunshine.studio.bone.logic;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;

import com.sunshine.engine.base.Tool;
import com.sunshine.studio.base.StudioTool;

/** Created by songxiaoguang on 2017/12/5. */
public class StudioRender2D {
  public static Paint paint = new Paint();
  public static Matrix m = new Matrix();
  public static RectF rc = new RectF();

  static {
    paint.setAntiAlias(true);
    paint.setStrokeWidth(5);
  }

  public static void draw(
      Canvas can,
      RectF rc,
      int color,
      int alpha,
      boolean fill,
      float rotate,
      float rtX,
      float rtY) {
    int cs = can.save();
    if (!Tool.equalsZero(rotate)) {
      can.rotate(rotate, rtX, rtY);
    }
    paint.setColor(color);
    if (fill) {
      paint.setAlpha(alpha);
      paint.setStyle(Paint.Style.FILL);
    } else {
      paint.setAlpha(alpha);
      paint.setStyle(Paint.Style.STROKE);
    }
    can.drawRect(rc, paint);
    can.restoreToCount(cs);
  }

  public static void drawCross(Canvas can, PointF pt, int color) {
    paint.setColor(color);
    paint.setAlpha(255);
    int len = StudioTool.getBtnHeight() / 2;
    can.drawLine(pt.x, pt.y - len, pt.x, pt.y + len, paint);
    can.drawLine(pt.x - len, pt.y, pt.x + len, pt.y, paint);
  }

  public static void drawLine(Canvas can, float x, float h, int color) {
    paint.setColor(color);
    can.drawLine(x, 0, x, h, paint);
  }
}
