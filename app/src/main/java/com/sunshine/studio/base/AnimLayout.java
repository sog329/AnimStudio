package com.sunshine.studio.base;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

/** Created by Jack on 2020/5/9. */
public class AnimLayout extends FrameLayout {
  private List<Anim> lstAnim = new ArrayList<>();
  private List<Anim> lstTemp = new ArrayList<>();

  public AnimLayout(Context context) {
    super(context);
  }

  public AnimLayout(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
  }

  public AnimLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @Override
  public void draw(Canvas can) {
    if (lstAnim.size() > 0) {
      Anim anim = lstAnim.remove(0);
      lstTemp.add(anim);
      anim.draw(this, can);
    } else {
      if (lstTemp.size() > 0) {
        lstAnim.addAll(lstTemp);
        lstTemp.clear();
      }
      super.draw(can);
    }
  }

  public AnimLayout addAnim(Anim anim) {
    lstAnim.add(anim);
    return this;
  }

  @Override
  protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    super.onSizeChanged(w, h, oldw, oldh);
    for (Anim anim : lstAnim) {
      anim.onSizeChanged(this, w, h);
    }
  }

  @Override
  public boolean dispatchTouchEvent(MotionEvent event) {
    for (Anim anim : lstAnim) {
      anim.dispatchTouchEvent(this, event);
    }
    return super.dispatchTouchEvent(event);
  }

  public interface Anim {
    void draw(View v, Canvas can);

    void dispatchTouchEvent(View v, MotionEvent event);

    void onSizeChanged(View v, int w, int h);
  }
}
