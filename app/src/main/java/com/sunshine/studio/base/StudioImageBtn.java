package com.sunshine.studio.base;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

/** Created by songxiaoguang on 2017/12/6. */
public class StudioImageBtn extends AppCompatImageView {
  private boolean checked = false;
  public boolean autoSize = true;

  public StudioImageBtn(Context context) {
    super(context);
  }

  public StudioImageBtn(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public StudioImageBtn(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  public static int getSize() {
    return StudioTool.getBtnHeight() + 20;
  }

  @Override
  protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    super.onSizeChanged(w, h, oldw, oldh);
    if (autoSize) {
      StudioTool.square(this, w, h, getSize());
    }
  }

  public boolean isChecked() {
    return checked;
  }

  public void mapValue(boolean b, Studio.MapValue<Boolean> mapValue) {
    setOnClickListener(null);
    checked = b;
    setOnClickListener(
        v -> {
          checked = !checked;
          mapValue.update(checked);
        });
    mapValue.update(b);
  }
}
