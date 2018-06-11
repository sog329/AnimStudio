package com.sunshine.studio.base;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sunshine.studio.R;

/** Created by Jack on 2018/6/8. */
public class InterpolatorSpinner extends LinearLayout {
  public InterpolatorSpinner(Context context) {
    super(context);
  }

  public InterpolatorSpinner(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
  }

  public InterpolatorSpinner(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  public void interpolator(String now, StudioSpinner.Callback callback) {
    StudioEt<Integer> et = findViewById(R.id.num);
    StudioSpinner sp = findViewById(R.id.type);
    String type = now;
    String num = "2";
    if (now.indexOf("_") > 0) {
      String[] ary = now.split("_");
      type = ary[0];
      num = ary[1];
    }
    sp.interpolator(type, s -> callback.onChoice(s + "_" + et.getText()));
    et.mapValue(
        Integer.parseInt(num),
        n ->
            callback.onChoice(
                ((TextView) sp.getSelectedView().findViewById(R.id.tv)).getText() + "_" + n));
  }
}
