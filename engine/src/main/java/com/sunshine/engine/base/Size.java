package com.sunshine.engine.base;

/** Created by songxiaoguang on 2018/2/23. */
public class Size<T> {
  public T width;
  public T height;

  public Size(T w, T h) {
    width = w;
    height = h;
  }

  public String toString() {
    return width + "," + height;
  }
}
