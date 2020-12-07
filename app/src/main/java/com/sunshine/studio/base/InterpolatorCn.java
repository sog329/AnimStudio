package com.sunshine.studio.base;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.sunshine.engine.base.Function;
import com.sunshine.studio.R;
import com.sunshine.studio.bone.logic.BoneStudio;

/** Created by Jack on 2018/6/8. */
public class InterpolatorCn extends LinearLayout {
  public InterpolatorCn(Context context) {
    super(context);
  }

  public InterpolatorCn(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
  }

  public InterpolatorCn(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  public void mapValue(String now, Function<String> func, Studio studio) {
    StudioEt<Integer> et = findViewById(R.id.num);
    InterpolatorView tvType = findViewById(R.id.type);
    tvType.setText(now);
    tvType.setInterpolator(now);
    tvType.setOnClickListener(
        v -> {
          Dialog dlg = studio.dlgInterpolator;
          dlg.show();

          InterpolatorRv rv = dlg.findViewById(R.id.rv);
          StudioTv btn = dlg.findViewById(R.id.select);
          btn.setOnClickListener(
              s -> {
                String type = rv.select();
                tvType.setText(type);
                tvType.setInterpolator(type);
                func.call(type + "_" + et.getText());
                dlg.dismiss();
              });

          InterpolatorView iv = dlg.findViewById(R.id.iv);
          Runnable onSelect =
              () -> {
                String s = rv.select() + "_" + et.getText();
                iv.setInterpolator(s);
                btn.setText(s);
              };
          rv.load(
              (String) tvType.getText(),
              onSelect,
              studio instanceof BoneStudio ? R.drawable.bg_btn : R.drawable.bg_btn_p);
        });
    String num = "2";
    if (now.indexOf("_") > 0) {
      String[] ary = now.split("_");
      num = ary[1];
    }
    et.map(Integer.parseInt(num), n -> func.call(tvType.getText() + "_" + n));
  }
}
