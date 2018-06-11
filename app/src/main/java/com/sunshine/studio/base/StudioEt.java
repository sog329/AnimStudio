package com.sunshine.studio.base;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.inputmethod.EditorInfo;

import com.sunshine.engine.base.Tool;
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
    setInputType(EditorInfo.TYPE_CLASS_PHONE);
    float scale = 1;
    if (attrs != null) {
      TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.StudioTv);
      scale = ta.getFloat(R.styleable.StudioTv_scale, 1);
    }
    StudioTv.initSize(this, scale);
  }

  public StudioEt mapValue(T value, Studio.MapValue<T> mapValue) {
    removeTextChangedListener((TextWatcher) getTag());
    setText(String.valueOf(value));
    TextWatcher textWatcher =
        new TextWatcher() {
          @Override
          public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

          @Override
          public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (value instanceof Float) {
              try {
                mapValue.update((T) new Float(Float.parseFloat(charSequence.toString())));
              } catch (Exception e) {
                Tool.log(e);
              }
            } else if (value instanceof Integer) {
              try {
                mapValue.update((T) new Integer(Integer.parseInt(charSequence.toString())));
              } catch (Exception e) {
                Tool.log(e);
              }
            } else {
              Tool.log("Error type");
            }
          }

          @Override
          public void afterTextChanged(Editable editable) {}
        };
    addTextChangedListener(textWatcher);
    setTag(textWatcher);
    return this;
  }
}
