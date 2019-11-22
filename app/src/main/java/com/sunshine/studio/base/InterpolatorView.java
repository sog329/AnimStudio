package com.sunshine.studio.base;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.sunshine.engine.base.InterpolatorHelper;

/** Created by Jack on 2018/6/9. */
public class InterpolatorView extends StudioTv {
  private String name = null;
  private Paint paint = new Paint();
  private Path path = null;

  {
    paint.setAntiAlias(true);
    paint.setStyle(Paint.Style.STROKE);
    paint.setStrokeWidth(3);
  }

  public InterpolatorView(Context context) {
    super(context);
  }

  public InterpolatorView(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
  }

  public InterpolatorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  public void setInterpolator(String str) {
    name = str;
    updatePath();
  }

  @Override
  protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    super.onSizeChanged(w, h, oldw, oldh);
    updatePath();
  }

  private void updatePath() {
    if (path == null) {
      path = new Path();
    } else {
      path.reset();
    }
    if (name != null) {
      int h2 = getHeight() / 2;
      int h3 = getHeight() / 3;
      final int n = 200;
      for (int i = 0; i < n; i++) {
        float percent = 1f * i / (n - 1);
        float x = getWidth() * percent;
        float y = h2 - h3 * InterpolatorHelper.obtain(name).getInterpolation(percent);
        if (i == 0) {
          path.moveTo(x, y);
        } else {
          path.lineTo(x, y);
        }
      }
    }
    invalidate();
  }

  @Override
  public void onDraw(Canvas can) {
    super.onDraw(can);
    if (name != null && path != null) {
      int w = getWidth();
      int h2 = getHeight() / 2;
      // x轴
      paint.setColor(Color.DKGRAY);
      can.drawLine(0, h2, w, h2, paint);
      // y=1
      int h6 = getHeight() / 6;
      paint.setColor(Color.DKGRAY);
      can.drawLine(0, h6, w, h6, paint);
      // 曲线
      paint.setColor(Color.WHITE);
      can.drawPath(path, paint);
      invalidate();
    }
  }
}
