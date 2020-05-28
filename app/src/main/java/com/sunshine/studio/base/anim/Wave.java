package com.sunshine.studio.base.anim;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;

import com.sunshine.studio.base.AnimLayout.Anim;

/** Created by Jack on 2020/5/28. */
public class Wave implements Anim {
  private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
  private final Xfermode mode = new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP);
  private final RectF rc = new RectF();
  private final Path pathFront = new Path();
  private final Path pathBack = new Path();
  private float percent = .03f;
  private long lastDrawTime = 0;
  private int[] aryColor = new int[] {Color.argb(30, 255, 146, 146), Color.argb(70, 252, 97, 97)};
  private int times = 1;

  public Wave() {
    paint.setStyle(Paint.Style.FILL);
  }

  public Wave setColor(int back, int front) {
    aryColor[0] = back;
    aryColor[1] = front;
    return this;
  }

  @Override
  public void draw(View v, Canvas can) {
    if (percent > 0) {
      int saveCount = can.saveLayer(rc, paint, Canvas.ALL_SAVE_FLAG);
      v.draw(can);
      drawWave(v, can);
      can.restoreToCount(saveCount);
    } else {
      v.draw(can);
    }
  }

  private void drawWave(View v, Canvas can) {
    can.translate(getWaveOffset(v.getWidth()), 0);
    paint.setXfermode(mode);
    // back
    paint.setColor(aryColor[0]);
    can.drawPath(pathBack, paint);
    // front
    paint.setColor(aryColor[1]);
    can.drawPath(pathFront, paint);
    // end
    paint.setXfermode(null);
    paint.setAlpha(255);
    if (percent < 1) {
      v.postInvalidate();
    }
  }

  private int getWaveOffset(int w) {
    if (lastDrawTime == 0) {
      lastDrawTime = SystemClock.elapsedRealtime();
    }
    if (w != 0) {
      return -(int) ((SystemClock.elapsedRealtime() - lastDrawTime) / 5 % w);
    } else {
      return 0;
    }
  }

  @Override
  public void dispatchTouchEvent(View v, MotionEvent event) {}

  @Override
  public void onSizeChanged(View v, int w, int h) {
    rc.set(0, 0, w, h);
    setPath(w, h);
    v.postInvalidate();
  }

  private void setPath(int w, int h) {
    setPath(pathFront, w, h, -h / 30);
    setPath(pathBack, w, h, h / 50);
  }

  private void setPath(Path path, int w, int h, int offsetY) {
    float height = h - h * (.1f + percent * .9f);
    path.reset();
    path.moveTo(w * 2, height); // right top
    path.lineTo(w * 2, h); // right bottom
    path.lineTo(0, h); // left bottom
    path.lineTo(0, height); // left top
    for (int i = 0; i < 4 * times; i++) {
      path.quadTo(
          w * (1 + 2 * i) / 4 / times,
          height + (i % 2 == 0 ? -1 : 1) * offsetY,
          w * (1 + i) / 2 / times,
          height);
    }
    path.close();
  }
}
