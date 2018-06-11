package com.sunshine.engine.bone.logic;

import com.sunshine.engine.base.Tool;

/** Created by songxiaoguang on 2017/11/30. */
public class Duration {
  private float from = 0;
  private float to = 0;
  private float delta = 0;

  public void set(float from, float to) {
    this.from = from;
    this.to = to;
    delta = to - from;
  }

  public boolean isIn(float percent, boolean isLast) {
    boolean in = false;
    if (isLast) {
      if (percent >= from && percent <= to) {
        in = true;
      }
    } else {
      if (percent >= from && percent < to) {
        in = true;
      }
    }
    return in;
  }

  public float getPercent(float percent) {
    float p = 0;
    if (!Tool.equalsZero(delta)) {
      p = (percent - from) / delta;
    }
    return p;
  }

  public float getDelta() {
    return delta;
  }

  public float getFrom() {
    return from;
  }

  public float getTo() {
    return to;
  }

  public void setFrom(float from) {
    this.from = from;
    set(from, to);
  }

  public void setTo(float to) {
    this.to = to;
    set(from, to);
  }

  public String toString() {
    return from + "," + to;
  }
}
