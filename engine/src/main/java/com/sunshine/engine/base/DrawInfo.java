package com.sunshine.engine.base;

import android.graphics.PointF;
import android.graphics.RectF;

public class DrawInfo {
  // 初始值
  public RectF rcSrc = new RectF();
  public PointF ptSrc = new PointF();
  // 最终值
  public RectF rcDst = new RectF();
  public float rt = 0;
  public PointF ptDst = new PointF();

  public int alpha = 0;
}
