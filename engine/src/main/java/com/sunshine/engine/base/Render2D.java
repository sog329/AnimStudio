package com.sunshine.engine.base;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;

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

  public static void draw(Canvas can, Bitmap bmp, Rect rcBmp, DrawInfo drawInfo) {
    int cs = can.save();
    if (!Tool.equalsZero(drawInfo.rt)) {
      can.rotate(drawInfo.rt, drawInfo.ptDst.x, drawInfo.ptDst.y);
    }
    PAINT.setAlpha(drawInfo.alpha);
    can.drawBitmap(bmp, rcBmp, drawInfo.rcDst, PAINT);
    can.restoreToCount(cs);
    PAINT.setAlpha(255);
  }
}
