package com.sunshine.studio.base;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.sunshine.engine.base.InterpolatorHelper;
import com.sunshine.studio.R;

/** Created by Jack on 2018/6/9. */
public class InterpolatorView extends StudioTv {
  private String name = null;
  private Paint paint = new Paint();
  private Path path = null;
  private boolean inDetail = false;

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
    if (inDetail) {
      invalidate();
    } else {
      updatePath(name);
    }
  }

  public void inDetail(boolean b) {
    inDetail = b;
    invalidate();
  }

  @Override
  protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    super.onSizeChanged(w, h, oldw, oldh);
    updatePath(name);
    if (inDetail) {
      invalidate();
    } else {
      updatePath(name);
    }
  }

  private void updatePath(String name) {
    if (path == null) {
      path = new Path();
    } else {
      path.reset();
    }
    if (name != null) {
      int h2 = getHeight() / 2;
      int h3 = getHeight() / 3;
      final int n = getWidth() / 2;
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
    if (name != null) {
      int w = getWidth();
      int h2 = getHeight() / 2;
      paint.setARGB(50, 255, 255, 255);
      if (inDetail) {
        // x轴
        can.drawLine(0, h2, w, h2, paint);
        // y轴
        can.drawLine(0, 0, 0, getHeight(), paint);
        // y=1
        int h6 = getHeight() / 6;
        can.drawLine(0, h6, w, h6, paint);
        // y=-1
        can.drawLine(0, getHeight() - h6, w, getHeight() - h6, paint);
        // 曲线
        String type = name;
        if (name.indexOf("_") > 0) {
          String[] ary = name.split("_");
          type = ary[0];
        }

        paint.setColor(getResources().getColor(R.color.interpolator_2));
        updatePath(type + "_2");
        can.drawPath(path, paint);

        paint.setColor(getResources().getColor(R.color.interpolator_3));
        updatePath(type + "_3");
        can.drawPath(path, paint);

        paint.setColor(getResources().getColor(R.color.interpolator_4));
        updatePath(type + "_4");
        can.drawPath(path, paint);

        paint.setColor(getResources().getColor(R.color.interpolator_8));
        updatePath(type + "_8");
        can.drawPath(path, paint);

        paint.setColor(Color.WHITE);
        updatePath(name);
      } else {
        // x轴
        can.drawLine(0, h2, w, h2, paint);
        // 曲线
        paint.setAlpha(255);
        paint.setColor(Color.WHITE);
      }
      can.drawPath(path, paint);
    }
  }
}
