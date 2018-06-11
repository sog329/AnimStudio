package com.sunshine.studio.base;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.ViewGroup;

/** Created by songxiaoguang on 2017/12/6. */
public class StudioImageBtn extends AppCompatImageView {

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

  @Override
  protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    super.onSizeChanged(w, h, oldw, oldh);
    if (autoSize) {
      int size = StudioTool.getBtnHeight() + 20;
      if (w != size || w != h) {
        post(
            () -> {
              ViewGroup.LayoutParams lp = getLayoutParams();
              lp.width = size;
              lp.height = size;
              setLayoutParams(lp);
              setPadding(size / 5, size / 5, size / 5, size / 5);
            });
      }
    }
  }
}
