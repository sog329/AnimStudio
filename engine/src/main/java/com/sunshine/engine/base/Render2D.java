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

  private static void drawCb(
      Canvas can, Callback cb, float percent, DrawInfo drawInfo, float scale) {
    cb.paint.setAlpha(drawInfo.alpha);
    cb.onDraw(can, percent, drawInfo.rcDst, scale);
  }

  public static void draw(Canvas can, Callback cb, float percent, DrawInfo drawInfo, float scale) {
    if (!Tool.equalsZero(drawInfo.rt)) {
      int cs = can.save();
      can.rotate(drawInfo.rt, drawInfo.ptDst.x, drawInfo.ptDst.y);
      drawCb(can, cb, percent, drawInfo, scale);
      can.restoreToCount(cs);
    } else {
      drawCb(can, cb, percent, drawInfo, scale);
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

  public interface Callback {
    final Paint paint = new Paint();

    void init();

    void onDraw(Canvas can, float percent, RectF rect, float scale);
  }
}
