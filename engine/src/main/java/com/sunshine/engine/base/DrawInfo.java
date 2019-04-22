package com.sunshine.engine.base;

import android.graphics.Point;
import android.graphics.Rect;

public class DrawInfo {
  // 初始值
  public Rect rcSrc = new Rect();
  public Point ptSrc = new Point();
  // 最终值
  public Rect rcDst = new Rect();
  public float rt = 0;
  public Point ptDst = new Point();

  public int alpha = 0;
}
