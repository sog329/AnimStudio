package com.sunshine.engine.base;

/** Created by songxiaoguang on 2017/9/13. */
public class ProcessInt extends ProcessObj<Integer> {

  public ProcessInt(Integer from, Integer to) {
    super(from, to);
  }

  public ProcessInt(Integer from, Integer to, String type) {
    super(from, to, type);
  }

  @Override
  public ProcessInt set(Integer from, Integer to) {
    this.from = from;
    this.to = to;
    delta = to - from;
    now = from;
    return this;
  }

  @Override
  public Integer get(float percent) {
    now = from + (int) (delta * getInterpolation(percent));
    return now;
  }

  @Override
  public void dif(Integer dif) {
    from += dif;
    to += dif;
  }
}
