package com.sunshine.studio.base;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

import com.sunshine.studio.R;

/** Created by songxiaoguang on 2017/12/4. */
public class StudioTv extends android.support.v7.widget.AppCompatTextView {
  private boolean checked = false;

  public StudioTv(Context context) {
    super(context);
    init(null);
  }

  public StudioTv(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    init(attrs);
  }

  public StudioTv(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(attrs);
  }

  private void init(AttributeSet attrs) {
    initSize(this, getScale(getContext(), attrs));
  }

  public static float getScale(Context context, AttributeSet attrs) {
    float scale = 1;
    if (attrs != null) {
      TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.StudioTv);
      scale = ta.getFloat(R.styleable.StudioTv_scale, 1);
      ta.recycle();
    }
    return scale;
  }

  public static void initSize(TextView tv, float k) {
    tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, StudioTool.getBtnHeight() * k);
    tv.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
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

  @Override
  public void setOnClickListener(@Nullable OnClickListener l) {
    super.setOnClickListener(l);
    int len = StudioImageBtn.getSize();
    setMinHeight(len);
    setMinWidth(len);
  }
}
