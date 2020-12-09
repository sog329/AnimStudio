package com.sunshine.studio.bone.logic;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;

import com.sunshine.engine.base.Point;
import com.sunshine.engine.base.Tool;
import com.sunshine.studio.base.StudioImageBtn;
import com.sunshine.studio.base.StudioTool;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/** Created by songxiaoguang on 2017/12/3. */
public class SkinIv extends StudioImageBtn {
  protected WeakReference<Bitmap> bmp = null;
  protected List<Rect> lstRcBmp = new ArrayList<>();
  protected Rect rcView = new Rect();
  protected RectF rcDraw = new RectF();
  private long firstDrawTime = 0;
  public boolean drawRc = false;
  private Point<Float> pt = null;
  private TextPaint tp = new TextPaint();
  private static final int K = 5;

  public SkinIv(Context context) {
    super(context);
  }

  public SkinIv(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
  }

  public SkinIv(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  public void setBmp(Bitmap bmp, List<Rect> lst) {
    lstRcBmp.clear();
    if (bmp == null) {
      this.bmp = null;
    } else {
      this.bmp = new WeakReference<>(bmp);
      if (lst != null) {
        lstRcBmp.addAll(lst);
      } else {
        lstRcBmp.add(new Rect(0, 0, bmp.getWidth(), bmp.getHeight()));
      }
      firstDrawTime = 0;
      mergeRcDraw();
    }
  }

  public void setPt(float x, float y) {
    if (pt == null) {
      pt = new Point<>(x, y);
    } else {
      pt.set(x, y);
    }
    mergeRcDraw();
    invalidate();
  }

  @Override
  protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    super.onSizeChanged(w, h, oldw, oldh);
    rcView.set(0, 0, w, h);
    mergeRcDraw();
  }

  private void mergeRcDraw() {
    if (lstRcBmp.size() > 0 && rcView.width() > 0 && rcView.height() > 0) {
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
      float h = pt == null ? 0 : rcDraw.width() / K;
      float v = pt == null ? 0 : rcDraw.height() / K;
      rcDraw.left += h;
      rcDraw.right -= h;
      rcDraw.top += v;
      rcDraw.bottom -= v;
      invalidate();
    }
  }

  @Override
  public void onDraw(Canvas can) {
    super.onDraw(can);
    if (bmp != null && lstRcBmp.size() > 0) {
      Bitmap bitmap = bmp.get();
      if (bitmap != null && !bitmap.isRecycled()) {
        if (drawRc) {
          StudioRender2D.draw(can, rcDraw, Color.rgb(0, 0, 0), 255, true, 0, 0, 0);
        }
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
        if (pt != null) {
          float scale = 1f * rcDraw.width() / rcBmp.width();
          PointF p = new PointF(rcDraw.centerX() + scale * pt.x, rcDraw.centerY() + scale * pt.y);
          int c = Color.argb(255, 125, 125, 125);
          StudioRender2D.drawCross(can, p, c);
          int size = StudioTool.getBtnHeight() / 2;
          StudioRender2D.paint.setTextSize(size);
          String s = String.valueOf(rcBmp.width());
          can.drawText(
              s,
              rcDraw.centerX() - StudioRender2D.paint.measureText(s) / 2,
              rcDraw.bottom + size,
              StudioRender2D.paint);
          can.drawText(
              String.valueOf(rcBmp.height()),
              rcDraw.right + 10,
              rcDraw.centerY() + size / 3,
              StudioRender2D.paint);
        }
      }
    }
  }
}
