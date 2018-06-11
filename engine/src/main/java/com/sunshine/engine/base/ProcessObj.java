package com.sunshine.engine.base;

/** Created by songxiaoguang on 2017/9/13. */
public abstract class ProcessObj<T> {
  protected T from;
  protected T to;
  protected T delta;
  private InterpolatorHelper interpolatorHelper = new InterpolatorHelper();

  public ProcessObj(T from, T to) {
    set(from, to, InterpolatorType.linear.toString());
  }

  public ProcessObj(T from, T to, String name) {
    set(from, to, name);
  }

  public abstract ProcessObj<T> set(T from, T to);

  public ProcessObj<T> set(T from, T to, String name) {
    set(from, to).setInterpolator(name);
    return this;
  }

  public abstract T get(float percent);

  public T random() {
    return get((float) Math.random());
  }

  public T getFrom() {
    return from;
  }

  public T getTo() {
    return to;
  }

  public T getDelta() {
    return delta;
  }

  public ProcessObj<T> setFrom(T from) {
    this.from = from;
    set(from, to);
    return this;
  }

  public ProcessObj<T> setTo(T to) {
    this.to = to;
    set(from, to);
    return this;
  }

  public ProcessObj<T> setInterpolator(String name) {
    interpolatorHelper.setName(name);
    return this;
  }

  public String toString() {
    return from + "," + to;
  }

  public String getInterpolatorName() {
    return interpolatorHelper.getName();
  }

  public float getInterpolation(float percent) {
    return interpolatorHelper.getInterpolation(percent);
  }
}
