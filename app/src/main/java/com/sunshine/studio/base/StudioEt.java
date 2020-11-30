package com.sunshine.studio.base;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.inputmethod.EditorInfo;

import com.sunshine.studio.R;

/** Created by songxiaoguang on 2017/12/4. */
public class StudioEt<T> extends android.support.v7.widget.AppCompatEditText {
  public StudioEt(Context context) {
    super(context);
    init(null);
  }

  public StudioEt(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    init(attrs);
  }

  public StudioEt(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(attrs);
  }

  protected void init(AttributeSet attrs) {
    float scale = 1;
    if (attrs != null) {
      TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.StudioTv);
      scale = ta.getFloat(R.styleable.StudioTv_scale, 1);
    }
    setInputType(EditorInfo.TYPE_CLASS_NUMBER);
    StudioTv.initSize(this, scale);
  }

  public StudioEt map(Integer value, Studio.MapValue<Integer> mapValue) {
    setInputType(EditorInfo.TYPE_CLASS_NUMBER);
    return map(
        value,
        mapValue,
        c -> {
          if (c.length() == 0) {
            return 0;
          } else {
            return Integer.parseInt(c.toString());
          }
        });
  }

  public StudioEt map(Float value, Studio.MapValue<Float> mapValue) {
    setInputType(EditorInfo.TYPE_CLASS_NUMBER);
    return map(
        value,
        mapValue,
        c -> {
          if (c.length() == 0) {
            return 0.0f;
          } else {
            float f = 0f;
            try {
              f = Float.parseFloat(c.toString());
            } catch (Exception e) {
              // do nothing
            }
            return f;
          }
        });
  }

  public StudioEt map(String value, Studio.MapValue<String> mapValue) {
    setInputType(EditorInfo.TYPE_CLASS_TEXT);
    return map(
        value,
        mapValue,
        c -> {
          if (c.length() == 0) {
            return null;
          } else {
            return c.toString();
          }
        });
  }

  private interface parse {
    Object parse(CharSequence c);
  }

  private StudioEt map(Object value, Studio.MapValue mapValue, parse p) {
    removeTextChangedListener((TextWatcher) getTag());
    setText(value == null ? null : String.valueOf(value));
    TextWatcher textWatcher =
        new TextWatcher() {
          @Override
          public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

          @Override
          public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            mapValue.update(p.parse(charSequence));
          }

          @Override
          public void afterTextChanged(Editable editable) {}
        };
    addTextChangedListener(textWatcher);
    setTag(textWatcher);
    return this;
  }
}
