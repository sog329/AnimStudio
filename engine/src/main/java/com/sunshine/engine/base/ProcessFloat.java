package com.sunshine.engine.base;

/** Created by songxiaoguang on 2017/9/13. */
public class ProcessFloat extends ProcessObj<Float> {

  public ProcessFloat(Float from, Float to) {
    super(from, to);
  }

  public ProcessFloat(Float from, Float to, String type) {
    super(from, to, type);
  }

  @Override
  public ProcessFloat set(Float from, Float to) {
    this.from = from;
    this.to = to;
    delta = to - from;
    now = from;
    return this;
  }

  @Override
  public Float get(float percent) {
    now = from + delta * getInterpolation(percent);
    return now;
  }

  @Override
  public void dif(Float dif) {
    from += dif;
    to += dif;
  }
}
