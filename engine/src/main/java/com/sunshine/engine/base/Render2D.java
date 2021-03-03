package com.sunshine.engine.base;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
import android.graphics.RectF;

/** Created by songxiaoguang on 2017/11/30. */
public class Render2D {
  public static final Paint PAINT = new Paint();
  private static final PaintFlagsDrawFilter DRAW_FILTER =
      new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);

  static {
    PAINT.setAntiAlias(true);
  }

  public static void setDrawFilter(Canvas can) {
    can.setDrawFilter(DRAW_FILTER);
  }

  private static void drawBmp(Canvas can, Bitmap bmp, Rect rcBmp, DrawInfo drawInfo) {
    PAINT.setAlpha(drawInfo.alpha);
    can.drawBitmap(bmp, rcBmp, drawInfo.rcDst, PAINT);
    PAINT.setAlpha(255);
  }

  private static void drawCb(Canvas can, Callback cb, float percent, Entity entity) {
    cb.paint.setAlpha(entity.drawInfo.alpha);
    if (cb instanceof Rect2D) {
      ((Rect2D) cb).onDraw(can, percent, entity.drawInfo.rcDst, entity.scale);
    }
  }

  public static void draw(Canvas can, Callback cb, float percent, Entity entity) {
    if (!Tool.equalsZero(entity.drawInfo.rt)) {
      int cs = can.save();
      can.rotate(entity.drawInfo.rt, entity.drawInfo.ptDst.x, entity.drawInfo.ptDst.y);
      drawCb(can, cb, percent, entity);
      can.restoreToCount(cs);
    } else {
      drawCb(can, cb, percent, entity);
    }
  }

  public static void draw(Canvas can, Bitmap bmp, Rect rcBmp, DrawInfo drawInfo) {
    if (!Tool.equalsZero(drawInfo.rt)) {
      int cs = can.save();
      can.rotate(drawInfo.rt, drawInfo.ptDst.x, drawInfo.ptDst.y);
      drawBmp(can, bmp, rcBmp, drawInfo);
      can.restoreToCount(cs);
    } else {
      drawBmp(can, bmp, rcBmp, drawInfo);
    }
  }

  public abstract static class Callback {
    public final Paint paint = new Paint();

    public void init() {
      paint.setAntiAlias(true);
    }
  }

  public abstract static class Rect2D extends Callback {
    public abstract void onDraw(Canvas can, float percent, RectF rect, float scale);
  }
}
