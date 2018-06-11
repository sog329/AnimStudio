package com.sunshine.engine.base;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.io.File;

/** Created by songxiaoguang on 2017/12/1. */
public abstract class AnimView<T extends ViewHelper> extends View {
  protected T helper = buildHelper();
  private boolean canTouch = false;

  public AnimView(Context context) {
    super(context);
  }

  public AnimView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public AnimView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  public abstract T buildHelper();

  public boolean play(String folderPath) {
    if (folderPath == null) {
      return false;
    } else {
      if (!folderPath.endsWith(File.separator)) {
        folderPath += File.separator;
      }
      String configPath = folderPath + "config.xml";
      String picPath = folderPath + "pic";
      return play(configPath, picPath);
    }
  }

  public boolean play(String configPath, String picPath, String soundPath) {
    return helper.play(this, configPath, picPath, soundPath);
  }

  public boolean play(String configPath, String picPath) {
    return play(configPath, picPath, null);
  }

  @Override
  protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
    super.onLayout(changed, left, top, right, bottom);
    helper.resize(
        getPaddingLeft(),
        getPaddingTop(),
        right - left - getPaddingRight(),
        bottom - top - getPaddingBottom());
  }

  public void canTouch(boolean can) {
    canTouch = can;
  }

  public void autoStop(boolean auto) {
    if (helper.entity != null) {
      helper.entity.autoStop = auto;
    }
  }

  public void isRepeat(boolean repeat) {
    if (helper.entity != null) {
      helper.entity.repeat = repeat;
    }
  }

  public void isMute(boolean mute) {
    if (helper.entity != null) {
      helper.entity.isMute(mute);
    }
  }

  @Override
  public void setOnClickListener(OnClickListener l) {
    super.setOnClickListener(l);
    canTouch(true);
  }

  public void stop() {
    helper.stop();
  }

  @Override
  public boolean onTouchEvent(MotionEvent me) {
    if (canTouch) {
      return super.onTouchEvent(me);
    } else {
      return false;
    }
  }

  @Override
  protected void onDraw(Canvas can) {
    super.onDraw(can);
    helper.draw(can);
  }

  @Override
  protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    if (helper.entity != null && helper.entity.autoStop) {
      stop();
    }
  }

  public boolean setExternalBmp(String id, Bitmap bmp) {
    boolean add = helper.setExternalBmp(id, bmp);
    if (add) {
      invalidate();
    }
    return add;
  }
}
