package com.sunshine.studio.bone.logic;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.sunshine.studio.R;

/** Created by Jack on 2020/10/9. */
public class ExtendIv extends BoneIv {
  private Rect rcLineY = new Rect();
  private Integer extendY = null;
  private boolean inExtendY = false;
  private Paint paint = null;

  public ExtendIv(Context context) {
    super(context);
  }

  public ExtendIv(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
  }

  public ExtendIv(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  public void inExtendY(boolean b) {
    inExtendY = b;
    invalidate();
  }

  public void setExtendY(Integer y) {
    if (y == null || y == 0) {
      extendY = null;
      paint = null;
    } else {
      extendY = y;
      paint = new Paint();
      paint.setStyle(Paint.Style.FILL);
      paint.setColor(getResources().getColor(R.color.btn_bg));
      paint.setAlpha(102);
    }
    invalidate();
  }

  @Override
  public void onDraw(Canvas can) {
    if (inExtendY && extendY != null) {
      if (bmp != null) {
        Bitmap bitmap = bmp.get();
        if (bitmap != null && !bitmap.isRecycled()) {
          if (0 < extendY && extendY <= rcBmp.height()) {
            float scale = 1f * rcDraw.width() / rcBmp.width();
            int topH = 0;
            int bottomH = 0;
            Rect rb = new Rect();
            Rect rd = new Rect();
            // up
            if (extendY > 1) {
              rb.set(rcBmp.left, rcBmp.top, rcBmp.right, rcBmp.top + extendY - 1);
              topH = (int) (rcView.top + rb.height() * scale);
              rd.set(rcView.left, rcView.top, rcView.right, topH);
              can.drawBitmap(bitmap, rb, rd, null);
            }
            // down
            if (extendY < rcBmp.height()) {
              rb.set(rcBmp.left, rcBmp.top + extendY + 1, rcBmp.right, rcBmp.bottom);
              bottomH = rcView.bottom - (int) (rb.height() * scale);
              rd.set(rcView.left, bottomH, rcView.right, rcView.bottom);
              can.drawBitmap(bitmap, rb, rd, null);
            }
            // middle
            rb.set(rcBmp.left, rcBmp.top + extendY, rcBmp.right, rcBmp.top + extendY + 1);
            rd.set(rcView.left, topH, rcView.right, bottomH);
            can.drawBitmap(bitmap, rb, rd, null);
            can.drawRect(rd, paint);
          }
        }
      }
    } else {
      super.onDraw(can);
      if (bmp != null) {
        Bitmap bitmap = bmp.get();
        if (bitmap != null && !bitmap.isRecycled()) {
          if (extendY != null) {
            float h = 1f * rcDraw.height() / rcBmp.height();
            int top = rcDraw.top + (int) ((extendY - 1) * h);
            rcLineY.set(rcDraw.left, top, rcDraw.right, top + Math.max(10, (int) h));
            can.drawRect(rcLineY, paint);
          }
        }
      }
    }
  }
}
