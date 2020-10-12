package com.sunshine.studio.bone.logic;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.sunshine.engine.base.Tool;
import com.sunshine.studio.base.StudioImageBtn;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/** Created by songxiaoguang on 2017/12/3. */
public class BoneIv extends StudioImageBtn {
  protected WeakReference<Bitmap> bmp = null;
  protected List<Rect> lstRcBmp = new ArrayList<>();
  protected Rect rcView = new Rect();
  protected Rect rcDraw = new Rect();
  private long firstDrawTime = 0;

  public BoneIv(Context context) {
    super(context);
  }

  public BoneIv(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
  }

  public BoneIv(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  public void setBmp(Bitmap bmp, List<Rect> lst) {
    this.bmp = new WeakReference<>(bmp);
    lstRcBmp.clear();
    if (lst != null) {
      lstRcBmp.addAll(lst);
    }
    firstDrawTime = 0;
    mergeRcDraw();
  }

  @Override
  protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    super.onSizeChanged(w, h, oldw, oldh);
    rcView.set(0, 0, w, h);
    mergeRcDraw();
  }

  private void mergeRcDraw() {
    if (lstRcBmp.size() > 0) {
      Rect rcBmp = lstRcBmp.get(0);
      if (1f * rcBmp.width() / rcBmp.height() > 1f * rcView.width() / rcView.height()) {
        int space =
            (rcView.height() - (int) (1f * rcView.width() / rcBmp.width() * rcBmp.height())) / 2;
        rcDraw.set(0, space, rcView.width(), rcView.height() - space);
      } else {
        int space =
            (rcView.width() - (int) (1f * rcView.height() / rcBmp.height() * rcBmp.width())) / 2;
        rcDraw.set(space, 0, rcView.width() - space, rcView.height());
      }
    }
    invalidate();
  }

  @Override
  public void onDraw(Canvas can) {
    super.onDraw(can);
    if (bmp != null && lstRcBmp.size() > 0) {
      Bitmap bitmap = bmp.get();
      if (bitmap != null && !bitmap.isRecycled()) {
        Rect rcBmp = lstRcBmp.get(0);
        if (lstRcBmp.size() != 1) {
          if (firstDrawTime == 0) {
            firstDrawTime = Tool.getTime();
          } else {
            // 默认按照24帧展示
            int index = (int) ((Tool.getTime() - firstDrawTime) / (1000 / 24)) % lstRcBmp.size();
            rcBmp = lstRcBmp.get(index);
          }
          invalidate();
        }
        can.drawBitmap(bitmap, rcBmp, rcDraw, null);
      }
    }
  }
}
