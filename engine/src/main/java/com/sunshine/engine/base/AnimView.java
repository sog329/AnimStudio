package com.sunshine.engine.base;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.sunshine.engine.base.Entity.Click;

/** Created by songxiaoguang on 2017/12/1. */
public abstract class AnimView<T extends ViewHelper> extends View {

  protected T helper = buildHelper();

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

  public boolean play(String... ary) {
    return helper.play(this, ary);
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

  /**
   * @param repeat
   * @return
   */
  public AnimView isRepeat(boolean repeat) {
    if (helper.entity != null) {
      helper.entity.repeat = repeat;
    }
    return this;
  }

  public AnimView isMute(boolean mute) {
    if (helper.entity != null) {
      helper.entity.isMute(mute);
    }
    return this;
  }

  /**
   * 在stop时是否主动recycle bitmap，默认true，即执行
   *
   * @param b
   * @return
   */
  public AnimView recycleBmp(boolean b) {
    if (helper.entity != null) {
      helper.entity.recycleBmp = b;
    }
    return this;
  }

  public AnimView setOnStop(Runnable rn) {
    if (helper.entity != null) {
      helper.entity.onStop = rn;
    }
    return this;
  }

  public void stop() {
    helper.stop();
  }

  @Override
  protected void onDraw(Canvas can) {
    super.onDraw(can);
    helper.draw(can);
  }

  public boolean setExternalBmp(String id, Bitmap bmp) {
    return helper.setExternalBmp(id, bmp);
  }

  public boolean setExternal2D(String id, Render2D.Callback cb) {
    return helper.setExternal2D(id, cb);
  }

  public boolean setOnClick(String id, Click click) {
    boolean result = helper.setOnClick(id, click);
    if (helper.entity != null && helper.entity.mapClick.size() > 0) {
      setClickable(true);
    } else {
      setClickable(false);
    }
    return result;
  }

  public void setOnError(Function<String> function) {
    helper.setOnError(function);
  }

  private long actionDownTime = Tool.NONE;

  @Override
  public boolean onTouchEvent(MotionEvent me) {
    if (helper.entity != null && helper.entity.mapClick.size() > 0) {
      if (me.getAction() == MotionEvent.ACTION_DOWN) {
        actionDownTime = Tool.getTime();
      } else if (me.getAction() == MotionEvent.ACTION_UP && Tool.getTime() - actionDownTime < 200) {
        onClick((int) me.getX(), (int) me.getY());
      }
    }
    return super.onTouchEvent(me);
  }

  protected abstract void onClick(int x, int y);
}
