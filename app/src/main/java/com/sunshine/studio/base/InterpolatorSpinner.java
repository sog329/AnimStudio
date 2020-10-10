package com.sunshine.studio.base;

import android.app.Dialog;
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

  public void mapValue(String now, StudioSpinner.Callback callback, Studio studio) {
    StudioEt<Integer> et = findViewById(R.id.num);
    StudioSpinner sp = findViewById(R.id.type);
    sp.setOnLongClickListener(
        v -> {
          Dialog dlg = studio.dlgInterpolator;
          dlg.show();

          InterpolatorView iv = dlg.findViewById(R.id.iv);
          String name =
              ((TextView) sp.getSelectedView().findViewById(R.id.tv)).getText()
                  + "_"
                  + et.getText();
          iv.setInterpolator(name);

          StudioTv tv = dlg.findViewById(R.id.name);
          tv.setText(name);
          return true;
        });
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
