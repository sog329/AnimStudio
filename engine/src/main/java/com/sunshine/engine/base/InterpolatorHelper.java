package com.sunshine.engine.base;

import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import java.util.HashMap;
import java.util.Map;

/** Created by songxiaoguang on 2017/9/30. */
public class InterpolatorHelper {
  private static final Map<String, Interpolator> mapInterpolator = new HashMap<>();

  private String name = null;
  private Interpolator interpolator = null;

  public InterpolatorHelper() {
    setName(InterpolatorType.linear.toString());
  }

  public void setName(String name) {
    if (name != null) {
      this.name = name;
      interpolator = obtain(name);
    }
  }

  public static Interpolator obtain(String name) {
    Interpolator it = mapInterpolator.get(name);
    if (it == null) {
      String[] ary = name.split("_");
      int param = ary.length == 2 ? Integer.parseInt(ary[1]) : 2;
      it = InterpolatorType.valueOf(ary[0]).build(param);
      if (it == null) {
        it = new LinearInterpolator();
      }
      mapInterpolator.put(name, it);
    }
    return it;
  }

  public String getName() {
    return name;
  }

  public float getInterpolation(float percent) {
    return interpolator.getInterpolation(percent);
  }
}
