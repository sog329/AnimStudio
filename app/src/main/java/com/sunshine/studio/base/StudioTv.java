package com.sunshine.studio.base;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

import com.sunshine.studio.R;

/** Created by songxiaoguang on 2017/12/4. */
public class StudioTv extends android.support.v7.widget.AppCompatTextView {

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

  protected void init(AttributeSet attrs) {
    float scale = 1;
    if (attrs != null) {
      TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.StudioTv);
      scale = ta.getFloat(R.styleable.StudioTv_scale, 1);
    }
    initSize(this, scale);
  }

  public static void initSize(TextView tv, float k) {
    tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, StudioTool.getBtnHeight() * k);
    tv.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
  }
}
