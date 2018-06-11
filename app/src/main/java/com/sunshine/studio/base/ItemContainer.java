package com.sunshine.studio.base;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/** Created by songxiaoguang on 2018/1/4. */
public class ItemContainer extends FrameLayout {
  public ItemContainer(@NonNull Context context) {
    super(context);
  }

  public ItemContainer(@NonNull Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
  }

  public ItemContainer(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @Override
  protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    super.onSizeChanged(w, h, oldw, oldh);
    int width = h / 16 * 10;
    if (w != width) {
      post(
          () -> {
            ViewGroup.LayoutParams lp = getLayoutParams();
            lp.width = width;
            setLayoutParams(lp);
          });
    }
  }
}
