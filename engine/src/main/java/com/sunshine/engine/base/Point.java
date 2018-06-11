package com.sunshine.engine.base;

/** Created by Jack on 2018/2/14. */
public class Point<T> {
  public T x;
  public T y;

  public Point(T x, T y) {
    set(x, y);
  }

  public void set(T x, T y) {
    this.x = x;
    this.y = y;
  }

  @Override
  public String toString() {
    return x + "," + y;
  }
}
