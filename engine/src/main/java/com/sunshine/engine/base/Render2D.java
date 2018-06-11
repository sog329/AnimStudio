package com.sunshine.engine.base;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/** Created by songxiaoguang on 2017/11/30. */
public class Render2D {
  private static Paint paint = new Paint();

  static {
    paint.setAntiAlias(true);
  }

  public static void draw(Canvas can, Bitmap bmp, Rect rcBmp, DrawInfo drawInfo) {
    int cs = can.save();
    if (!Tool.equalsZero(drawInfo.rt)) {
      can.rotate(drawInfo.rt, drawInfo.ptDst.x, drawInfo.ptDst.y);
    }
    paint.setAlpha(drawInfo.alpha);
    can.drawBitmap(bmp, rcBmp, drawInfo.rcDst, paint);
    can.restoreToCount(cs);
  }
}
