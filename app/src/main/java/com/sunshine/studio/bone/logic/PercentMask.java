package com.sunshine.studio.bone.logic;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.sunshine.studio.base.StudioTool;

/** Created by Jack on 2018/3/5. */
public class PercentMask extends View {
  private final int color = Color.parseColor("#55FFFFFF");
  private final int start = StudioTool.getBtnHeight() * 3 + 100;

  private float percent = 0;
  private int length = 0;
  private int tmpLen = 0;

  public PercentMask(Context context) {
    super(context);
  }

  public PercentMask(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
  }

  public PercentMask(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  public void setPercent(float percent) {
    this.percent = percent;
    calculate();
    invalidate();
  }

  private void calculate() {
    tmpLen = start + (int) (length * percent);
  }

  @Override
  public boolean onTouchEvent(MotionEvent me) {
    return false;
  }

  @Override
  protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    super.onSizeChanged(w, h, oldw, oldh);
    length = w - start - 35 - StudioTool.getBtnHeight();
    calculate();
  }

  @Override
  public void onDraw(Canvas can) {
    StudioRender2D.drawLine(can, tmpLen, getHeight(), color);
  }
}
