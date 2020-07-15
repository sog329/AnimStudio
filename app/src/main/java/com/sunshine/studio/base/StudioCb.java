package com.sunshine.studio.base;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

/** Created by songxiaoguang on 2017/12/4. */
public class StudioCb extends android.support.v7.widget.AppCompatCheckBox {
  public StudioCb(Context context) {
    super(context);
    init();
  }

  public StudioCb(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public StudioCb(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }

  private void init() {
    StudioTv.initSize(this, 1);
  }

  public void mapValue(boolean b, Studio.MapValue<Boolean> mapValue) {
    setOnCheckedChangeListener(null);
    setChecked(b);
    setOnCheckedChangeListener((btn, checked) -> mapValue.update(checked));
  }
}
