package com.sunshine.engine.base;

import android.view.animation.AccelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;

/** Created by songxiaoguang on 2018/1/5. */
public enum InterpolatorType {
  linear("linear", s -> new LinearInterpolator()),
  accelerate("accelerate", s -> new AccelerateInterpolator()),
  decelerate("decelerate", s -> new DecelerateInterpolator()),
  overshoot("overshoot", s -> new OvershootInterpolator()),
  bounce("bounce", s -> new BounceInterpolator()),
  spring("spring", s -> p -> 4 * p * (1 - p)),
  shake("shake", s -> p -> (float) Math.pow(1 - p, 2) * (float) Math.sin(2 * Math.PI * s / 2 * p)),
  resonance(
      "resonance",
      s ->
          p ->
              (1 - (float) Math.pow(1 - p, 2))
                  * (float) Math.sin(2 * Math.PI * s / 2 * p * (1 - Math.pow(1 - p, 2)))),
  sin("sin", s -> p -> (float) Math.sin(2 * Math.PI * s / 2 * p)),
  cos("cos", s -> p -> (float) Math.cos(2 * Math.PI * s / 2 * p)),
  triangle(
      "triangle",
      s ->
          p -> {
            float fTotal = 2 * p * s;
            int iTotal = (int) fTotal;
            if (iTotal % 2 == 0) {
              return fTotal - iTotal;
            } else {
              return 1 - fTotal + iTotal;
            }
          });
  private final String text;
  private final Callback callback;

  InterpolatorType(String text, Callback callback) {
    this.text = text;
    this.callback = callback;
  }

  @Override
  public String toString() {
    return text;
  }

  protected Interpolator build(int param) {
    return callback.build(param);
  }

  public Interpolator obtain() {
    return InterpolatorHelper.obtain(toString());
  }

  public Interpolator obtain(int param) {
    return InterpolatorHelper.obtain(toString() + "_" + param);
  }

  private interface Callback {
    Interpolator build(int param);
  }
}
